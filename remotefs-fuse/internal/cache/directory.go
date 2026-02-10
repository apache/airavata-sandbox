package cache

import (
	"sync"
	"time"

	pb "github.com/apache/airavata-sandbox/remotefs-fuse/proto/gen/remotefs"
)

// directoryEntry holds cached directory entries with expiration time.
type directoryEntry struct {
	entries   []*pb.DirEntry
	expiresAt time.Time
}

// DirectoryCache caches directory listings with TTL-based expiration.
// It stores the results of readdir operations to avoid repeated network calls
// when listing directory contents.
type DirectoryCache struct {
	mu      sync.RWMutex
	entries map[string]*directoryEntry
	ttl     time.Duration
	now     func() time.Time // for testing
}

// NewDirectoryCache creates a new DirectoryCache with the given TTL.
func NewDirectoryCache(ttl time.Duration) *DirectoryCache {
	if ttl <= 0 {
		ttl = 30 * time.Second
	}
	return &DirectoryCache{
		entries: make(map[string]*directoryEntry),
		ttl:     ttl,
		now:     time.Now,
	}
}

// Get retrieves cached directory entries for the given path.
// Returns the entries and true if found and not expired, otherwise nil and false.
func (c *DirectoryCache) Get(path string) ([]*pb.DirEntry, bool) {
	c.mu.RLock()
	entry, ok := c.entries[path]
	if !ok {
		c.mu.RUnlock()
		return nil, false
	}

	expired := c.now().After(entry.expiresAt)
	if !expired {
		result := cloneDirEntries(entry.entries)
		c.mu.RUnlock()
		return result, true
	}
	c.mu.RUnlock()

	// Upgrade to write lock to remove expired entry.
	c.mu.Lock()
	entry, ok = c.entries[path]
	if ok && c.now().After(entry.expiresAt) {
		delete(c.entries, path)
	}
	c.mu.Unlock()

	return nil, false
}

// Set stores directory entries for the given path with the configured TTL.
func (c *DirectoryCache) Set(path string, entries []*pb.DirEntry) {
	c.mu.Lock()
	defer c.mu.Unlock()

	c.entries[path] = &directoryEntry{
		entries:   cloneDirEntries(entries),
		expiresAt: c.now().Add(c.ttl),
	}
}

// SetWithTTL stores directory entries with a custom TTL.
func (c *DirectoryCache) SetWithTTL(path string, entries []*pb.DirEntry, ttl time.Duration) {
	c.mu.Lock()
	defer c.mu.Unlock()

	c.entries[path] = &directoryEntry{
		entries:   cloneDirEntries(entries),
		expiresAt: c.now().Add(ttl),
	}
}

// Invalidate removes the cached entry for the given path.
func (c *DirectoryCache) Invalidate(path string) {
	c.mu.Lock()
	defer c.mu.Unlock()
	delete(c.entries, path)
}

// InvalidatePrefix removes all entries with paths starting with the given prefix.
// Useful for invalidating all subdirectories under a path.
func (c *DirectoryCache) InvalidatePrefix(prefix string) {
	c.mu.Lock()
	defer c.mu.Unlock()

	for path := range c.entries {
		if len(path) >= len(prefix) && path[:len(prefix)] == prefix {
			delete(c.entries, path)
		}
	}
}

// Clear removes all entries from the cache.
func (c *DirectoryCache) Clear() {
	c.mu.Lock()
	defer c.mu.Unlock()
	c.entries = make(map[string]*directoryEntry)
}

// Count returns the number of directories in the cache (including expired ones).
func (c *DirectoryCache) Count() int {
	c.mu.RLock()
	defer c.mu.RUnlock()
	return len(c.entries)
}

// Cleanup removes expired entries from the cache.
// Can be called periodically to free memory.
func (c *DirectoryCache) Cleanup() int {
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

// AddEntry adds a single entry to a cached directory listing.
// Used when a file is created and we want to update the cache without invalidating.
// Returns true if the directory was in cache and was updated.
func (c *DirectoryCache) AddEntry(dirPath string, entry *pb.DirEntry) bool {
	c.mu.Lock()
	defer c.mu.Unlock()

	dirEntry, ok := c.entries[dirPath]
	if !ok || c.now().After(dirEntry.expiresAt) {
		return false
	}

	// Check if entry already exists
	for i, e := range dirEntry.entries {
		if e.Name == entry.Name {
			// Update existing entry by replacing it with a new copy
			dirEntry.entries[i] = cloneDirEntry(entry)
			return true
		}
	}

	// Add new entry (create a new slice to avoid race conditions)
	newEntries := make([]*pb.DirEntry, len(dirEntry.entries)+1)
	copy(newEntries, dirEntry.entries)
	newEntries[len(dirEntry.entries)] = cloneDirEntry(entry)
	dirEntry.entries = newEntries
	return true
}

// RemoveEntry removes a single entry from a cached directory listing.
// Used when a file is deleted and we want to update the cache without invalidating.
// Returns true if the directory was in cache and the entry was removed.
func (c *DirectoryCache) RemoveEntry(dirPath, name string) bool {
	c.mu.Lock()
	defer c.mu.Unlock()

	dirEntry, ok := c.entries[dirPath]
	if !ok || c.now().After(dirEntry.expiresAt) {
		return false
	}

	for i, e := range dirEntry.entries {
		if e.Name == name {
			// Remove entry by creating a new slice (avoid race conditions)
			newEntries := make([]*pb.DirEntry, len(dirEntry.entries)-1)
			copy(newEntries[:i], dirEntry.entries[:i])
			copy(newEntries[i:], dirEntry.entries[i+1:])
			dirEntry.entries = newEntries
			return true
		}
	}

	return false
}

// HasEntry checks if a directory has a specific entry cached.
// Returns true if the entry exists in the cache.
func (c *DirectoryCache) HasEntry(dirPath, name string) bool {
	c.mu.RLock()
	defer c.mu.RUnlock()

	dirEntry, ok := c.entries[dirPath]
	if !ok || c.now().After(dirEntry.expiresAt) {
		return false
	}

	for _, e := range dirEntry.entries {
		if e.Name == name {
			return true
		}
	}

	return false
}

// cloneDirEntries creates a deep copy of a slice of DirEntry protobuf messages.
func cloneDirEntries(entries []*pb.DirEntry) []*pb.DirEntry {
	if entries == nil {
		return nil
	}
	result := make([]*pb.DirEntry, len(entries))
	for i, e := range entries {
		result[i] = cloneDirEntry(e)
	}
	return result
}

// cloneDirEntry creates a deep copy of a DirEntry protobuf message.
func cloneDirEntry(e *pb.DirEntry) *pb.DirEntry {
	if e == nil {
		return nil
	}
	return &pb.DirEntry{
		Name: e.Name,
		Mode: e.Mode,
		Ino:  e.Ino,
	}
}
