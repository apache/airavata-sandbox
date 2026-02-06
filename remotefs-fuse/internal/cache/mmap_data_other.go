//go:build !linux

package cache

import (
	"fmt"
)

// MmapDataCache is a stub for non-Linux platforms.
// Memory-mapped file caching is only supported on Linux where mmap
// performance benefits are most significant.
type MmapDataCache struct {
	enabled bool
}

// mmapBlock is a stub for non-Linux platforms.
type mmapBlock struct{}

// NewMmapDataCache returns a disabled cache on non-Linux platforms.
func NewMmapDataCache(cacheDir string, maxSize, blockSize int64) (*MmapDataCache, error) {
	if cacheDir != "" {
		return nil, fmt.Errorf("mmap cache not supported on this platform (Linux only)")
	}
	return &MmapDataCache{enabled: false}, nil
}

// Enabled returns false on non-Linux platforms.
func (mc *MmapDataCache) Enabled() bool {
	return false
}

// Read returns a cache miss on non-Linux platforms.
func (mc *MmapDataCache) Read(path string, offset, size int64) ([]byte, bool) {
	return nil, false
}

// ReadWithMtime returns a cache miss on non-Linux platforms.
func (mc *MmapDataCache) ReadWithMtime(path string, offset, size, mtime int64) ([]byte, bool) {
	return nil, false
}

// Write is a no-op on non-Linux platforms.
func (mc *MmapDataCache) Write(path string, offset int64, data []byte) {}

// WriteWithMtime is a no-op on non-Linux platforms.
func (mc *MmapDataCache) WriteWithMtime(path string, offset int64, data []byte, mtime int64) {}

// Invalidate is a no-op on non-Linux platforms.
func (mc *MmapDataCache) Invalidate(path string, offset, size int64) {}

// InvalidateAll is a no-op on non-Linux platforms.
func (mc *MmapDataCache) InvalidateAll(path string) {}

// InvalidateBeyond is a no-op on non-Linux platforms.
func (mc *MmapDataCache) InvalidateBeyond(path string, size int64) {}

// Clear is a no-op on non-Linux platforms.
func (mc *MmapDataCache) Clear() {}

// SetMtime is a no-op on non-Linux platforms.
func (mc *MmapDataCache) SetMtime(path string, mtime int64) {}

// GetMtime returns 0 on non-Linux platforms.
func (mc *MmapDataCache) GetMtime(path string) int64 {
	return 0
}

// IsMtimeStale returns false on non-Linux platforms.
func (mc *MmapDataCache) IsMtimeStale(path string, mtime int64) bool {
	return false
}

// Size returns 0 on non-Linux platforms.
func (mc *MmapDataCache) Size() int64 {
	return 0
}

// BlockCount returns 0 on non-Linux platforms.
func (mc *MmapDataCache) BlockCount() int {
	return 0
}

// Cleanup is a no-op on non-Linux platforms.
func (mc *MmapDataCache) Cleanup() int {
	return 0
}

// Close is a no-op on non-Linux platforms.
func (mc *MmapDataCache) Close() {}
