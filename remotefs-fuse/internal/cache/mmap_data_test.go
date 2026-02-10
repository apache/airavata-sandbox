//go:build linux

package cache

import (
	"os"
	"path/filepath"
	"sync"
	"testing"
)

func setupMmapTestCache(t *testing.T, maxSize, blockSize int64) (*MmapDataCache, func()) {
	t.Helper()
	tmpDir, err := os.MkdirTemp("", "mmap_cache_test_*")
	if err != nil {
		t.Fatalf("failed to create temp dir: %v", err)
	}

	cache, err := NewMmapDataCache(tmpDir, maxSize, blockSize)
	if err != nil {
		os.RemoveAll(tmpDir)
		t.Fatalf("failed to create mmap cache: %v", err)
	}

	cleanup := func() {
		cache.Close()
		os.RemoveAll(tmpDir)
	}

	return cache, cleanup
}

func TestMmapDataCache_BasicReadWrite(t *testing.T) {
	cache, cleanup := setupMmapTestCache(t, 1024*1024, 64)
	defer cleanup()

	path := "test.txt"
	data := []byte("Hello, World!")
	mtime := int64(1000)

	// Write data
	cache.WriteWithMtime(path, 0, data, mtime)

	// Read it back
	result, complete := cache.ReadWithMtime(path, 0, int64(len(data)), mtime)
	if !complete {
		t.Fatal("expected cache hit")
	}
	if string(result) != string(data) {
		t.Fatalf("data mismatch: got %q, want %q", result, data)
	}
}

func TestMmapDataCache_MtimeInvalidation(t *testing.T) {
	cache, cleanup := setupMmapTestCache(t, 1024*1024, 64)
	defer cleanup()

	path := "file.txt"
	originalMtime := int64(1000)
	newMtime := int64(2000)

	// Write data with original mtime
	cache.WriteWithMtime(path, 0, []byte("Original content"), originalMtime)

	// Read with same mtime should succeed
	data, complete := cache.ReadWithMtime(path, 0, 16, originalMtime)
	if !complete {
		t.Fatal("expected cache hit with matching mtime")
	}
	if string(data) != "Original content" {
		t.Fatalf("unexpected data: %q", data)
	}

	// Read with different mtime should fail (source file changed)
	_, complete = cache.ReadWithMtime(path, 0, 16, newMtime)
	if complete {
		t.Fatal("expected cache miss when source file mtime changed")
	}

	// After stale detection, the file's cache should be invalidated
	// Even reading without mtime should fail
	_, complete = cache.Read(path, 0, 16)
	if complete {
		t.Fatal("expected cache miss after stale invalidation")
	}
}

func TestMmapDataCache_MtimeTracking(t *testing.T) {
	cache, cleanup := setupMmapTestCache(t, 1024*1024, 64)
	defer cleanup()

	path := "file.txt"
	mtime := int64(12345)

	// Write with mtime
	cache.WriteWithMtime(path, 0, []byte("Hello"), mtime)

	// Check tracked mtime
	if cache.GetMtime(path) != mtime {
		t.Fatalf("expected tracked mtime %d, got %d", mtime, cache.GetMtime(path))
	}

	// IsMtimeStale should return false for same mtime
	if cache.IsMtimeStale(path, mtime) {
		t.Fatal("expected IsMtimeStale to return false for same mtime")
	}

	// IsMtimeStale should return true for different mtime
	if !cache.IsMtimeStale(path, mtime+1) {
		t.Fatal("expected IsMtimeStale to return true for different mtime")
	}
}

func TestMmapDataCache_SourceFileChangesInvalidateCache(t *testing.T) {
	cache, cleanup := setupMmapTestCache(t, 1024*1024, 64)
	defer cleanup()

	path := "important_file.txt"

	// Step 1: Cache file with original mtime
	originalContent := []byte("Original important data")
	originalMtime := int64(1000)
	cache.WriteWithMtime(path, 0, originalContent, originalMtime)

	// Verify cached correctly
	data, hit := cache.ReadWithMtime(path, 0, int64(len(originalContent)), originalMtime)
	if !hit {
		t.Fatal("expected cache hit for original data")
	}
	if string(data) != string(originalContent) {
		t.Fatalf("unexpected data: got %q, want %q", data, originalContent)
	}

	// Step 2: Simulate source file modification (mtime changes)
	newMtime := int64(2000)

	// Step 3: Read with new mtime should fail (stale detection)
	_, hit = cache.ReadWithMtime(path, 0, int64(len(originalContent)), newMtime)
	if hit {
		t.Fatal("expected cache miss when source file changed (stale data)")
	}

	// Step 4: All cached data for this file should be invalidated
	_, hit = cache.Read(path, 0, int64(len(originalContent)))
	if hit {
		t.Fatal("expected cache to be fully invalidated after stale detection")
	}
}

func TestMmapDataCache_LRUEviction(t *testing.T) {
	// Cache that can only hold 2 blocks of 64 bytes each
	cache, cleanup := setupMmapTestCache(t, 128, 64)
	defer cleanup()

	// Write 3 blocks - should evict the first one
	cache.Write("file1.txt", 0, make([]byte, 64))
	cache.Write("file2.txt", 0, make([]byte, 64))
	cache.Write("file3.txt", 0, make([]byte, 64))

	// file1 should be evicted
	_, complete := cache.Read("file1.txt", 0, 64)
	if complete {
		t.Fatal("expected file1 to be evicted")
	}

	// file2 and file3 should still be cached
	_, complete = cache.Read("file2.txt", 0, 64)
	if !complete {
		t.Fatal("expected file2 to be cached")
	}
	_, complete = cache.Read("file3.txt", 0, 64)
	if !complete {
		t.Fatal("expected file3 to be cached")
	}
}

func TestMmapDataCache_LRUAccessOrder(t *testing.T) {
	// Cache that can only hold 2 blocks
	cache, cleanup := setupMmapTestCache(t, 128, 64)
	defer cleanup()

	// Write 2 blocks
	cache.Write("file1.txt", 0, make([]byte, 64))
	cache.Write("file2.txt", 0, make([]byte, 64))

	// Access file1 to make it most recently used
	cache.Read("file1.txt", 0, 64)

	// Write file3 - should evict file2 (LRU)
	cache.Write("file3.txt", 0, make([]byte, 64))

	// file1 should still be cached (was accessed recently)
	_, complete := cache.Read("file1.txt", 0, 64)
	if !complete {
		t.Fatal("expected file1 to be cached (was accessed recently)")
	}

	// file2 should be evicted (LRU)
	_, complete = cache.Read("file2.txt", 0, 64)
	if complete {
		t.Fatal("expected file2 to be evicted (LRU)")
	}
}

func TestMmapDataCache_InvalidateAll(t *testing.T) {
	cache, cleanup := setupMmapTestCache(t, 1024*1024, 64)
	defer cleanup()

	path := "file.txt"
	mtime := int64(12345)

	// Write multiple blocks
	cache.WriteWithMtime(path, 0, make([]byte, 128), mtime)

	// Verify cached
	_, complete := cache.Read(path, 0, 128)
	if !complete {
		t.Fatal("expected cache hit")
	}

	// Invalidate all
	cache.InvalidateAll(path)

	// Should be gone
	_, complete = cache.Read(path, 0, 128)
	if complete {
		t.Fatal("expected cache miss after InvalidateAll")
	}

	// Mtime should be cleared
	if cache.GetMtime(path) != 0 {
		t.Fatal("expected mtime to be cleared after InvalidateAll")
	}
}

func TestMmapDataCache_InvalidateBeyond(t *testing.T) {
	cache, cleanup := setupMmapTestCache(t, 1024*1024, 64)
	defer cleanup()

	path := "file.txt"

	// Write 3 blocks (192 bytes)
	cache.Write(path, 0, make([]byte, 192))

	// Invalidate beyond 64 bytes (should remove blocks 1 and 2)
	cache.InvalidateBeyond(path, 64)

	// First block should still be cached
	_, complete := cache.Read(path, 0, 64)
	if !complete {
		t.Fatal("expected first block to still be cached")
	}

	// Second block should be gone
	_, complete = cache.Read(path, 64, 64)
	if complete {
		t.Fatal("expected second block to be invalidated")
	}
}

func TestMmapDataCache_Clear(t *testing.T) {
	cache, cleanup := setupMmapTestCache(t, 1024*1024, 64)
	defer cleanup()

	// Write some data
	cache.Write("file1.txt", 0, []byte("Hello"))
	cache.Write("file2.txt", 0, []byte("World"))

	// Verify cached
	if cache.BlockCount() == 0 {
		t.Fatal("expected blocks to be cached")
	}

	// Clear
	cache.Clear()

	// Should be empty
	if cache.BlockCount() != 0 {
		t.Fatalf("expected 0 blocks after Clear, got %d", cache.BlockCount())
	}
	if cache.Size() != 0 {
		t.Fatalf("expected 0 size after Clear, got %d", cache.Size())
	}
}

func TestMmapDataCache_FilesAreCreated(t *testing.T) {
	tmpDir, err := os.MkdirTemp("", "mmap_cache_test_*")
	if err != nil {
		t.Fatalf("failed to create temp dir: %v", err)
	}
	defer os.RemoveAll(tmpDir)

	cache, err := NewMmapDataCache(tmpDir, 1024*1024, 64)
	if err != nil {
		t.Fatalf("failed to create mmap cache: %v", err)
	}
	defer cache.Close()

	// Write data
	cache.Write("test.txt", 0, []byte("Hello, World!"))

	// Check that block files were created
	blocksDir := filepath.Join(tmpDir, "blocks")
	entries, err := os.ReadDir(blocksDir)
	if err != nil {
		t.Fatalf("failed to read blocks dir: %v", err)
	}
	if len(entries) == 0 {
		t.Fatal("expected block directories to be created")
	}
}

func TestMmapDataCache_NoTTLExpiration(t *testing.T) {
	cache, cleanup := setupMmapTestCache(t, 1024*1024, 64)
	defer cleanup()

	path := "file.txt"
	data := []byte("This data should never expire by TTL")

	// Write data
	cache.Write(path, 0, data)

	// Cleanup should do nothing (no TTL in mmap cache)
	removed := cache.Cleanup()
	if removed != 0 {
		t.Fatalf("expected Cleanup to remove 0 blocks, removed %d", removed)
	}

	// Data should still be cached
	result, complete := cache.Read(path, 0, int64(len(data)))
	if !complete {
		t.Fatal("expected data to still be cached (no TTL expiration)")
	}
	if string(result) != string(data) {
		t.Fatalf("data mismatch: got %q, want %q", result, data)
	}
}

func TestMmapDataCache_ConcurrentReadWrite(t *testing.T) {
	cache, cleanup := setupMmapTestCache(t, 10*1024*1024, 64)
	defer cleanup()

	const numGoroutines = 10
	const numOperations = 100

	var wg sync.WaitGroup
	wg.Add(numGoroutines * 2)

	// Writers
	for i := 0; i < numGoroutines; i++ {
		go func(id int) {
			defer wg.Done()
			for j := 0; j < numOperations; j++ {
				path := "file" + string(rune('A'+id)) + ".txt"
				data := make([]byte, 64)
				for k := range data {
					data[k] = byte(j)
				}
				cache.WriteWithMtime(path, 0, data, int64(j+1))
			}
		}(i)
	}

	// Readers
	for i := 0; i < numGoroutines; i++ {
		go func(id int) {
			defer wg.Done()
			for j := 0; j < numOperations; j++ {
				path := "file" + string(rune('A'+id)) + ".txt"
				cache.Read(path, 0, 64)
			}
		}(i)
	}

	wg.Wait()
}

func TestMmapDataCache_ConcurrentEviction(t *testing.T) {
	// Small cache that will force eviction
	cache, cleanup := setupMmapTestCache(t, 512, 64)
	defer cleanup()

	const numGoroutines = 10
	const numOperations = 50

	var wg sync.WaitGroup
	wg.Add(numGoroutines)

	for i := 0; i < numGoroutines; i++ {
		go func(id int) {
			defer wg.Done()
			for j := 0; j < numOperations; j++ {
				path := "concurrent_file_" + string(rune('A'+id)) + "_" + string(rune('0'+j%10)) + ".txt"
				data := make([]byte, 64)
				cache.Write(path, 0, data)
			}
		}(i)
	}

	wg.Wait()

	// Cache should not exceed max size
	if cache.Size() > 512 {
		t.Fatalf("cache size %d exceeds max size 512", cache.Size())
	}
}

func TestMmapDataCache_MultipleFilesWithDifferentMtimes(t *testing.T) {
	cache, cleanup := setupMmapTestCache(t, 1024*1024, 64)
	defer cleanup()

	// Cache multiple files with different mtimes
	cache.WriteWithMtime("file1.txt", 0, []byte("Content1"), 1000)
	cache.WriteWithMtime("file2.txt", 0, []byte("Content2"), 2000)
	cache.WriteWithMtime("file3.txt", 0, []byte("Content3"), 3000)

	// All should be readable with correct mtimes
	_, hit := cache.ReadWithMtime("file1.txt", 0, 8, 1000)
	if !hit {
		t.Fatal("expected hit for file1")
	}
	_, hit = cache.ReadWithMtime("file2.txt", 0, 8, 2000)
	if !hit {
		t.Fatal("expected hit for file2")
	}
	_, hit = cache.ReadWithMtime("file3.txt", 0, 8, 3000)
	if !hit {
		t.Fatal("expected hit for file3")
	}

	// Only file2 changes
	_, hit = cache.ReadWithMtime("file2.txt", 0, 8, 2001) // mtime changed
	if hit {
		t.Fatal("expected miss for file2 with changed mtime")
	}

	// file1 and file3 should still be readable
	_, hit = cache.ReadWithMtime("file1.txt", 0, 8, 1000)
	if !hit {
		t.Fatal("expected file1 to still be cached")
	}
	_, hit = cache.ReadWithMtime("file3.txt", 0, 8, 3000)
	if !hit {
		t.Fatal("expected file3 to still be cached")
	}
}

func TestMmapDataCache_BlockAlignment(t *testing.T) {
	cache, cleanup := setupMmapTestCache(t, 1024*1024, 64)
	defer cleanup()

	path := "aligned.txt"

	// Write data that spans multiple blocks
	data := make([]byte, 150) // 2.3 blocks
	for i := range data {
		data[i] = byte(i % 256)
	}
	cache.Write(path, 0, data)

	// Read back and verify
	result, complete := cache.Read(path, 0, 150)
	if !complete {
		t.Fatal("expected complete read")
	}
	for i := range result {
		if result[i] != byte(i%256) {
			t.Fatalf("data mismatch at offset %d: got %d, want %d", i, result[i], i%256)
		}
	}
}

func TestMmapDataCache_Disabled(t *testing.T) {
	// Create disabled cache (empty dir)
	cache, err := NewMmapDataCache("", 1024*1024, 64)
	if err != nil {
		t.Fatalf("failed to create disabled cache: %v", err)
	}
	defer cache.Close()

	if cache.Enabled() {
		t.Fatal("expected cache to be disabled")
	}

	// All operations should be no-ops or return appropriate defaults
	cache.Write("test.txt", 0, []byte("Hello"))
	_, complete := cache.Read("test.txt", 0, 5)
	if complete {
		t.Fatal("expected read to fail on disabled cache")
	}

	if cache.Size() != 0 {
		t.Fatal("expected size 0 for disabled cache")
	}
	if cache.BlockCount() != 0 {
		t.Fatal("expected block count 0 for disabled cache")
	}
}
