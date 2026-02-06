package cache

import (
	"sync"
	"time"

	pb "github.com/you/remotefs/proto/gen/remotefs"
)

// metadataEntry holds a cached file attribute with expiration time.
type metadataEntry struct {
	attr      *pb.Attr
	expiresAt time.Time
}

// MetadataCache caches file attributes with TTL-based expiration.
// It stores attributes like size, mode, mtime, etc. for quick access
// without needing to query the remote server.
type MetadataCache struct {
	mu      sync.RWMutex
	entries map[string]*metadataEntry
	ttl     time.Duration
	now     func() time.Time // for testing
}

// NewMetadataCache creates a new MetadataCache with the given TTL.
func NewMetadataCache(ttl time.Duration) *MetadataCache {
	if ttl <= 0 {
		ttl = 30 * time.Second
	}
	return &MetadataCache{
		entries: make(map[string]*metadataEntry),
		ttl:     ttl,
		now:     time.Now,
	}
}

// Get retrieves cached attributes for the given path.
// Returns the attributes and true if found and not expired, otherwise nil and false.
func (c *MetadataCache) Get(path string) (*pb.Attr, bool) {
	c.mu.RLock()
	entry, ok := c.entries[path]
	c.mu.RUnlock()

	if !ok {
		return nil, false
	}

	if c.now().After(entry.expiresAt) {
		// Expired - remove it
		c.mu.Lock()
		// Double-check after acquiring write lock
		if entry, ok := c.entries[path]; ok && c.now().After(entry.expiresAt) {
			delete(c.entries, path)
		}
		c.mu.Unlock()
		return nil, false
	}

	// Return a copy to prevent modification
	return cloneAttr(entry.attr), true
}

// Set stores attributes for the given path with the configured TTL.
func (c *MetadataCache) Set(path string, attr *pb.Attr) {
	if attr == nil {
		return
	}

	c.mu.Lock()
	defer c.mu.Unlock()

	c.entries[path] = &metadataEntry{
		attr:      cloneAttr(attr),
		expiresAt: c.now().Add(c.ttl),
	}
}

// SetWithTTL stores attributes with a custom TTL.
func (c *MetadataCache) SetWithTTL(path string, attr *pb.Attr, ttl time.Duration) {
	if attr == nil {
		return
	}

	c.mu.Lock()
	defer c.mu.Unlock()

	c.entries[path] = &metadataEntry{
		attr:      cloneAttr(attr),
		expiresAt: c.now().Add(ttl),
	}
}

// Invalidate removes the cached entry for the given path.
func (c *MetadataCache) Invalidate(path string) {
	c.mu.Lock()
	defer c.mu.Unlock()
	delete(c.entries, path)
}

// InvalidatePrefix removes all entries with paths starting with the given prefix.
// Useful for invalidating all entries under a directory.
func (c *MetadataCache) InvalidatePrefix(prefix string) {
	c.mu.Lock()
	defer c.mu.Unlock()

	for path := range c.entries {
		if len(path) >= len(prefix) && path[:len(prefix)] == prefix {
			delete(c.entries, path)
		}
	}
}

// Clear removes all entries from the cache.
func (c *MetadataCache) Clear() {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.entries = make(map[string]*metadataEntry)
}

// Count returns the number of entries in the cache (including expired ones).
func (c *MetadataCache) Count() int {
	c.mu.RLock()
	defer c.mu.RUnlock()
	return len(c.entries)
}

// Cleanup removes expired entries from the cache.
// Can be called periodically to free memory.
func (c *MetadataCache) Cleanup() int {
	c.mu.Lock()
	defer c.mu.Unlock()

	now := c.now()
	removed := 0
	for path, entry := range c.entries {
		if now.After(entry.expiresAt) {
			delete(c.entries, path)
			removed++
		}
	}
	return removed
}

// UpdateSize updates the cached size for a file without resetting the TTL.
// Returns true if the entry was found and updated.
func (c *MetadataCache) UpdateSize(path string, newSize uint64) bool {
	c.mu.Lock()
	defer c.mu.Unlock()

	entry, ok := c.entries[path]
	if !ok || c.now().After(entry.expiresAt) {
		return false
	}

	entry.attr.Size = newSize
	return true
}

// cloneAttr creates a deep copy of an Attr protobuf message.
func cloneAttr(attr *pb.Attr) *pb.Attr {
	if attr == nil {
		return nil
	}
	return &pb.Attr{
		Ino:     attr.Ino,
		Size:    attr.Size,
		Mode:    attr.Mode,
		Uid:     attr.Uid,
		Gid:     attr.Gid,
		Atime:   attr.Atime,
		Mtime:   attr.Mtime,
		Ctime:   attr.Ctime,
		Blksize: attr.Blksize,
		Blocks:  attr.Blocks,
	}
}
