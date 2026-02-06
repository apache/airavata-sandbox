//go:build !linux

package cache

import (
	"fmt"
)

// FileCache is a stub for non-Linux platforms.
// FUSE passthrough is only supported on Linux.
type FileCache struct {
	enabled bool
}

// CachedFile is a stub for non-Linux platforms.
type CachedFile struct {
	remotePath string
	localPath  string
	size       int64
	mtime      int64
	complete   bool
	refCount   int
}

// NewFileCache creates a disabled file cache on non-Linux platforms.
func NewFileCache(cacheDir string, maxSize int64) (*FileCache, error) {
	return &FileCache{enabled: false}, nil
}

// Enabled returns false on non-Linux platforms.
func (fc *FileCache) Enabled() bool {
	return false
}

// GetOrCreate returns an error on non-Linux platforms.
func (fc *FileCache) GetOrCreate(remotePath string, size, mtime int64, downloadFn func(offset, length int64) ([]byte, error)) (*CachedFile, error) {
	return nil, fmt.Errorf("file cache not supported on this platform")
}

// Get returns nil on non-Linux platforms.
func (fc *FileCache) Get(remotePath string, mtime int64) *CachedFile {
	return nil
}

// PassthroughFd returns false on non-Linux platforms.
func (fc *FileCache) PassthroughFd(remotePath string, mtime int64) (int, bool) {
	return 0, false
}

// Release is a no-op on non-Linux platforms.
func (fc *FileCache) Release(remotePath string) {}

// Invalidate is a no-op on non-Linux platforms.
func (fc *FileCache) Invalidate(remotePath string) {}

// Clear is a no-op on non-Linux platforms.
func (fc *FileCache) Clear() {}

// Size returns 0 on non-Linux platforms.
func (fc *FileCache) Size() int64 {
	return 0
}

// Count returns 0 on non-Linux platforms.
func (fc *FileCache) Count() int {
	return 0
}

// Close is a no-op on non-Linux platforms.
func (fc *FileCache) Close() {}

// Read returns an error on non-Linux platforms.
func (cf *CachedFile) Read(offset int64, size int) ([]byte, error) {
	return nil, fmt.Errorf("file cache not supported on this platform")
}

// Fd returns -1 on non-Linux platforms.
func (cf *CachedFile) Fd() int {
	return -1
}

// IsComplete returns false on non-Linux platforms.
func (cf *CachedFile) IsComplete() bool {
	return false
}

// LocalPath returns empty string on non-Linux platforms.
func (cf *CachedFile) LocalPath() string {
	return ""
}
