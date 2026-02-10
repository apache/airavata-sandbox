//go:build linux

package cache

import (
	"container/list"
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"os"
	"path/filepath"
	"sync"
	"syscall"
)

// MmapDataCache implements a file-backed, memory-mapped block cache.
// Blocks are stored as individual files under cacheDir/blocks/{pathHash}/{blockIdx}.blk
// and accessed via mmap for zero-copy reads. Consistency is maintained through
// mtime validation on every read (no TTL expiration).
type MmapDataCache struct {
	mu        sync.RWMutex
	cacheDir  string                   // directory for block files
	blocksDir string                   // subdirectory: cacheDir/blocks
	maxSize   int64                    // maximum total cache size in bytes
	currSize  int64                    // current total size in bytes
	blockSize int64                    // size of each block in bytes
	blocks    map[blockKey]*mmapBlock  // cached blocks
	lru       *list.List               // LRU list for eviction (front = most recent)
	mtimes    map[string]int64         // tracked mtimes per file path
	enabled   bool                     // whether mmap cache is enabled
}

// mmapBlock holds a single mmap'd cache block.
type mmapBlock struct {
	key      blockKey      // block identifier
	data     []byte        // mmap'd slice (read-only view of file)
	fd       *os.File      // backing file descriptor
	filePath string        // path to block file on disk
	mtime    int64         // source file mtime when cached
	size     int64         // actual data size in this block
	elem     *list.Element // LRU list element
}

// NewMmapDataCache creates a new file-backed, memory-mapped block cache.
// If cacheDir is empty, the cache is disabled.
func NewMmapDataCache(cacheDir string, maxSize, blockSize int64) (*MmapDataCache, error) {
	if cacheDir == "" {
		return &MmapDataCache{enabled: false}, nil
	}

	// Expand home directory
	if cacheDir[0] == '~' {
		home, err := os.UserHomeDir()
		if err != nil {
			return nil, fmt.Errorf("expand home dir: %w", err)
		}
		cacheDir = filepath.Join(home, cacheDir[1:])
	}

	blocksDir := filepath.Join(cacheDir, "blocks")
	if err := os.MkdirAll(blocksDir, 0755); err != nil {
		return nil, fmt.Errorf("create blocks dir: %w", err)
	}

	if maxSize <= 0 {
		maxSize = 256 * 1024 * 1024 // 256MB default
	}
	if blockSize <= 0 {
		blockSize = 64 * 1024 // 64KB default
	}

	mc := &MmapDataCache{
		cacheDir:  cacheDir,
		blocksDir: blocksDir,
		maxSize:   maxSize,
		blockSize: blockSize,
		blocks:    make(map[blockKey]*mmapBlock),
		lru:       list.New(),
		mtimes:    make(map[string]int64),
		enabled:   true,
	}

	// Clean up any stale block files from previous runs
	mc.cleanStaleBlocks()

	return mc, nil
}

// Enabled returns whether the mmap cache is enabled.
func (mc *MmapDataCache) Enabled() bool {
	return mc.enabled
}

// hashPath creates a safe directory name from a file path using SHA256.
func (mc *MmapDataCache) hashPath(path string) string {
	h := sha256.Sum256([]byte(path))
	return hex.EncodeToString(h[:16]) // 32 hex chars
}

// blockFilePath returns the file path for a block.
func (mc *MmapDataCache) blockFilePath(path string, blockIndex int64) string {
	pathHash := mc.hashPath(path)
	return filepath.Join(mc.blocksDir, pathHash, fmt.Sprintf("%d.blk", blockIndex))
}

// Read reads data from the cache. Equivalent to ReadWithMtime with mtime=0.
func (mc *MmapDataCache) Read(path string, offset, size int64) ([]byte, bool) {
	return mc.ReadWithMtime(path, offset, size, 0)
}

// ReadWithMtime reads data from cache and validates against source file mtime.
// If mtime > 0 and differs from cached mtime, returns cache miss.
// This is the primary consistency mechanism - NO TTL expiration.
func (mc *MmapDataCache) ReadWithMtime(path string, offset, size, mtime int64) ([]byte, bool) {
	if !mc.enabled {
		return nil, false
	}

	mc.mu.Lock()
	defer mc.mu.Unlock()

	if size <= 0 {
		return nil, true
	}

	startBlock := offset / mc.blockSize
	endBlock := (offset + size - 1) / mc.blockSize

	result := make([]byte, 0, size)
	currentOffset := offset

	for blockIdx := startBlock; blockIdx <= endBlock; blockIdx++ {
		key := blockKey{path: path, blockIndex: blockIdx}
		block, ok := mc.blocks[key]
		if !ok {
			// Cache miss for this block
			return nil, false
		}

		// Check mtime if provided (stale detection) - this is the ONLY expiration mechanism
		if mtime > 0 && block.mtime != mtime {
			// Source file has changed - invalidate all blocks for this path
			mc.invalidateAllLocked(path)
			return nil, false
		}

		// Move to front of LRU
		mc.lru.MoveToFront(block.elem)

		// Calculate how much of this block we need
		blockStart := blockIdx * mc.blockSize
		blockEnd := blockStart + block.size

		// Calculate the portion of this block to read
		readStart := currentOffset - blockStart
		if readStart < 0 {
			readStart = 0
		}

		remaining := size - int64(len(result))
		readEnd := readStart + remaining
		if readEnd > block.size {
			readEnd = block.size
		}

		if readStart >= block.size {
			// This block doesn't have the data we need
			return nil, false
		}

		// Copy from mmap'd data
		result = append(result, block.data[readStart:readEnd]...)
		currentOffset = blockEnd
	}

	if len(result) == 0 && size > 0 {
		return nil, false
	}
	return result, true
}

// Write caches data for the given path and offset.
func (mc *MmapDataCache) Write(path string, offset int64, data []byte) {
	mc.WriteWithMtime(path, offset, data, 0)
}

// WriteWithMtime caches data with the source file's mtime for stale detection.
func (mc *MmapDataCache) WriteWithMtime(path string, offset int64, data []byte, mtime int64) {
	if !mc.enabled || len(data) == 0 {
		return
	}

	mc.mu.Lock()
	defer mc.mu.Unlock()

	// Update tracked mtime for this path
	if mtime > 0 {
		mc.mtimes[path] = mtime
	}

	startBlock := offset / mc.blockSize
	dataOffset := int64(0)

	for dataOffset < int64(len(data)) {
		blockIdx := startBlock + (dataOffset+offset%mc.blockSize)/mc.blockSize
		key := blockKey{path: path, blockIndex: blockIdx}

		blockStart := blockIdx * mc.blockSize
		offsetInBlock := offset + dataOffset - blockStart

		// Calculate how much data to write to this block
		spaceInBlock := mc.blockSize - offsetInBlock
		toWrite := int64(len(data)) - dataOffset
		if toWrite > spaceInBlock {
			toWrite = spaceInBlock
		}

		// Get or create block
		block, exists := mc.blocks[key]
		if exists {
			// Block exists - need to update it
			// For simplicity, we remove and recreate with new data
			mc.removeBlockLocked(key)
		}

		// Calculate the new block size
		neededSize := offsetInBlock + toWrite
		if exists && block.size > neededSize {
			neededSize = block.size
		}

		// Create new mmap'd block
		newBlock, err := mc.createBlock(key, neededSize, mtime)
		if err != nil {
			// Failed to create block, skip it
			dataOffset += toWrite
			continue
		}

		// Copy data into the block
		// First, copy existing data if any (for partial block updates)
		if exists && block.data != nil {
			copy(newBlock.data, block.data)
		}
		copy(newBlock.data[offsetInBlock:], data[dataOffset:dataOffset+toWrite])

		// Sync the data to disk via the file descriptor
		if newBlock.fd != nil {
			_ = newBlock.fd.Sync()
		}

		mc.blocks[key] = newBlock
		mc.currSize += newBlock.size
		dataOffset += toWrite

		// Evict if needed
		mc.evictIfNeeded()
	}
}

// createBlock creates a new mmap'd block file.
func (mc *MmapDataCache) createBlock(key blockKey, size int64, mtime int64) (*mmapBlock, error) {
	// Create directory for this path's blocks
	pathHash := mc.hashPath(key.path)
	pathDir := filepath.Join(mc.blocksDir, pathHash)
	if err := os.MkdirAll(pathDir, 0755); err != nil {
		return nil, fmt.Errorf("create path dir: %w", err)
	}

	// Create the block file
	filePath := mc.blockFilePath(key.path, key.blockIndex)
	fd, err := os.OpenFile(filePath, os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0644)
	if err != nil {
		return nil, fmt.Errorf("create block file: %w", err)
	}

	// Set file size
	if err := fd.Truncate(size); err != nil {
		fd.Close()
		os.Remove(filePath)
		return nil, fmt.Errorf("truncate block file: %w", err)
	}

	// Memory-map the file
	data, err := syscall.Mmap(int(fd.Fd()), 0, int(size), syscall.PROT_READ|syscall.PROT_WRITE, syscall.MAP_SHARED)
	if err != nil {
		fd.Close()
		os.Remove(filePath)
		return nil, fmt.Errorf("mmap block file: %w", err)
	}

	block := &mmapBlock{
		key:      key,
		data:     data,
		fd:       fd,
		filePath: filePath,
		mtime:    mtime,
		size:     size,
	}
	block.elem = mc.lru.PushFront(key)

	return block, nil
}

// Invalidate removes cached blocks that overlap with the given range.
func (mc *MmapDataCache) Invalidate(path string, offset, size int64) {
	if !mc.enabled {
		return
	}

	mc.mu.Lock()
	defer mc.mu.Unlock()

	if size <= 0 {
		return
	}

	startBlock := offset / mc.blockSize
	endBlock := (offset + size - 1) / mc.blockSize

	for blockIdx := startBlock; blockIdx <= endBlock; blockIdx++ {
		key := blockKey{path: path, blockIndex: blockIdx}
		mc.removeBlockLocked(key)
	}
}

// InvalidateAll removes all cached blocks for a given path.
func (mc *MmapDataCache) InvalidateAll(path string) {
	if !mc.enabled {
		return
	}

	mc.mu.Lock()
	defer mc.mu.Unlock()
	mc.invalidateAllLocked(path)
}

// invalidateAllLocked removes all cached blocks for a path (must hold lock).
func (mc *MmapDataCache) invalidateAllLocked(path string) {
	// Collect keys to remove
	toRemove := make([]blockKey, 0)
	for key := range mc.blocks {
		if key.path == path {
			toRemove = append(toRemove, key)
		}
	}

	for _, key := range toRemove {
		mc.removeBlockLocked(key)
	}

	// Remove tracked mtime
	delete(mc.mtimes, path)

	// Remove the path's directory
	pathHash := mc.hashPath(path)
	pathDir := filepath.Join(mc.blocksDir, pathHash)
	os.RemoveAll(pathDir)
}

// InvalidateBeyond removes cached blocks for a path that extend beyond the given size.
func (mc *MmapDataCache) InvalidateBeyond(path string, size int64) {
	if !mc.enabled {
		return
	}

	mc.mu.Lock()
	defer mc.mu.Unlock()

	lastValidBlock := size / mc.blockSize

	// Collect keys to remove
	toRemove := make([]blockKey, 0)
	for key := range mc.blocks {
		if key.path == path && key.blockIndex > lastValidBlock {
			toRemove = append(toRemove, key)
		}
	}

	for _, key := range toRemove {
		mc.removeBlockLocked(key)
	}

	// Truncate the last valid block if needed
	if size > 0 {
		key := blockKey{path: path, blockIndex: lastValidBlock}
		if block, ok := mc.blocks[key]; ok {
			offsetInBlock := size - (lastValidBlock * mc.blockSize)
			if block.size > offsetInBlock {
				// Need to truncate this block - remove and recreate smaller
				mc.removeBlockLocked(key)
			}
		}
	}
}

// Clear removes all entries from the cache.
func (mc *MmapDataCache) Clear() {
	if !mc.enabled {
		return
	}

	mc.mu.Lock()
	defer mc.mu.Unlock()

	for key := range mc.blocks {
		mc.removeBlockLocked(key)
	}

	mc.blocks = make(map[blockKey]*mmapBlock)
	mc.lru = list.New()
	mc.currSize = 0
	mc.mtimes = make(map[string]int64)

	// Remove all files in blocks directory
	os.RemoveAll(mc.blocksDir)
	os.MkdirAll(mc.blocksDir, 0755)
}

// SetMtime records the mtime for a path, used for stale detection.
func (mc *MmapDataCache) SetMtime(path string, mtime int64) {
	if !mc.enabled {
		return
	}
	mc.mu.Lock()
	defer mc.mu.Unlock()
	mc.mtimes[path] = mtime
}

// GetMtime returns the tracked mtime for a path, or 0 if not tracked.
func (mc *MmapDataCache) GetMtime(path string) int64 {
	if !mc.enabled {
		return 0
	}
	mc.mu.RLock()
	defer mc.mu.RUnlock()
	return mc.mtimes[path]
}

// IsMtimeStale checks if the given mtime differs from the cached mtime.
func (mc *MmapDataCache) IsMtimeStale(path string, mtime int64) bool {
	if !mc.enabled {
		return false
	}
	mc.mu.RLock()
	defer mc.mu.RUnlock()
	cached, ok := mc.mtimes[path]
	if !ok {
		return false
	}
	return cached != mtime
}

// Size returns the current size of the cache in bytes.
func (mc *MmapDataCache) Size() int64 {
	if !mc.enabled {
		return 0
	}
	mc.mu.RLock()
	defer mc.mu.RUnlock()
	return mc.currSize
}

// BlockCount returns the number of blocks currently cached.
func (mc *MmapDataCache) BlockCount() int {
	if !mc.enabled {
		return 0
	}
	mc.mu.RLock()
	defer mc.mu.RUnlock()
	return len(mc.blocks)
}

// removeBlockLocked removes a block from the cache (must hold lock).
func (mc *MmapDataCache) removeBlockLocked(key blockKey) {
	block, ok := mc.blocks[key]
	if !ok {
		return
	}

	// Unmap the memory
	if block.data != nil {
		syscall.Munmap(block.data)
	}

	// Close file descriptor
	if block.fd != nil {
		block.fd.Close()
	}

	// Remove the file
	os.Remove(block.filePath)

	// Update size
	mc.currSize -= block.size

	// Remove from LRU
	if block.elem != nil {
		mc.lru.Remove(block.elem)
	}

	// Remove from map
	delete(mc.blocks, key)
}

// evictIfNeeded removes least recently used blocks until under maxSize.
func (mc *MmapDataCache) evictIfNeeded() {
	for mc.currSize > mc.maxSize && mc.lru.Len() > 0 {
		// Remove from back of LRU (least recently used)
		elem := mc.lru.Back()
		if elem == nil {
			break
		}
		key := elem.Value.(blockKey)
		mc.removeBlockLocked(key)
	}
}

// cleanStaleBlocks removes any orphaned block files from previous runs.
func (mc *MmapDataCache) cleanStaleBlocks() {
	// Just remove the entire blocks directory contents
	os.RemoveAll(mc.blocksDir)
	os.MkdirAll(mc.blocksDir, 0755)
}

// Close closes all mmap'd blocks and cleans up.
func (mc *MmapDataCache) Close() {
	mc.Clear()
}

// Cleanup is a no-op for MmapDataCache (no TTL expiration).
// This method exists for interface compatibility with DataCache.
func (mc *MmapDataCache) Cleanup() int {
	// No TTL expiration in mmap cache - blocks only expire via mtime invalidation
	return 0
}
