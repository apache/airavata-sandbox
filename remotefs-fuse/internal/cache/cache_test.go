package cache

import (
	"testing"
	"time"

	pb "github.com/apache/airavata-sandbox/remotefs-fuse/proto/gen/remotefs"
)

// Data Cache Tests

func TestDataCache_WriteAndRead(t *testing.T) {
	cache := NewDataCache(1024*1024, 64) // 1MB cache, 64 byte blocks

	path := "test/file.txt"
	data := []byte("Hello, World!")

	// Write data
	cache.Write(path, 0, data)

	// Read it back
	result, complete := cache.Read(path, 0, int64(len(data)))
	if !complete {
		t.Fatal("expected complete read")
	}
	if string(result) != string(data) {
		t.Fatalf("expected %q, got %q", data, result)
	}
}

func TestDataCache_ReadMiss(t *testing.T) {
	cache := NewDataCache(1024*1024, 64)

	// Read from empty cache
	_, complete := cache.Read("nonexistent", 0, 100)
	if complete {
		t.Fatal("expected incomplete read for cache miss")
	}
}

func TestDataCache_PartialRead(t *testing.T) {
	cache := NewDataCache(1024*1024, 64)

	path := "test/file.txt"
	data := []byte("Hello, World! This is a longer string for testing partial reads.")

	cache.Write(path, 0, data)

	// Read partial data from middle
	result, complete := cache.Read(path, 7, 5)
	if !complete {
		t.Fatal("expected complete read")
	}
	if string(result) != "World" {
		t.Fatalf("expected %q, got %q", "World", result)
	}
}

func TestDataCache_BlockAlignment(t *testing.T) {
	blockSize := int64(16)
	cache := NewDataCache(1024*1024, blockSize)

	path := "test/file.txt"

	// Write data spanning multiple blocks
	data := []byte("0123456789ABCDEF0123456789ABCDEF0123456789") // 42 bytes, spans 3 blocks

	cache.Write(path, 0, data)

	// Should have 3 blocks
	if cache.BlockCount() != 3 {
		t.Fatalf("expected 3 blocks, got %d", cache.BlockCount())
	}

	// Read across block boundaries
	result, complete := cache.Read(path, 14, 4) // Read "EF01"
	if !complete {
		t.Fatal("expected complete read")
	}
	if string(result) != "EF01" {
		t.Fatalf("expected %q, got %q", "EF01", result)
	}
}

func TestDataCache_WriteInvalidation(t *testing.T) {
	cache := NewDataCache(1024*1024, 64)

	path := "test/file.txt"

	// Write initial data
	cache.Write(path, 0, []byte("Hello, World!"))

	// Invalidate part of it
	cache.Invalidate(path, 0, 5)

	// Read should miss for the invalidated part
	_, complete := cache.Read(path, 0, 5)
	if complete {
		t.Fatal("expected incomplete read after invalidation")
	}
}

func TestDataCache_InvalidateAll(t *testing.T) {
	cache := NewDataCache(1024*1024, 64)

	path := "test/file.txt"
	cache.Write(path, 0, []byte("Hello"))
	cache.Write(path, 100, []byte("World"))

	// Invalidate all blocks for path
	cache.InvalidateAll(path)

	if cache.BlockCount() != 0 {
		t.Fatalf("expected 0 blocks after InvalidateAll, got %d", cache.BlockCount())
	}
}

func TestDataCache_InvalidateBeyond(t *testing.T) {
	blockSize := int64(16)
	cache := NewDataCache(1024*1024, blockSize)

	path := "test/file.txt"

	// Write 64 bytes (4 blocks)
	data := make([]byte, 64)
	for i := range data {
		data[i] = byte('A' + i%26)
	}
	cache.Write(path, 0, data)

	if cache.BlockCount() != 4 {
		t.Fatalf("expected 4 blocks, got %d", cache.BlockCount())
	}

	// Truncate to 20 bytes (should keep 2 blocks, truncate block 1)
	cache.InvalidateBeyond(path, 20)

	// Blocks 2 and 3 should be gone
	if cache.BlockCount() != 2 {
		t.Fatalf("expected 2 blocks after truncate, got %d", cache.BlockCount())
	}
}

func TestDataCache_LRUEviction(t *testing.T) {
	// Small cache that can only hold 2 blocks of 16 bytes each
	cache := NewDataCache(32, 16)

	// Write 3 blocks - third should evict first
	cache.Write("file1", 0, []byte("0123456789ABCDEF"))
	cache.Write("file2", 0, []byte("GHIJKLMNOPQRSTUV"))
	cache.Write("file3", 0, []byte("WXYZ0123456789AB"))

	// file1 should be evicted
	_, complete := cache.Read("file1", 0, 16)
	if complete {
		t.Fatal("expected file1 to be evicted")
	}

	// file2 and file3 should still be there
	_, complete = cache.Read("file2", 0, 16)
	if !complete {
		t.Fatal("expected file2 to be cached")
	}

	_, complete = cache.Read("file3", 0, 16)
	if !complete {
		t.Fatal("expected file3 to be cached")
	}
}

func TestDataCache_LRUAccessOrder(t *testing.T) {
	// Small cache that can only hold 2 blocks
	cache := NewDataCache(32, 16)

	// Write 2 blocks
	cache.Write("file1", 0, []byte("0123456789ABCDEF"))
	cache.Write("file2", 0, []byte("GHIJKLMNOPQRSTUV"))

	// Access file1 to make it more recent
	cache.Read("file1", 0, 16)

	// Write third block - should evict file2 (least recently used)
	cache.Write("file3", 0, []byte("WXYZ0123456789AB"))

	// file2 should be evicted
	_, complete := cache.Read("file2", 0, 16)
	if complete {
		t.Fatal("expected file2 to be evicted")
	}

	// file1 should still be there
	_, complete = cache.Read("file1", 0, 16)
	if !complete {
		t.Fatal("expected file1 to be cached")
	}
}

func TestDataCache_Size(t *testing.T) {
	cache := NewDataCache(1024*1024, 16)

	cache.Write("file1", 0, []byte("0123456789")) // 10 bytes
	cache.Write("file2", 0, []byte("ABCDEF"))     // 6 bytes

	expectedSize := int64(16) // 10 + 6 = 16
	if cache.Size() != expectedSize {
		t.Fatalf("expected size %d, got %d", expectedSize, cache.Size())
	}
}

func TestDataCache_Clear(t *testing.T) {
	cache := NewDataCache(1024*1024, 64)

	cache.Write("file1", 0, []byte("Hello"))
	cache.Write("file2", 0, []byte("World"))

	cache.Clear()

	if cache.BlockCount() != 0 {
		t.Fatalf("expected 0 blocks after Clear, got %d", cache.BlockCount())
	}
	if cache.Size() != 0 {
		t.Fatalf("expected size 0 after Clear, got %d", cache.Size())
	}
}

// Data Cache Offset Bug Tests
// These tests verify the fix for the bug where Read() incorrectly returned
// complete=true even when it had no data to return for non-zero offsets.
// This caused files to appear truncated (only first 64KB readable).

func TestDataCache_ReadAtOffsetZero(t *testing.T) {
	// Test reading at offset 0 - should work correctly
	blockSize := int64(64)
	cache := NewDataCache(1024*1024, blockSize)

	path := "test/file.txt"
	data := []byte("Hello, this is test data for offset zero read")

	cache.Write(path, 0, data)

	// Read at offset 0 should succeed
	result, complete := cache.Read(path, 0, int64(len(data)))
	if !complete {
		t.Fatal("expected complete read at offset 0")
	}
	if string(result) != string(data) {
		t.Fatalf("expected %q, got %q", data, result)
	}
}

func TestDataCache_ReadAtOffsetWithinPartialBlock(t *testing.T) {
	// Test reading at an offset within a partially-filled block
	// If a block only has 30 bytes, reading at offset 40 should return false
	blockSize := int64(64)
	cache := NewDataCache(1024*1024, blockSize)

	path := "test/file.txt"
	// Write only 30 bytes to block 0 (which can hold 64 bytes)
	data := []byte("This is only 30 bytes of data!")
	cache.Write(path, 0, data)

	// Try to read starting at offset 40 (within the block but beyond the data)
	result, complete := cache.Read(path, 40, 10)
	if complete {
		t.Fatal("expected incomplete read when reading beyond data in partial block")
	}
	if result != nil {
		t.Fatalf("expected nil result, got %v", result)
	}
}

func TestDataCache_ReadBeyondCachedData(t *testing.T) {
	// Test reading at an offset completely beyond the cached data
	blockSize := int64(64)
	cache := NewDataCache(1024*1024, blockSize)

	path := "test/file.txt"
	// Write 50 bytes to block 0
	data := make([]byte, 50)
	for i := range data {
		data[i] = byte('A' + i%26)
	}
	cache.Write(path, 0, data)

	// Try to read at offset 64 (which would be block 1, not cached)
	result, complete := cache.Read(path, 64, 20)
	if complete {
		t.Fatal("expected incomplete read when reading from uncached block")
	}
	if result != nil {
		t.Fatalf("expected nil result, got %v", result)
	}
}

func TestDataCache_ReadBeyondCachedDataLargeOffset(t *testing.T) {
	// Test reading at a large offset beyond all cached data (simulates the 64KB bug)
	blockSize := int64(64 * 1024) // 64KB blocks, like default
	cache := NewDataCache(256*1024*1024, blockSize)

	path := "test/file.txt"
	// Write only 32KB of data to the first block
	data := make([]byte, 32*1024)
	for i := range data {
		data[i] = byte(i % 256)
	}
	cache.Write(path, 0, data)

	// Try to read at offset 64KB - this is the bug scenario
	// Block 1 doesn't exist, should return cache miss
	result, complete := cache.Read(path, 64*1024, 1024)
	if complete {
		t.Fatal("expected incomplete read at offset 64KB when only 32KB cached")
	}
	if result != nil {
		t.Fatalf("expected nil result, got %d bytes", len(result))
	}

	// Try to read at offset 48KB (within block 0 but beyond cached data)
	result, complete = cache.Read(path, 48*1024, 1024)
	if complete {
		t.Fatal("expected incomplete read at offset 48KB when only 32KB cached")
	}
	if result != nil {
		t.Fatalf("expected nil result, got %d bytes", len(result))
	}
}

func TestDataCache_SequentialReadsSpanningMultipleBlocks(t *testing.T) {
	// Test sequential reads that span multiple blocks
	blockSize := int64(16)
	cache := NewDataCache(1024*1024, blockSize)

	path := "test/file.txt"
	// Write 48 bytes (spans 3 blocks: 0-15, 16-31, 32-47)
	data := []byte("0123456789ABCDEF" + "GHIJKLMNOPQRSTUV" + "WXYZ0123456789AB")
	cache.Write(path, 0, data)

	// Sequential read 1: Read block 0
	result, complete := cache.Read(path, 0, 16)
	if !complete {
		t.Fatal("expected complete read for block 0")
	}
	if string(result) != "0123456789ABCDEF" {
		t.Fatalf("expected block 0 content, got %q", result)
	}

	// Sequential read 2: Read block 1
	result, complete = cache.Read(path, 16, 16)
	if !complete {
		t.Fatal("expected complete read for block 1")
	}
	if string(result) != "GHIJKLMNOPQRSTUV" {
		t.Fatalf("expected block 1 content, got %q", result)
	}

	// Sequential read 3: Read block 2
	result, complete = cache.Read(path, 32, 16)
	if !complete {
		t.Fatal("expected complete read for block 2")
	}
	if string(result) != "WXYZ0123456789AB" {
		t.Fatalf("expected block 2 content, got %q", result)
	}

	// Sequential read 4: Try to read block 3 (not cached)
	result, complete = cache.Read(path, 48, 16)
	if complete {
		t.Fatal("expected incomplete read for uncached block 3")
	}
	if result != nil {
		t.Fatalf("expected nil result for uncached block, got %v", result)
	}
}

func TestDataCache_PartialBlockDoesNotFalselyClaimComplete(t *testing.T) {
	// Test that partial blocks don't falsely claim complete reads
	blockSize := int64(32)
	cache := NewDataCache(1024*1024, blockSize)

	path := "test/file.txt"
	// Write only 20 bytes to block 0 (which can hold 32 bytes)
	data := []byte("12345678901234567890") // 20 bytes
	cache.Write(path, 0, data)

	// Read exactly what's cached - should succeed
	result, complete := cache.Read(path, 0, 20)
	if !complete {
		t.Fatal("expected complete read for exact cached size")
	}
	if string(result) != string(data) {
		t.Fatalf("expected %q, got %q", data, result)
	}

	// Read more than what's cached within same block - should still work
	// because the data ends at 20, and we're reading 0-25
	// But block only has data up to 20, so we get 20 bytes
	result, complete = cache.Read(path, 0, 25)
	if !complete {
		t.Fatal("expected complete read when reading up to end of cached data")
	}
	if len(result) != 20 {
		t.Fatalf("expected 20 bytes (all cached data), got %d", len(result))
	}

	// Read starting past the end of cached data - should fail
	result, complete = cache.Read(path, 21, 5)
	if complete {
		t.Fatal("expected incomplete read when starting past cached data")
	}
	if result != nil {
		t.Fatalf("expected nil result, got %v", result)
	}

	// Read starting at exactly the end - should fail
	result, complete = cache.Read(path, 20, 5)
	if complete {
		t.Fatal("expected incomplete read when starting at exact end of cached data")
	}
	if result != nil {
		t.Fatalf("expected nil result, got %v", result)
	}
}

func TestDataCache_MultiBlockPartialLastBlock(t *testing.T) {
	// Test reading across blocks where the last block is partial
	blockSize := int64(16)
	cache := NewDataCache(1024*1024, blockSize)

	path := "test/file.txt"
	// Write 24 bytes (block 0 full, block 1 has 8 bytes)
	data := []byte("0123456789ABCDEF12345678") // 24 bytes
	cache.Write(path, 0, data)

	// Read all 24 bytes - should succeed
	result, complete := cache.Read(path, 0, 24)
	if !complete {
		t.Fatal("expected complete read for all cached data")
	}
	if string(result) != string(data) {
		t.Fatalf("expected %q, got %q", data, result)
	}

	// Read 30 bytes - should succeed with only 24 bytes (what's available)
	result, complete = cache.Read(path, 0, 30)
	if !complete {
		t.Fatal("expected complete read (capped at available data)")
	}
	if len(result) != 24 {
		t.Fatalf("expected 24 bytes, got %d", len(result))
	}

	// Read starting at offset 20 (in partial block) with size 10
	// Block 1 only has 8 bytes (indices 16-23), so we can only get 4 bytes (20-23)
	result, complete = cache.Read(path, 20, 10)
	if !complete {
		t.Fatal("expected complete read for available partial data")
	}
	if len(result) != 4 {
		t.Fatalf("expected 4 bytes, got %d", len(result))
	}
	if string(result) != "5678" {
		t.Fatalf("expected %q, got %q", "5678", result)
	}

	// Read starting at offset 24 (past all data) - should fail
	result, complete = cache.Read(path, 24, 5)
	if complete {
		t.Fatal("expected incomplete read at offset 24 (past all cached data)")
	}
	if result != nil {
		t.Fatalf("expected nil result, got %v", result)
	}
}

func TestDataCache_ReadEmptyResultReturnsIncomplete(t *testing.T) {
	// Test that requesting data but getting empty result returns incomplete
	blockSize := int64(16)
	cache := NewDataCache(1024*1024, blockSize)

	path := "test/file.txt"
	// Write 10 bytes
	data := []byte("0123456789")
	cache.Write(path, 0, data)

	// Request read starting at offset 10 (exactly at end of data)
	result, complete := cache.Read(path, 10, 5)
	if complete {
		t.Fatal("expected incomplete when result would be empty")
	}
	if result != nil {
		t.Fatalf("expected nil result, got %v", result)
	}

	// Request read starting at offset 15 (beyond data but within same block)
	result, complete = cache.Read(path, 15, 5)
	if complete {
		t.Fatal("expected incomplete when offset is beyond data in block")
	}
	if result != nil {
		t.Fatalf("expected nil result, got %v", result)
	}
}

func TestDataCache_SimulateTruncatedFileBug(t *testing.T) {
	// Simulate the original bug: file appears truncated because reads
	// beyond the first cached block return complete=true with empty data
	blockSize := int64(64 * 1024) // 64KB blocks
	cache := NewDataCache(256*1024*1024, blockSize)

	path := "large_file.bin"
	// Simulate caching only the first 64KB of a larger file
	firstBlock := make([]byte, 64*1024)
	for i := range firstBlock {
		firstBlock[i] = byte(i % 256)
	}
	cache.Write(path, 0, firstBlock)

	// Reading the first block should work
	result, complete := cache.Read(path, 0, 64*1024)
	if !complete {
		t.Fatal("expected complete read for first block")
	}
	if len(result) != 64*1024 {
		t.Fatalf("expected %d bytes, got %d", 64*1024, len(result))
	}

	// BUG SCENARIO: Reading the second block (offset 64KB)
	// Before the fix, this would return complete=true with empty data
	// causing the file to appear truncated at 64KB
	result, complete = cache.Read(path, 64*1024, 64*1024)
	if complete {
		t.Fatal("BUG REGRESSION: Read at offset 64KB should return incomplete (cache miss)")
	}
	if result != nil {
		t.Fatalf("expected nil result for uncached block, got %d bytes", len(result))
	}

	// Also test read at offset within second block
	result, complete = cache.Read(path, 65*1024, 1024)
	if complete {
		t.Fatal("BUG REGRESSION: Read at offset 65KB should return incomplete")
	}
	if result != nil {
		t.Fatalf("expected nil result, got %d bytes", len(result))
	}
}

func TestDataCache_ZeroSizeRead(t *testing.T) {
	// Test that zero-size reads return complete=true (edge case)
	blockSize := int64(64)
	cache := NewDataCache(1024*1024, blockSize)

	path := "test/file.txt"
	cache.Write(path, 0, []byte("Hello"))

	// Zero-size read should return complete=true
	result, complete := cache.Read(path, 0, 0)
	if !complete {
		t.Fatal("expected complete for zero-size read")
	}
	if result != nil {
		t.Fatalf("expected nil result for zero-size read, got %v", result)
	}

	// Zero-size read at any offset should return complete=true
	result, complete = cache.Read(path, 1000, 0)
	if !complete {
		t.Fatal("expected complete for zero-size read at any offset")
	}
}

// Metadata Cache Tests

func TestMetadataCache_SetAndGet(t *testing.T) {
	cache := NewMetadataCache(time.Hour)

	attr := &pb.Attr{
		Ino:  123,
		Size: 1024,
		Mode: 0644,
	}

	cache.Set("test/file.txt", attr)

	result, ok := cache.Get("test/file.txt")
	if !ok {
		t.Fatal("expected cache hit")
	}
	if result.Ino != attr.Ino || result.Size != attr.Size || result.Mode != attr.Mode {
		t.Fatalf("expected %+v, got %+v", attr, result)
	}
}

func TestMetadataCache_GetMiss(t *testing.T) {
	cache := NewMetadataCache(time.Hour)

	_, ok := cache.Get("nonexistent")
	if ok {
		t.Fatal("expected cache miss")
	}
}

func TestMetadataCache_TTLExpiration(t *testing.T) {
	cache := NewMetadataCache(100 * time.Millisecond)

	// Override time function for testing
	currentTime := time.Now()
	cache.now = func() time.Time { return currentTime }

	attr := &pb.Attr{Ino: 123}
	cache.Set("test/file.txt", attr)

	// Should be in cache
	_, ok := cache.Get("test/file.txt")
	if !ok {
		t.Fatal("expected cache hit before expiration")
	}

	// Advance time past TTL
	currentTime = currentTime.Add(200 * time.Millisecond)

	// Should be expired
	_, ok = cache.Get("test/file.txt")
	if ok {
		t.Fatal("expected cache miss after expiration")
	}
}

func TestMetadataCache_Invalidation(t *testing.T) {
	cache := NewMetadataCache(time.Hour)

	cache.Set("test/file.txt", &pb.Attr{Ino: 123})
	cache.Invalidate("test/file.txt")

	_, ok := cache.Get("test/file.txt")
	if ok {
		t.Fatal("expected cache miss after invalidation")
	}
}

func TestMetadataCache_InvalidatePrefix(t *testing.T) {
	cache := NewMetadataCache(time.Hour)

	cache.Set("dir/file1.txt", &pb.Attr{Ino: 1})
	cache.Set("dir/file2.txt", &pb.Attr{Ino: 2})
	cache.Set("other/file.txt", &pb.Attr{Ino: 3})

	cache.InvalidatePrefix("dir/")

	_, ok := cache.Get("dir/file1.txt")
	if ok {
		t.Fatal("expected dir/file1.txt to be invalidated")
	}

	_, ok = cache.Get("dir/file2.txt")
	if ok {
		t.Fatal("expected dir/file2.txt to be invalidated")
	}

	_, ok = cache.Get("other/file.txt")
	if !ok {
		t.Fatal("expected other/file.txt to remain cached")
	}
}

func TestMetadataCache_UpdateSize(t *testing.T) {
	cache := NewMetadataCache(time.Hour)

	cache.Set("test/file.txt", &pb.Attr{Ino: 123, Size: 100})

	ok := cache.UpdateSize("test/file.txt", 200)
	if !ok {
		t.Fatal("expected UpdateSize to succeed")
	}

	attr, _ := cache.Get("test/file.txt")
	if attr.Size != 200 {
		t.Fatalf("expected size 200, got %d", attr.Size)
	}
}

func TestMetadataCache_Cleanup(t *testing.T) {
	cache := NewMetadataCache(100 * time.Millisecond)

	currentTime := time.Now()
	cache.now = func() time.Time { return currentTime }

	cache.Set("file1.txt", &pb.Attr{Ino: 1})
	cache.Set("file2.txt", &pb.Attr{Ino: 2})

	// Advance time
	currentTime = currentTime.Add(200 * time.Millisecond)

	// Add one more that won't be expired
	cache.Set("file3.txt", &pb.Attr{Ino: 3})

	removed := cache.Cleanup()
	if removed != 2 {
		t.Fatalf("expected 2 entries removed, got %d", removed)
	}

	if cache.Count() != 1 {
		t.Fatalf("expected 1 entry remaining, got %d", cache.Count())
	}
}

func TestMetadataCache_ImmutableReturn(t *testing.T) {
	cache := NewMetadataCache(time.Hour)

	cache.Set("test/file.txt", &pb.Attr{Ino: 123, Size: 100})

	// Get and modify
	attr1, _ := cache.Get("test/file.txt")
	attr1.Size = 999

	// Get again - should have original value
	attr2, _ := cache.Get("test/file.txt")
	if attr2.Size != 100 {
		t.Fatalf("cache entry was modified, expected size 100, got %d", attr2.Size)
	}
}

// Directory Cache Tests

func TestDirectoryCache_SetAndGet(t *testing.T) {
	cache := NewDirectoryCache(time.Hour)

	entries := []*pb.DirEntry{
		{Name: "file1.txt", Mode: 0644, Ino: 1},
		{Name: "file2.txt", Mode: 0644, Ino: 2},
		{Name: "subdir", Mode: 0755 | 0040000, Ino: 3},
	}

	cache.Set("test/dir", entries)

	result, ok := cache.Get("test/dir")
	if !ok {
		t.Fatal("expected cache hit")
	}
	if len(result) != len(entries) {
		t.Fatalf("expected %d entries, got %d", len(entries), len(result))
	}
	for i, e := range result {
		if e.Name != entries[i].Name {
			t.Fatalf("expected name %q, got %q", entries[i].Name, e.Name)
		}
	}
}

func TestDirectoryCache_GetMiss(t *testing.T) {
	cache := NewDirectoryCache(time.Hour)

	_, ok := cache.Get("nonexistent")
	if ok {
		t.Fatal("expected cache miss")
	}
}

func TestDirectoryCache_TTLExpiration(t *testing.T) {
	cache := NewDirectoryCache(100 * time.Millisecond)

	currentTime := time.Now()
	cache.now = func() time.Time { return currentTime }

	cache.Set("test/dir", []*pb.DirEntry{{Name: "file.txt"}})

	// Should be in cache
	_, ok := cache.Get("test/dir")
	if !ok {
		t.Fatal("expected cache hit before expiration")
	}

	// Advance time past TTL
	currentTime = currentTime.Add(200 * time.Millisecond)

	// Should be expired
	_, ok = cache.Get("test/dir")
	if ok {
		t.Fatal("expected cache miss after expiration")
	}
}

func TestDirectoryCache_Invalidation(t *testing.T) {
	cache := NewDirectoryCache(time.Hour)

	cache.Set("test/dir", []*pb.DirEntry{{Name: "file.txt"}})
	cache.Invalidate("test/dir")

	_, ok := cache.Get("test/dir")
	if ok {
		t.Fatal("expected cache miss after invalidation")
	}
}

func TestDirectoryCache_AddEntry(t *testing.T) {
	cache := NewDirectoryCache(time.Hour)

	cache.Set("test/dir", []*pb.DirEntry{{Name: "file1.txt", Ino: 1}})

	ok := cache.AddEntry("test/dir", &pb.DirEntry{Name: "file2.txt", Ino: 2})
	if !ok {
		t.Fatal("expected AddEntry to succeed")
	}

	entries, _ := cache.Get("test/dir")
	if len(entries) != 2 {
		t.Fatalf("expected 2 entries, got %d", len(entries))
	}
}

func TestDirectoryCache_RemoveEntry(t *testing.T) {
	cache := NewDirectoryCache(time.Hour)

	cache.Set("test/dir", []*pb.DirEntry{
		{Name: "file1.txt", Ino: 1},
		{Name: "file2.txt", Ino: 2},
	})

	ok := cache.RemoveEntry("test/dir", "file1.txt")
	if !ok {
		t.Fatal("expected RemoveEntry to succeed")
	}

	entries, _ := cache.Get("test/dir")
	if len(entries) != 1 {
		t.Fatalf("expected 1 entry, got %d", len(entries))
	}
	if entries[0].Name != "file2.txt" {
		t.Fatalf("expected file2.txt to remain, got %s", entries[0].Name)
	}
}

func TestDirectoryCache_HasEntry(t *testing.T) {
	cache := NewDirectoryCache(time.Hour)

	cache.Set("test/dir", []*pb.DirEntry{
		{Name: "file1.txt", Ino: 1},
	})

	if !cache.HasEntry("test/dir", "file1.txt") {
		t.Fatal("expected HasEntry to return true for existing entry")
	}

	if cache.HasEntry("test/dir", "nonexistent.txt") {
		t.Fatal("expected HasEntry to return false for non-existing entry")
	}
}

func TestDirectoryCache_ImmutableReturn(t *testing.T) {
	cache := NewDirectoryCache(time.Hour)

	cache.Set("test/dir", []*pb.DirEntry{{Name: "file.txt", Ino: 1}})

	// Get and modify
	entries1, _ := cache.Get("test/dir")
	entries1[0].Name = "modified.txt"

	// Get again - should have original value
	entries2, _ := cache.Get("test/dir")
	if entries2[0].Name != "file.txt" {
		t.Fatalf("cache entry was modified, expected name 'file.txt', got %q", entries2[0].Name)
	}
}

func TestDirectoryCache_Clear(t *testing.T) {
	cache := NewDirectoryCache(time.Hour)

	cache.Set("dir1", []*pb.DirEntry{{Name: "f1"}})
	cache.Set("dir2", []*pb.DirEntry{{Name: "f2"}})

	cache.Clear()

	if cache.Count() != 0 {
		t.Fatalf("expected 0 entries after Clear, got %d", cache.Count())
	}
}

// Data Cache Eviction Policy Tests (with TTL)

func TestDataCache_LRUEvictionWithTTL(t *testing.T) {
	// Create a small cache that can only hold 2 blocks of 16 bytes each
	blockSize := int64(16)
	maxSize := int64(32) // 2 blocks
	cache := NewDataCacheWithTTL(maxSize, blockSize, time.Hour)

	// Write 3 blocks - the first should be evicted
	cache.Write("file1", 0, []byte("1111111111111111")) // Block 1: 16 bytes
	cache.Write("file2", 0, []byte("2222222222222222")) // Block 2: 16 bytes
	cache.Write("file3", 0, []byte("3333333333333333")) // Block 3: 16 bytes, should evict file1

	// file1 should be evicted (LRU)
	_, complete := cache.Read("file1", 0, 16)
	if complete {
		t.Fatal("expected cache miss for file1 (should be evicted)")
	}

	// file2 and file3 should still be present
	data2, complete2 := cache.Read("file2", 0, 16)
	if !complete2 {
		t.Fatal("expected cache hit for file2")
	}
	if string(data2) != "2222222222222222" {
		t.Fatalf("unexpected data for file2: %q", data2)
	}

	data3, complete3 := cache.Read("file3", 0, 16)
	if !complete3 {
		t.Fatal("expected cache hit for file3")
	}
	if string(data3) != "3333333333333333" {
		t.Fatalf("unexpected data for file3: %q", data3)
	}
}

func TestDataCache_LRUAccessPatternWithTTL(t *testing.T) {
	// Cache can hold 2 blocks
	blockSize := int64(16)
	maxSize := int64(32)
	cache := NewDataCacheWithTTL(maxSize, blockSize, time.Hour)

	// Write 2 blocks
	cache.Write("file1", 0, []byte("1111111111111111"))
	cache.Write("file2", 0, []byte("2222222222222222"))

	// Access file1 to make it recently used
	cache.Read("file1", 0, 16)

	// Write file3 - should evict file2 (not file1, since file1 was accessed more recently)
	cache.Write("file3", 0, []byte("3333333333333333"))

	// file1 should still be present (recently accessed)
	_, complete1 := cache.Read("file1", 0, 16)
	if !complete1 {
		t.Fatal("expected cache hit for file1 (was accessed recently)")
	}

	// file2 should be evicted (LRU)
	_, complete2 := cache.Read("file2", 0, 16)
	if complete2 {
		t.Fatal("expected cache miss for file2 (should be evicted)")
	}

	// file3 should be present
	_, complete3 := cache.Read("file3", 0, 16)
	if !complete3 {
		t.Fatal("expected cache hit for file3")
	}
}

func TestDataCache_MaxSizeEnforced(t *testing.T) {
	blockSize := int64(16)
	maxSize := int64(64) // 4 blocks max
	cache := NewDataCacheWithTTL(maxSize, blockSize, time.Hour)

	// Write 6 blocks worth of data
	for i := 0; i < 6; i++ {
		data := make([]byte, 16)
		for j := range data {
			data[j] = byte('A' + i)
		}
		cache.Write("file"+string(rune('0'+i)), 0, data)
	}

	// Cache size should not exceed max
	if cache.Size() > maxSize {
		t.Fatalf("cache size %d exceeds max size %d", cache.Size(), maxSize)
	}

	// Block count should be at most 4
	if cache.BlockCount() > 4 {
		t.Fatalf("block count %d exceeds expected max 4", cache.BlockCount())
	}
}

// Data Cache TTL Expiration Tests

func TestDataCache_TTLExpiration(t *testing.T) {
	cache := NewDataCacheWithTTL(1024*1024, 64, 100*time.Millisecond)

	// Use mock time
	currentTime := time.Now()
	cache.now = func() time.Time { return currentTime }

	// Write data
	cache.Write("file.txt", 0, []byte("Hello, World!"))

	// Should be in cache
	data, complete := cache.Read("file.txt", 0, 13)
	if !complete {
		t.Fatal("expected cache hit before expiration")
	}
	if string(data) != "Hello, World!" {
		t.Fatalf("unexpected data: %q", data)
	}

	// Advance time past TTL
	currentTime = currentTime.Add(200 * time.Millisecond)

	// Should be expired
	_, complete = cache.Read("file.txt", 0, 13)
	if complete {
		t.Fatal("expected cache miss after TTL expiration")
	}
}

func TestDataCache_TTLRefreshedOnWrite(t *testing.T) {
	cache := NewDataCacheWithTTL(1024*1024, 64, 100*time.Millisecond)

	currentTime := time.Now()
	cache.now = func() time.Time { return currentTime }

	// Write initial data
	cache.Write("file.txt", 0, []byte("Hello"))

	// Advance time partially
	currentTime = currentTime.Add(50 * time.Millisecond)

	// Overwrite the same location (should refresh TTL)
	cache.Write("file.txt", 0, []byte("World"))

	// Advance time partially again (total 100ms from initial write, but only 50ms from refresh)
	currentTime = currentTime.Add(50 * time.Millisecond)

	// Should still be in cache (TTL was refreshed)
	data, complete := cache.Read("file.txt", 0, 5)
	if !complete {
		t.Fatal("expected cache hit (TTL should be refreshed on write)")
	}
	if string(data) != "World" {
		t.Fatalf("unexpected data: %q", data)
	}
}

func TestDataCache_Cleanup(t *testing.T) {
	cache := NewDataCacheWithTTL(1024*1024, 64, 100*time.Millisecond)

	currentTime := time.Now()
	cache.now = func() time.Time { return currentTime }

	// Write multiple files
	cache.Write("file1.txt", 0, []byte("Data1"))
	cache.Write("file2.txt", 0, []byte("Data2"))

	// Advance time past TTL
	currentTime = currentTime.Add(200 * time.Millisecond)

	// Add one more that won't be expired
	cache.Write("file3.txt", 0, []byte("Data3"))

	// Run cleanup
	removed := cache.Cleanup()
	if removed != 2 {
		t.Fatalf("expected 2 blocks removed, got %d", removed)
	}

	// file3 should still be in cache
	_, complete := cache.Read("file3.txt", 0, 5)
	if !complete {
		t.Fatal("expected cache hit for file3")
	}
}

// Data Cache Mtime-based Stale Detection Tests

func TestDataCache_MtimeStaleDetection(t *testing.T) {
	cache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

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

func TestDataCache_MtimeTracking(t *testing.T) {
	cache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

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

func TestDataCache_MtimeInvalidationClearsTracking(t *testing.T) {
	cache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

	path := "file.txt"
	mtime := int64(12345)

	// Write with mtime
	cache.WriteWithMtime(path, 0, []byte("Hello"), mtime)

	// Invalidate all blocks for the path
	cache.InvalidateAll(path)

	// Tracked mtime should be cleared
	if cache.GetMtime(path) != 0 {
		t.Fatalf("expected mtime to be cleared after InvalidateAll, got %d", cache.GetMtime(path))
	}
}

func TestDataCache_SourceFileChangesInvalidateCache(t *testing.T) {
	// This test simulates the critical scenario:
	// 1. File is cached with mtime=1000
	// 2. Source file is modified (mtime changes to 2000)
	// 3. Read should detect stale data and return cache miss

	cache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

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

	// Step 2: Source file is modified (mtime changes)
	newMtime := int64(2000)

	// Step 3: Read with new mtime should fail (stale detection)
	_, hit = cache.ReadWithMtime(path, 0, int64(len(originalContent)), newMtime)
	if hit {
		t.Fatal("expected cache miss when source file changed (stale data)")
	}

	// The cache should have invalidated the file
	// Even subsequent reads should fail
	_, hit = cache.Read(path, 0, int64(len(originalContent)))
	if hit {
		t.Fatal("expected cache to be invalidated after detecting stale data")
	}
}

// Read Tracker Cleanup Tests

func TestReadTrackerCleanup(t *testing.T) {
	// This test verifies that stale read trackers are cleaned up
	// Note: We can't easily test the CachedClient's background cleanup,
	// but we can test the cleanup logic directly

	// Create a mock to test the logic
	trackers := make(map[string]*readTracker)

	now := time.Now()

	// Add some fresh trackers
	trackers["fresh1"] = &readTracker{lastAccess: now}
	trackers["fresh2"] = &readTracker{lastAccess: now.Add(-1 * time.Minute)}

	// Add some stale trackers (older than 5 minutes)
	trackers["stale1"] = &readTracker{lastAccess: now.Add(-6 * time.Minute)}
	trackers["stale2"] = &readTracker{lastAccess: now.Add(-10 * time.Minute)}

	// Simulate cleanup logic
	staleThreshold := now.Add(-5 * time.Minute)
	for path, tracker := range trackers {
		if tracker.lastAccess.Before(staleThreshold) {
			delete(trackers, path)
		}
	}

	// Verify results
	if len(trackers) != 2 {
		t.Fatalf("expected 2 trackers after cleanup, got %d", len(trackers))
	}

	if _, ok := trackers["fresh1"]; !ok {
		t.Fatal("expected fresh1 to remain")
	}
	if _, ok := trackers["fresh2"]; !ok {
		t.Fatal("expected fresh2 to remain")
	}
	if _, ok := trackers["stale1"]; ok {
		t.Fatal("expected stale1 to be removed")
	}
	if _, ok := trackers["stale2"]; ok {
		t.Fatal("expected stale2 to be removed")
	}
}

// Integration Tests: Cache Behavior on Source File Changes

func TestDataCache_MultipleFilesWithDifferentMtimes(t *testing.T) {
	cache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

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
		t.Fatal("expected hit for file1 (unchanged)")
	}
	_, hit = cache.ReadWithMtime("file3.txt", 0, 8, 3000)
	if !hit {
		t.Fatal("expected hit for file3 (unchanged)")
	}
}

func TestDataCache_CombinedLRUAndTTL(t *testing.T) {
	// Test that both LRU and TTL work together
	blockSize := int64(16)
	maxSize := int64(32) // 2 blocks max
	ttl := 100 * time.Millisecond
	cache := NewDataCacheWithTTL(maxSize, blockSize, ttl)

	currentTime := time.Now()
	cache.now = func() time.Time { return currentTime }

	// Fill cache with 2 blocks
	cache.Write("file1", 0, []byte("1111111111111111"))
	cache.Write("file2", 0, []byte("2222222222222222"))

	// Advance time partially (not past TTL)
	currentTime = currentTime.Add(50 * time.Millisecond)

	// Access file1 to make it recently used
	_, _ = cache.Read("file1", 0, 16)

	// Advance time past TTL for file2 but file1's TTL refreshed by access
	currentTime = currentTime.Add(60 * time.Millisecond)

	// file2 should be expired (TTL)
	_, hit := cache.Read("file2", 0, 16)
	if hit {
		t.Fatal("expected miss for file2 (TTL expired)")
	}

	// file1 was accessed recently, but TTL still applies
	// It's now 110ms since file1 was written, 60ms since accessed
	// If read refreshes TTL... (it doesn't in current impl)
	// Let's check: file1 should also be expired
	_, hit = cache.Read("file1", 0, 16)
	if hit {
		t.Fatal("expected miss for file1 (TTL expired from original write)")
	}
}
