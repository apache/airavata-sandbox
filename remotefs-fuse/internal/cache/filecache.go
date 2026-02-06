//go:build linux

package cache

import (
	"container/list"
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"io"
	"os"
	"path/filepath"
	"sync"
	"syscall"
)

// FileCache stores downloaded files on disk for FUSE passthrough mode.
// Files are stored with hashed names, tracked by mtime, and evicted via LRU.
type FileCache struct {
	mu       sync.RWMutex
	cacheDir string                  // directory to store cached files
	maxSize  int64                   // maximum total cache size in bytes
	currSize int64                   // current total size in bytes
	files    map[string]*CachedFile  // remotePath -> cachedFile
	lru      *list.List              // LRU list for eviction
	enabled  bool                    // whether file cache is enabled
}

// CachedFile represents a cached file on disk.
type CachedFile struct {
	remotePath string        // original remote path
	localPath  string        // path in cache directory
	size       int64         // file size in bytes
	mtime      int64         // source file mtime when cached
	complete   bool          // true if file is fully downloaded
	fd         *os.File      // open file descriptor for passthrough
	elem       *list.Element // LRU list element
	refCount   int           // number of active file handles using this cache
}

// NewFileCache creates a new file-backed cache.
// If cacheDir is empty, file caching is disabled.
func NewFileCache(cacheDir string, maxSize int64) (*FileCache, error) {
	if cacheDir == "" {
		return &FileCache{enabled: false}, nil
	}

	// Expand home directory
	if cacheDir[0] == '~' {
		home, err := os.UserHomeDir()
		if err != nil {
			return nil, fmt.Errorf("expand home dir: %w", err)
		}
		cacheDir = filepath.Join(home, cacheDir[1:])
	}

	// Create cache directory
	if err := os.MkdirAll(cacheDir, 0755); err != nil {
		return nil, fmt.Errorf("create cache dir: %w", err)
	}

	if maxSize <= 0 {
		maxSize = 1024 * 1024 * 1024 // 1GB default
	}

	fc := &FileCache{
		cacheDir: cacheDir,
		maxSize:  maxSize,
		files:    make(map[string]*CachedFile),
		lru:      list.New(),
		enabled:  true,
	}

	// Clean up any stale cache files from previous runs
	fc.cleanStaleFiles()

	return fc, nil
}

// Enabled returns whether file caching is enabled.
func (fc *FileCache) Enabled() bool {
	return fc.enabled
}

// hashPath creates a safe filename from a remote path.
func (fc *FileCache) hashPath(remotePath string) string {
	h := sha256.Sum256([]byte(remotePath))
	return hex.EncodeToString(h[:16]) // Use first 16 bytes (32 hex chars)
}

// GetOrCreate gets an existing cached file or creates a new one.
// If the file exists but has a different mtime, it's invalidated.
// The downloadFn is called to download file content if not cached or stale.
func (fc *FileCache) GetOrCreate(remotePath string, size, mtime int64, downloadFn func(offset, length int64) ([]byte, error)) (*CachedFile, error) {
	if !fc.enabled {
		return nil, fmt.Errorf("file cache not enabled")
	}

	fc.mu.Lock()
	defer fc.mu.Unlock()

	// Check if already cached
	if cached, ok := fc.files[remotePath]; ok {
		// Check if stale (mtime changed)
		if cached.mtime != mtime {
			// Invalidate and re-download
			fc.removeLocked(remotePath)
		} else if cached.complete {
			// Move to front of LRU
			fc.lru.MoveToFront(cached.elem)
			cached.refCount++
			return cached, nil
		}
	}

	// Create new cache file
	localName := fc.hashPath(remotePath) + filepath.Ext(remotePath)
	localPath := filepath.Join(fc.cacheDir, localName)

	// Create and open the cache file
	fd, err := os.OpenFile(localPath, os.O_CREATE|os.O_RDWR|os.O_TRUNC, 0644)
	if err != nil {
		return nil, fmt.Errorf("create cache file: %w", err)
	}

	// Pre-allocate space for the file
	if size > 0 {
		if err := syscall.Fallocate(int(fd.Fd()), 0, 0, size); err != nil {
			// Fallocate not supported, try truncate
			if err := fd.Truncate(size); err != nil {
				fd.Close()
				os.Remove(localPath)
				return nil, fmt.Errorf("allocate space: %w", err)
			}
		}
	}

	cached := &CachedFile{
		remotePath: remotePath,
		localPath:  localPath,
		size:       size,
		mtime:      mtime,
		complete:   false,
		fd:         fd,
		refCount:   1,
	}
	cached.elem = fc.lru.PushFront(remotePath)
	fc.files[remotePath] = cached

	// Download the file content
	// Release lock during download to avoid blocking other operations
	fc.mu.Unlock()
	err = fc.downloadFile(cached, downloadFn)
	fc.mu.Lock()

	if err != nil {
		// Remove failed cache entry
		fc.removeLocked(remotePath)
		return nil, fmt.Errorf("download file: %w", err)
	}

	// Update cache size
	fc.currSize += size

	// Mark as complete
	cached.complete = true

	// Evict if needed
	fc.evictIfNeeded()

	return cached, nil
}

// downloadFile downloads the entire file content to the cache file.
func (fc *FileCache) downloadFile(cached *CachedFile, downloadFn func(offset, length int64) ([]byte, error)) error {
	const chunkSize = int64(1024 * 1024) // 1MB chunks

	offset := int64(0)
	for offset < cached.size {
		length := chunkSize
		if offset+length > cached.size {
			length = cached.size - offset
		}

		data, err := downloadFn(offset, length)
		if err != nil {
			return fmt.Errorf("download chunk at offset %d: %w", offset, err)
		}

		if len(data) == 0 {
			break // EOF
		}

		n, err := cached.fd.WriteAt(data, offset)
		if err != nil {
			return fmt.Errorf("write chunk at offset %d: %w", offset, err)
		}
		offset += int64(n)
	}

	// Sync to ensure data is on disk for passthrough
	if err := cached.fd.Sync(); err != nil {
		return fmt.Errorf("sync cache file: %w", err)
	}

	return nil
}

// Get retrieves a cached file if it exists and is valid.
func (fc *FileCache) Get(remotePath string, mtime int64) *CachedFile {
	if !fc.enabled {
		return nil
	}

	fc.mu.Lock()
	defer fc.mu.Unlock()

	cached, ok := fc.files[remotePath]
	if !ok {
		return nil
	}

	// Check if stale
	if cached.mtime != mtime {
		fc.removeLocked(remotePath)
		return nil
	}

	if !cached.complete {
		return nil
	}

	// Move to front of LRU
	fc.lru.MoveToFront(cached.elem)
	cached.refCount++
	return cached
}

// PassthroughFd returns the file descriptor for passthrough if the file
// is fully cached and passthrough is available.
func (fc *FileCache) PassthroughFd(remotePath string, mtime int64) (int, bool) {
	if !fc.enabled {
		return 0, false
	}

	fc.mu.RLock()
	defer fc.mu.RUnlock()

	cached, ok := fc.files[remotePath]
	if !ok || !cached.complete || cached.fd == nil {
		return 0, false
	}

	// Check if stale
	if cached.mtime != mtime {
		return 0, false
	}

	return int(cached.fd.Fd()), true
}

// Release decrements the reference count for a cached file.
// The file is not removed until its ref count reaches 0 and it's evicted.
func (fc *FileCache) Release(remotePath string) {
	if !fc.enabled {
		return
	}

	fc.mu.Lock()
	defer fc.mu.Unlock()

	if cached, ok := fc.files[remotePath]; ok {
		cached.refCount--
	}
}

// Invalidate removes a cached file.
func (fc *FileCache) Invalidate(remotePath string) {
	if !fc.enabled {
		return
	}

	fc.mu.Lock()
	defer fc.mu.Unlock()

	fc.removeLocked(remotePath)
}

// removeLocked removes a cached file (must hold lock).
func (fc *FileCache) removeLocked(remotePath string) {
	cached, ok := fc.files[remotePath]
	if !ok {
		return
	}

	// Close file descriptor
	if cached.fd != nil {
		cached.fd.Close()
	}

	// Remove from disk
	os.Remove(cached.localPath)

	// Update size
	fc.currSize -= cached.size

	// Remove from LRU
	if cached.elem != nil {
		fc.lru.Remove(cached.elem)
	}

	// Remove from map
	delete(fc.files, remotePath)
}

// evictIfNeeded removes least recently used files until under maxSize.
func (fc *FileCache) evictIfNeeded() {
	for fc.currSize > fc.maxSize && fc.lru.Len() > 0 {
		// Remove from back of LRU (least recently used)
		elem := fc.lru.Back()
		if elem == nil {
			break
		}
		remotePath := elem.Value.(string)
		cached := fc.files[remotePath]

		// Don't evict files with active references
		if cached != nil && cached.refCount > 0 {
			// Move to front and try next
			fc.lru.MoveToFront(elem)
			continue
		}

		fc.removeLocked(remotePath)
	}
}

// cleanStaleFiles removes any orphaned cache files from previous runs.
func (fc *FileCache) cleanStaleFiles() {
	entries, err := os.ReadDir(fc.cacheDir)
	if err != nil {
		return
	}

	for _, entry := range entries {
		if entry.IsDir() {
			continue
		}
		path := filepath.Join(fc.cacheDir, entry.Name())
		os.Remove(path)
	}
}

// Clear removes all cached files.
func (fc *FileCache) Clear() {
	if !fc.enabled {
		return
	}

	fc.mu.Lock()
	defer fc.mu.Unlock()

	for remotePath := range fc.files {
		fc.removeLocked(remotePath)
	}
}

// Size returns the current cache size in bytes.
func (fc *FileCache) Size() int64 {
	fc.mu.RLock()
	defer fc.mu.RUnlock()
	return fc.currSize
}

// Count returns the number of cached files.
func (fc *FileCache) Count() int {
	fc.mu.RLock()
	defer fc.mu.RUnlock()
	return len(fc.files)
}

// Close closes all file descriptors and clears the cache.
func (fc *FileCache) Close() {
	fc.Clear()
}

// Read reads data from a cached file.
func (cf *CachedFile) Read(offset int64, size int) ([]byte, error) {
	if cf.fd == nil {
		return nil, fmt.Errorf("cache file not open")
	}

	data := make([]byte, size)
	n, err := cf.fd.ReadAt(data, offset)
	if err != nil && err != io.EOF {
		return nil, err
	}
	return data[:n], nil
}

// Fd returns the underlying file descriptor.
func (cf *CachedFile) Fd() int {
	if cf.fd == nil {
		return -1
	}
	return int(cf.fd.Fd())
}

// IsComplete returns whether the file is fully cached.
func (cf *CachedFile) IsComplete() bool {
	return cf.complete
}

// LocalPath returns the path to the cached file on disk.
func (cf *CachedFile) LocalPath() string {
	return cf.localPath
}
