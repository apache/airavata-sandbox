package cache

import (
	"container/list"
	"sync"
	"time"
)

// blockKey uniquely identifies a cached block.
type blockKey struct {
	path       string
	blockIndex int64
}

// cacheBlock holds a single cached data block.
type cacheBlock struct {
	key       blockKey
	data      []byte
	elem      *list.Element // pointer to LRU list element
	expiresAt time.Time     // TTL-based expiration
	mtime     int64         // source file mtime when cached (for stale detection)
}

// DataCache implements a block-based LRU data cache with TTL support.
// Data is stored in fixed-size blocks for efficient partial reads
// and memory management. Blocks expire based on TTL and source file mtime.
type DataCache struct {
	mu        sync.RWMutex
	maxSize   int64         // maximum total cache size in bytes
	blockSize int64         // size of each block in bytes
	currSize  int64         // current total size in bytes
	ttl       time.Duration // time-to-live for cached blocks
	now       func() time.Time // for testing

	// blocks maps blockKey to cacheBlock
	blocks map[blockKey]*cacheBlock

	// lru tracks access order for eviction (front = most recent)
	lru *list.List

	// mtimes tracks the last known mtime for each file path
	// used for stale detection when source changes
	mtimes map[string]int64
}

// NewDataCache creates a new DataCache with the given maximum size and block size.
func NewDataCache(maxSize, blockSize int64) *DataCache {
	return NewDataCacheWithTTL(maxSize, blockSize, 30*time.Second)
}

// NewDataCacheWithTTL creates a new DataCache with custom TTL.
func NewDataCacheWithTTL(maxSize, blockSize int64, ttl time.Duration) *DataCache {
	if blockSize <= 0 {
		blockSize = 64 * 1024 // 64KB default
	}
	if maxSize <= 0 {
		maxSize = 256 * 1024 * 1024 // 256MB default
	}
	if ttl <= 0 {
		ttl = 30 * time.Second
	}
	return &DataCache{
		maxSize:   maxSize,
		blockSize: blockSize,
		ttl:       ttl,
		now:       time.Now,
		blocks:    make(map[blockKey]*cacheBlock),
		lru:       list.New(),
		mtimes:    make(map[string]int64),
	}
}

// Read attempts to read data from cache for the given path, offset, and size.
// Returns the data and a boolean indicating if the read was complete (cache hit).
// If partial data is found, returns what's available with complete=false.
func (c *DataCache) Read(path string, offset, size int64) ([]byte, bool) {
	return c.ReadWithMtime(path, offset, size, 0)
}

// ReadWithMtime reads from cache but validates against source file mtime.
// If mtime > 0 and differs from cached mtime, returns cache miss.
// This enables stale detection when source files change.
func (c *DataCache) ReadWithMtime(path string, offset, size, mtime int64) ([]byte, bool) {
	if size <= 0 {
		return nil, true
	}

	now := c.now()
	startBlock := offset / c.blockSize
	endBlock := (offset + size - 1) / c.blockSize

	// Phase 1: Try read-only check first (fast path for cache hits)
	c.mu.RLock()
	needsWrite := false
	needsInvalidation := false
	var blocksToUpdate []blockKey

	result := make([]byte, 0, size)
	currentOffset := offset
	allHit := true

	for blockIdx := startBlock; blockIdx <= endBlock; blockIdx++ {
		key := blockKey{path: path, blockIndex: blockIdx}
		block, ok := c.blocks[key]
		if !ok {
			// Cache miss for this block
			allHit = false
			break
		}

		// Check TTL expiration
		if now.After(block.expiresAt) {
			// Block has expired - need write lock to remove
			needsWrite = true
			allHit = false
			break
		}

		// Check mtime if provided (stale detection)
		if mtime > 0 && block.mtime != mtime {
			// Source file has changed - need write lock to invalidate
			needsInvalidation = true
			allHit = false
			break
		}

		// Track blocks to update LRU
		blocksToUpdate = append(blocksToUpdate, key)

		// Calculate how much of this block we need
		blockStart := blockIdx * c.blockSize
		blockEnd := blockStart + int64(len(block.data))

		// Calculate the portion of this block to read
		readStart := currentOffset - blockStart
		if readStart < 0 {
			readStart = 0
		}

		remaining := size - int64(len(result))
		readEnd := readStart + remaining
		if readEnd > int64(len(block.data)) {
			readEnd = int64(len(block.data))
		}

		if readStart >= int64(len(block.data)) {
			// This block doesn't have the data we need
			allHit = false
			break
		}

		result = append(result, block.data[readStart:readEnd]...)
		currentOffset = blockEnd
	}
	c.mu.RUnlock()

	// If complete cache hit, update LRU with write lock (can be batched)
	if allHit && len(result) > 0 {
		// Only update LRU if we have blocks to update
		if len(blocksToUpdate) > 0 {
			c.mu.Lock()
			for _, key := range blocksToUpdate {
				if block, ok := c.blocks[key]; ok {
					c.lru.MoveToFront(block.elem)
				}
			}
			c.mu.Unlock()
		}
		return result, true
	}

	// Phase 2: Handle cache misses requiring write lock
	if needsWrite || needsInvalidation {
		c.mu.Lock()
		if needsInvalidation {
			c.invalidateAllLocked(path)
		} else {
			// Remove expired blocks
			for blockIdx := startBlock; blockIdx <= endBlock; blockIdx++ {
				key := blockKey{path: path, blockIndex: blockIdx}
				if block, ok := c.blocks[key]; ok && now.After(block.expiresAt) {
					c.removeBlock(key)
				}
			}
		}
		c.mu.Unlock()
	}

	// Cache miss
	if len(result) == 0 && size > 0 {
		return nil, false
	}
	return nil, false
}

// Write caches data for the given path and offset.
// The data is split into blocks and stored in the cache.
func (c *DataCache) Write(path string, offset int64, data []byte) {
	c.WriteWithMtime(path, offset, data, 0)
}

// WriteWithMtime caches data with the source file's mtime for stale detection.
func (c *DataCache) WriteWithMtime(path string, offset int64, data []byte, mtime int64) {
	c.mu.Lock()
	defer c.mu.Unlock()

	if len(data) == 0 {
		return
	}

	now := c.now()
	expiresAt := now.Add(c.ttl)

	// Update tracked mtime for this path
	if mtime > 0 {
		c.mtimes[path] = mtime
	}

	startBlock := offset / c.blockSize
	dataOffset := int64(0)

	for dataOffset < int64(len(data)) {
		blockIdx := startBlock + (dataOffset+offset%c.blockSize)/c.blockSize
		key := blockKey{path: path, blockIndex: blockIdx}

		blockStart := blockIdx * c.blockSize
		offsetInBlock := offset + dataOffset - blockStart

		// Get or create block
		block, exists := c.blocks[key]
		if !exists {
			block = &cacheBlock{
				key:       key,
				data:      make([]byte, 0, c.blockSize),
				expiresAt: expiresAt,
				mtime:     mtime,
			}
			block.elem = c.lru.PushFront(key)
			c.blocks[key] = block
		} else {
			// Move to front of LRU and refresh expiration
			c.lru.MoveToFront(block.elem)
			block.expiresAt = expiresAt
			if mtime > 0 {
				block.mtime = mtime
			}
		}

		// Calculate how much data to write to this block
		spaceInBlock := c.blockSize - offsetInBlock
		toWrite := int64(len(data)) - dataOffset
		if toWrite > spaceInBlock {
			toWrite = spaceInBlock
		}

		// Ensure block has enough capacity
		neededSize := offsetInBlock + toWrite
		if neededSize > int64(len(block.data)) {
			// Extend block data
			oldSize := int64(len(block.data))
			if neededSize > int64(cap(block.data)) {
				newData := make([]byte, neededSize, c.blockSize)
				copy(newData, block.data)
				block.data = newData
			} else {
				block.data = block.data[:neededSize]
			}
			c.currSize += neededSize - oldSize
		}

		// Copy data into block
		copy(block.data[offsetInBlock:], data[dataOffset:dataOffset+toWrite])
		dataOffset += toWrite

		// Evict if necessary
		c.evictIfNeeded()
	}
}

// Invalidate removes cached blocks that overlap with the given range.
func (c *DataCache) Invalidate(path string, offset, size int64) {
	c.mu.Lock()
	defer c.mu.Unlock()

	if size <= 0 {
		return
	}

	startBlock := offset / c.blockSize
	endBlock := (offset + size - 1) / c.blockSize

	for blockIdx := startBlock; blockIdx <= endBlock; blockIdx++ {
		key := blockKey{path: path, blockIndex: blockIdx}
		c.removeBlock(key)
	}
}

// InvalidateAll removes all cached blocks for a given path.
func (c *DataCache) InvalidateAll(path string) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.invalidateAllLocked(path)
}

// invalidateAllLocked removes all cached blocks for a path (must hold lock).
func (c *DataCache) invalidateAllLocked(path string) {
	// Collect keys to remove (can't modify map while iterating)
	toRemove := make([]blockKey, 0)
	for key := range c.blocks {
		if key.path == path {
			toRemove = append(toRemove, key)
		}
	}

	for _, key := range toRemove {
		c.removeBlock(key)
	}

	// Also remove tracked mtime
	delete(c.mtimes, path)
}

// InvalidateBeyond removes cached blocks for a path that extend beyond the given size.
// Used when a file is truncated.
func (c *DataCache) InvalidateBeyond(path string, size int64) {
	c.mu.Lock()
	defer c.mu.Unlock()

	lastValidBlock := size / c.blockSize

	// Collect keys to remove
	toRemove := make([]blockKey, 0)
	for key := range c.blocks {
		if key.path == path && key.blockIndex > lastValidBlock {
			toRemove = append(toRemove, key)
		}
	}

	for _, key := range toRemove {
		c.removeBlock(key)
	}

	// Truncate the last valid block if needed
	if size > 0 {
		key := blockKey{path: path, blockIndex: lastValidBlock}
		if block, ok := c.blocks[key]; ok {
			offsetInBlock := size - (lastValidBlock * c.blockSize)
			if int64(len(block.data)) > offsetInBlock {
				oldSize := int64(len(block.data))
				block.data = block.data[:offsetInBlock]
				c.currSize -= oldSize - offsetInBlock
			}
		}
	}
}

// Clear removes all entries from the cache.
func (c *DataCache) Clear() {
	c.mu.Lock()
	defer c.mu.Unlock()

	c.blocks = make(map[blockKey]*cacheBlock)
	c.lru = list.New()
	c.currSize = 0
	c.mtimes = make(map[string]int64)
}

// Cleanup removes expired blocks from the cache.
// Returns the number of blocks removed.
// Can be called periodically to free memory from stale entries.
func (c *DataCache) Cleanup() int {
	c.mu.Lock()
	defer c.mu.Unlock()

	now := c.now()
	toRemove := make([]blockKey, 0)

	for key, block := range c.blocks {
		if now.After(block.expiresAt) {
			toRemove = append(toRemove, key)
		}
	}

	for _, key := range toRemove {
		c.removeBlock(key)
	}

	return len(toRemove)
}

// SetMtime records the mtime for a path, used for stale detection.
func (c *DataCache) SetMtime(path string, mtime int64) {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.mtimes[path] = mtime
}

// GetMtime returns the tracked mtime for a path, or 0 if not tracked.
func (c *DataCache) GetMtime(path string) int64 {
	c.mu.RLock()
	defer c.mu.RUnlock()
	return c.mtimes[path]
}

// IsMtimeStale checks if the given mtime differs from the cached mtime.
// Returns true if the source file has changed.
func (c *DataCache) IsMtimeStale(path string, mtime int64) bool {
	c.mu.RLock()
	defer c.mu.RUnlock()
	cached, ok := c.mtimes[path]
	if !ok {
		return false // No cached mtime, not stale
	}
	return cached != mtime
}

// Size returns the current size of the cache in bytes.
func (c *DataCache) Size() int64 {
	c.mu.RLock()
	defer c.mu.RUnlock()
	return c.currSize
}

// BlockCount returns the number of blocks currently cached.
func (c *DataCache) BlockCount() int {
	c.mu.RLock()
	defer c.mu.RUnlock()
	return len(c.blocks)
}

// removeBlock removes a block from the cache (must hold lock).
func (c *DataCache) removeBlock(key blockKey) {
	if block, ok := c.blocks[key]; ok {
		c.currSize -= int64(len(block.data))
		c.lru.Remove(block.elem)
		delete(c.blocks, key)
	}
}

// evictIfNeeded removes least recently used blocks until under maxSize (must hold lock).
func (c *DataCache) evictIfNeeded() {
	for c.currSize > c.maxSize && c.lru.Len() > 0 {
		// Remove from back of LRU (least recently used)
		elem := c.lru.Back()
		if elem == nil {
			break
		}
		key := elem.Value.(blockKey)
		c.removeBlock(key)
	}
}
