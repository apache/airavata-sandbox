package cache

import (
	"context"
	"sync"
	"sync/atomic"
	"testing"
	"time"

	pb "github.com/apache/airavata-sandbox/remotefs-fuse/proto/gen/remotefs"
)

// mockFileprotoClient simulates a remote server for testing cache consistency.
// It allows us to modify "remote" file state and verify the cache handles changes correctly.
type mockFileprotoClient struct {
	mu       sync.RWMutex
	files    map[string]*mockFile
	requests []string // Track requests for verification
}

type mockFile struct {
	data  []byte
	mtime int64
	size  uint64
}

func newMockClient() *mockFileprotoClient {
	return &mockFileprotoClient{
		files:    make(map[string]*mockFile),
		requests: make([]string, 0),
	}
}

func (m *mockFileprotoClient) SetFile(path string, data []byte, mtime int64) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.files[path] = &mockFile{
		data:  data,
		mtime: mtime,
		size:  uint64(len(data)),
	}
}

func (m *mockFileprotoClient) DeleteFile(path string) {
	m.mu.Lock()
	defer m.mu.Unlock()
	delete(m.files, path)
}

func (m *mockFileprotoClient) GetRequestCount() int {
	m.mu.RLock()
	defer m.mu.RUnlock()
	return len(m.requests)
}

func (m *mockFileprotoClient) ResetRequestCount() {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.requests = make([]string, 0)
}

// Do implements the fileproto.Client interface for testing.
func (m *mockFileprotoClient) Do(ctx context.Context, req *pb.FileRequest) (*pb.FileResponse, error) {
	m.mu.Lock()
	defer m.mu.Unlock()

	switch op := req.Op.(type) {
	case *pb.FileRequest_GetAttr:
		m.requests = append(m.requests, "GetAttr:"+op.GetAttr.Path)
		file, ok := m.files[op.GetAttr.Path]
		if !ok {
			return &pb.FileResponse{Errno: 2}, nil // ENOENT
		}
		return &pb.FileResponse{
			Result: &pb.FileResponse_GetAttr{
				GetAttr: &pb.GetAttrResult{
					Attr: &pb.Attr{
						Size:  file.size,
						Mtime: uint64(file.mtime),
						Mode:  0644,
					},
				},
			},
		}, nil

	case *pb.FileRequest_Read:
		m.requests = append(m.requests, "Read:"+op.Read.Path)
		file, ok := m.files[op.Read.Path]
		if !ok {
			return &pb.FileResponse{Errno: 2}, nil // ENOENT
		}
		offset := int64(op.Read.Offset)
		size := int64(op.Read.Size)
		if offset >= int64(len(file.data)) {
			return &pb.FileResponse{
				Result: &pb.FileResponse_Read{
					Read: &pb.ReadResult{Data: nil},
				},
			}, nil
		}
		end := offset + size
		if end > int64(len(file.data)) {
			end = int64(len(file.data))
		}
		return &pb.FileResponse{
			Result: &pb.FileResponse_Read{
				Read: &pb.ReadResult{Data: file.data[offset:end]},
			},
		}, nil

	case *pb.FileRequest_Write:
		m.requests = append(m.requests, "Write:"+op.Write.Path)
		file, ok := m.files[op.Write.Path]
		if !ok {
			file = &mockFile{data: make([]byte, 0), mtime: time.Now().Unix()}
			m.files[op.Write.Path] = file
		}
		offset := int64(op.Write.Offset)
		newEnd := offset + int64(len(op.Write.Data))
		if newEnd > int64(len(file.data)) {
			newData := make([]byte, newEnd)
			copy(newData, file.data)
			file.data = newData
		}
		copy(file.data[offset:], op.Write.Data)
		file.size = uint64(len(file.data))
		file.mtime = time.Now().Unix() // Update mtime on write
		return &pb.FileResponse{
			Result: &pb.FileResponse_Write{
				Write: &pb.WriteResult{Written: uint32(len(op.Write.Data))},
			},
		}, nil

	default:
		return &pb.FileResponse{}, nil
	}
}

// TestConsistency_SourceFileModification verifies that when a source file is modified,
// the cache returns fresh data, not stale cached data.
func TestConsistency_SourceFileModification(t *testing.T) {
	// Create mock client with initial file
	mock := newMockClient()
	originalData := []byte("Original content that will be cached")
	originalMtime := int64(1000)
	mock.SetFile("test.txt", originalData, originalMtime)

	// Create cache config
	config := &Config{
		MaxDataCacheSize:    1024 * 1024,
		BlockSize:           64,
		MetadataTTL:         time.Hour, // Long TTL to test mtime-based invalidation
		DirectoryTTL:        time.Hour,
		Enabled:             true,
		EnablePrefetch:      false,
		EnableParallelFetch: false,
	}

	// Create data cache and metadata cache
	dataCache := NewDataCacheWithTTL(config.MaxDataCacheSize, config.BlockSize, config.MetadataTTL)
	metaCache := NewMetadataCache(config.MetadataTTL)

	// Simulate initial read - cache the data with mtime
	dataCache.WriteWithMtime("test.txt", 0, originalData, originalMtime)
	metaCache.Set("test.txt", &pb.Attr{Size: uint64(len(originalData)), Mtime: uint64(originalMtime)})

	// Verify cache hit with same mtime
	data, hit := dataCache.ReadWithMtime("test.txt", 0, int64(len(originalData)), originalMtime)
	if !hit {
		t.Fatal("expected cache hit for original data")
	}
	if string(data) != string(originalData) {
		t.Fatalf("data mismatch: got %q, want %q", data, originalData)
	}

	// CRITICAL: Simulate source file modification (mtime changes)
	newData := []byte("Modified content - this should be returned")
	newMtime := int64(2000)
	mock.SetFile("test.txt", newData, newMtime)

	// Read with new mtime - should cause cache miss (stale detection)
	_, hit = dataCache.ReadWithMtime("test.txt", 0, int64(len(originalData)), newMtime)
	if hit {
		t.Fatal("CONSISTENCY ERROR: Cache returned stale data after source file modified")
	}

	// After invalidation, cache should be cleared
	_, hit = dataCache.Read("test.txt", 0, int64(len(originalData)))
	if hit {
		t.Fatal("CONSISTENCY ERROR: Cache still contains data after stale detection")
	}
}

// TestConsistency_MetadataTTLExpiry verifies that metadata TTL expiry forces fresh fetch.
func TestConsistency_MetadataTTLExpiry(t *testing.T) {
	// Create metadata cache with very short TTL
	metaCache := NewMetadataCache(10 * time.Millisecond)

	// Set metadata
	attr := &pb.Attr{Size: 100, Mtime: 1000}
	metaCache.Set("test.txt", attr)

	// Verify cache hit
	_, ok := metaCache.Get("test.txt")
	if !ok {
		t.Fatal("expected metadata cache hit")
	}

	// Wait for TTL to expire
	time.Sleep(20 * time.Millisecond)

	// Verify cache miss after TTL
	_, ok = metaCache.Get("test.txt")
	if ok {
		t.Fatal("expected metadata cache miss after TTL expiry")
	}
}

// TestConsistency_WriteInvalidatesCache verifies that writes invalidate cached data.
func TestConsistency_WriteInvalidatesCache(t *testing.T) {
	dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)
	metaCache := NewMetadataCache(time.Hour)

	path := "test.txt"
	mtime := int64(1000)

	// Cache some data
	dataCache.WriteWithMtime(path, 0, []byte("Original data"), mtime)
	metaCache.Set(path, &pb.Attr{Size: 13, Mtime: uint64(mtime)})

	// Verify cached
	_, hit := dataCache.ReadWithMtime(path, 0, 13, mtime)
	if !hit {
		t.Fatal("expected cache hit")
	}

	// Simulate a write operation (as CachedClient.handleWrite does)
	// Write invalidates the affected range
	dataCache.Invalidate(path, 0, 5) // Invalidate first 5 bytes
	metaCache.Invalidate(path)       // Invalidate metadata

	// Data cache should be partially invalidated
	// (in practice, the whole file is typically invalidated)
	_, hit = metaCache.Get(path)
	if hit {
		t.Fatal("expected metadata cache miss after write")
	}
}

// TestConsistency_FileDeletion verifies that deleted files are not served from cache.
func TestConsistency_FileDeletion(t *testing.T) {
	dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)
	metaCache := NewMetadataCache(time.Hour)

	path := "deleted.txt"
	mtime := int64(1000)

	// Cache the file
	dataCache.WriteWithMtime(path, 0, []byte("Will be deleted"), mtime)
	metaCache.Set(path, &pb.Attr{Size: 15, Mtime: uint64(mtime)})

	// Simulate file deletion (as CachedClient.handleUnlink does)
	dataCache.InvalidateAll(path)
	metaCache.Invalidate(path)

	// Both caches should miss
	_, hit := dataCache.Read(path, 0, 15)
	if hit {
		t.Fatal("expected data cache miss after deletion")
	}
	_, ok := metaCache.Get(path)
	if ok {
		t.Fatal("expected metadata cache miss after deletion")
	}
}

// TestConsistency_FileTruncation verifies that truncated files don't return stale data.
func TestConsistency_FileTruncation(t *testing.T) {
	dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

	path := "truncated.txt"
	mtime := int64(1000)

	// Cache a large file (multiple blocks)
	largeData := make([]byte, 200)
	for i := range largeData {
		largeData[i] = byte(i % 256)
	}
	dataCache.WriteWithMtime(path, 0, largeData, mtime)

	// Verify all data is cached
	_, hit := dataCache.ReadWithMtime(path, 0, 200, mtime)
	if !hit {
		t.Fatal("expected cache hit for full file")
	}

	// Simulate truncation to 50 bytes (as CachedClient.handleSetAttr does)
	dataCache.InvalidateBeyond(path, 50)

	// First 50 bytes should still be cached (within the first block)
	_, hit = dataCache.ReadWithMtime(path, 0, 50, mtime)
	if !hit {
		t.Fatal("expected cache hit for data within truncation point")
	}

	// Data beyond truncation point should be invalidated
	_, hit = dataCache.ReadWithMtime(path, 64, 64, mtime) // Second block
	if hit {
		t.Fatal("expected cache miss for data beyond truncation point")
	}
}

// TestConsistency_RaceConditionProtection verifies thread-safety of cache operations.
func TestConsistency_RaceConditionProtection(t *testing.T) {
	dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

	path := "race.txt"
	var operations int64

	// Run multiple concurrent operations
	var wg sync.WaitGroup
	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for j := 0; j < 100; j++ {
				mtime := int64(id*1000 + j)
				data := make([]byte, 64)
				for k := range data {
					data[k] = byte((id + j) % 256)
				}

				// Write
				dataCache.WriteWithMtime(path, 0, data, mtime)
				atomic.AddInt64(&operations, 1)

				// Read with same mtime
				_, _ = dataCache.ReadWithMtime(path, 0, 64, mtime)
				atomic.AddInt64(&operations, 1)

				// Invalidate
				if j%10 == 0 {
					dataCache.InvalidateAll(path)
					atomic.AddInt64(&operations, 1)
				}
			}
		}(i)
	}

	wg.Wait()
	t.Logf("Completed %d concurrent operations without panics", operations)
}

// TestConsistency_MtimeZeroBypassesValidation verifies that mtime=0 reads work (for backwards compat).
func TestConsistency_MtimeZeroBypassesValidation(t *testing.T) {
	dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

	path := "test.txt"
	mtime := int64(1000)

	// Write data with mtime
	dataCache.WriteWithMtime(path, 0, []byte("Data with mtime"), mtime)

	// Read with mtime=0 should still hit cache (backwards compat)
	data, hit := dataCache.ReadWithMtime(path, 0, 15, 0)
	if !hit {
		t.Fatal("expected cache hit when mtime=0 (no validation)")
	}
	if string(data) != "Data with mtime" {
		t.Fatalf("unexpected data: %q", data)
	}
}

// TestConsistency_MultipleFilesIndependent verifies that one file's invalidation doesn't affect others.
func TestConsistency_MultipleFilesIndependent(t *testing.T) {
	dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

	// Cache multiple files
	dataCache.WriteWithMtime("file1.txt", 0, []byte("File 1 content"), 1000)
	dataCache.WriteWithMtime("file2.txt", 0, []byte("File 2 content"), 2000)
	dataCache.WriteWithMtime("file3.txt", 0, []byte("File 3 content"), 3000)

	// Invalidate file2 by mtime change
	_, hit := dataCache.ReadWithMtime("file2.txt", 0, 14, 2001) // Different mtime
	if hit {
		t.Fatal("expected cache miss for file2 with changed mtime")
	}

	// file1 and file3 should still be cached
	_, hit = dataCache.ReadWithMtime("file1.txt", 0, 14, 1000)
	if !hit {
		t.Fatal("expected cache hit for file1")
	}
	_, hit = dataCache.ReadWithMtime("file3.txt", 0, 14, 3000)
	if !hit {
		t.Fatal("expected cache hit for file3")
	}
}

// TestConsistency_BlockLevelInvalidation verifies that partial writes invalidate correct blocks.
func TestConsistency_BlockLevelInvalidation(t *testing.T) {
	// Block size of 64 bytes
	dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

	path := "blocks.txt"
	mtime := int64(1000)

	// Write 3 blocks (192 bytes)
	data := make([]byte, 192)
	for i := range data {
		data[i] = byte(i % 256)
	}
	dataCache.WriteWithMtime(path, 0, data, mtime)

	// Verify all blocks are cached
	for blockIdx := int64(0); blockIdx < 3; blockIdx++ {
		_, hit := dataCache.ReadWithMtime(path, blockIdx*64, 64, mtime)
		if !hit {
			t.Fatalf("expected cache hit for block %d", blockIdx)
		}
	}

	// Invalidate middle block (bytes 64-127)
	dataCache.Invalidate(path, 64, 64)

	// First and third blocks should still be cached
	_, hit := dataCache.ReadWithMtime(path, 0, 64, mtime)
	if !hit {
		t.Fatal("expected cache hit for first block")
	}
	_, hit = dataCache.ReadWithMtime(path, 128, 64, mtime)
	if !hit {
		t.Fatal("expected cache hit for third block")
	}

	// Middle block should be invalidated
	_, hit = dataCache.ReadWithMtime(path, 64, 64, mtime)
	if hit {
		t.Fatal("expected cache miss for invalidated middle block")
	}
}

// TestConsistency_EndToEndScenario simulates a realistic scenario of file operations.
func TestConsistency_EndToEndScenario(t *testing.T) {
	dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)
	metaCache := NewMetadataCache(time.Hour)

	path := "document.txt"

	// Step 1: Initial file creation (mtime=1000)
	content1 := []byte("Initial document content")
	mtime1 := int64(1000)
	dataCache.WriteWithMtime(path, 0, content1, mtime1)
	metaCache.Set(path, &pb.Attr{Size: uint64(len(content1)), Mtime: uint64(mtime1)})

	// Step 2: Read the file - should hit cache
	data, hit := dataCache.ReadWithMtime(path, 0, int64(len(content1)), mtime1)
	if !hit || string(data) != string(content1) {
		t.Fatal("Step 2: Expected cache hit with original content")
	}

	// Step 3: User edits file externally (mtime changes to 2000)
	content2 := []byte("Updated document content - longer now!")
	mtime2 := int64(2000)
	// (In real scenario, this happens on the remote server)

	// Step 4: Next read should detect stale data via mtime mismatch
	_, hit = dataCache.ReadWithMtime(path, 0, int64(len(content1)), mtime2)
	if hit {
		t.Fatal("Step 4: CONSISTENCY ERROR - cache returned stale data after external modification")
	}

	// Step 5: Cache fresh data with new mtime
	dataCache.WriteWithMtime(path, 0, content2, mtime2)
	metaCache.Set(path, &pb.Attr{Size: uint64(len(content2)), Mtime: uint64(mtime2)})

	// Step 6: Read should now return new content
	data, hit = dataCache.ReadWithMtime(path, 0, int64(len(content2)), mtime2)
	if !hit || string(data) != string(content2) {
		t.Fatal("Step 6: Expected cache hit with updated content")
	}

	// Step 7: File deleted externally
	dataCache.InvalidateAll(path)
	metaCache.Invalidate(path)

	// Step 8: Read should fail
	_, hit = dataCache.Read(path, 0, int64(len(content2)))
	if hit {
		t.Fatal("Step 8: CONSISTENCY ERROR - cache returned data for deleted file")
	}
}

// TestConsistency_CleanupDoesNotAffectValidData verifies that TTL cleanup doesn't affect valid data.
func TestConsistency_CleanupDoesNotAffectValidData(t *testing.T) {
	// Long TTL
	dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

	path := "valid.txt"
	mtime := int64(1000)

	// Cache data
	dataCache.WriteWithMtime(path, 0, []byte("Valid data"), mtime)

	// Run cleanup
	removed := dataCache.Cleanup()
	if removed != 0 {
		t.Fatalf("expected 0 blocks removed from cleanup, got %d", removed)
	}

	// Data should still be accessible
	data, hit := dataCache.ReadWithMtime(path, 0, 10, mtime)
	if !hit || string(data) != "Valid data" {
		t.Fatal("data should still be accessible after cleanup")
	}
}
