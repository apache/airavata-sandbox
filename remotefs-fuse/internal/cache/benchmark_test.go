package cache

import (
	"context"
	"math/rand"
	"os"
	"path/filepath"
	"testing"
	"time"

	"github.com/apache/airavata-sandbox/remotefs-fuse/internal/export"
	"github.com/apache/airavata-sandbox/remotefs-fuse/internal/fileproto"
	pb "github.com/apache/airavata-sandbox/remotefs-fuse/proto/gen/remotefs"
)

// Data Cache Benchmarks

func BenchmarkDataCache_Write(b *testing.B) {
	cache := NewDataCache(256*1024*1024, 64*1024) // 256MB, 64KB blocks
	data := make([]byte, 64*1024)                 // 64KB write
	for i := range data {
		data[i] = byte(i % 256)
	}

	b.ResetTimer()
	b.SetBytes(int64(len(data)))

	for i := 0; i < b.N; i++ {
		path := "file" + string(rune('A'+i%26)) + ".txt"
		cache.Write(path, 0, data)
	}
}

func BenchmarkDataCache_ReadHit(b *testing.B) {
	cache := NewDataCache(256*1024*1024, 64*1024)
	data := make([]byte, 64*1024)
	path := "test/file.txt"
	cache.Write(path, 0, data)

	b.ResetTimer()
	b.SetBytes(int64(len(data)))

	for i := 0; i < b.N; i++ {
		cache.Read(path, 0, int64(len(data)))
	}
}

func BenchmarkDataCache_ReadMiss(b *testing.B) {
	cache := NewDataCache(256*1024*1024, 64*1024)

	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		path := "nonexistent" + string(rune('A'+i%26)) + ".txt"
		cache.Read(path, 0, 64*1024)
	}
}

func BenchmarkDataCache_SequentialRead(b *testing.B) {
	cache := NewDataCache(256*1024*1024, 64*1024)
	fileSize := int64(10 * 1024 * 1024) // 10MB file
	data := make([]byte, fileSize)
	path := "large/file.bin"
	cache.Write(path, 0, data)

	readSize := int64(4 * 1024) // 4KB reads

	b.ResetTimer()
	b.SetBytes(fileSize)

	for i := 0; i < b.N; i++ {
		for offset := int64(0); offset < fileSize; offset += readSize {
			cache.Read(path, offset, readSize)
		}
	}
}

func BenchmarkDataCache_RandomRead(b *testing.B) {
	cache := NewDataCache(256*1024*1024, 64*1024)
	fileSize := int64(10 * 1024 * 1024)
	data := make([]byte, fileSize)
	path := "large/file.bin"
	cache.Write(path, 0, data)

	readSize := int64(4 * 1024)
	rng := rand.New(rand.NewSource(42))

	b.ResetTimer()
	b.SetBytes(readSize)

	for i := 0; i < b.N; i++ {
		offset := rng.Int63n(fileSize - readSize)
		cache.Read(path, offset, readSize)
	}
}

func BenchmarkDataCache_LRUEviction(b *testing.B) {
	// Small cache to force eviction
	cache := NewDataCache(1024*1024, 64*1024) // 1MB, 64KB blocks (~16 blocks)
	data := make([]byte, 64*1024)

	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		// Write many files to trigger eviction
		path := "file" + string(rune(i%1000)) + ".txt"
		cache.Write(path, 0, data)
	}
}

// Metadata Cache Benchmarks

func BenchmarkMetadataCache_Set(b *testing.B) {
	cache := NewMetadataCache(time.Hour)
	attr := &pb.Attr{Ino: 123, Size: 1024, Mode: 0644}

	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		path := "file" + string(rune('A'+i%26)) + string(rune('0'+i%10)) + ".txt"
		cache.Set(path, attr)
	}
}

func BenchmarkMetadataCache_GetHit(b *testing.B) {
	cache := NewMetadataCache(time.Hour)
	path := "test/file.txt"
	cache.Set(path, &pb.Attr{Ino: 123})

	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		cache.Get(path)
	}
}

func BenchmarkMetadataCache_GetMiss(b *testing.B) {
	cache := NewMetadataCache(time.Hour)

	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		path := "nonexistent" + string(rune('A'+i%26)) + ".txt"
		cache.Get(path)
	}
}

func BenchmarkMetadataCache_Invalidate(b *testing.B) {
	cache := NewMetadataCache(time.Hour)
	// Pre-populate
	for i := 0; i < 1000; i++ {
		path := "file" + string(rune('A'+i%26)) + string(rune('0'+i%10)) + ".txt"
		cache.Set(path, &pb.Attr{Ino: uint64(i)})
	}

	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		path := "file" + string(rune('A'+i%26)) + string(rune('0'+i%10)) + ".txt"
		cache.Invalidate(path)
		cache.Set(path, &pb.Attr{Ino: uint64(i)}) // Re-add for next iteration
	}
}

// Directory Cache Benchmarks

func BenchmarkDirectoryCache_Set(b *testing.B) {
	cache := NewDirectoryCache(time.Hour)
	entries := make([]*pb.DirEntry, 100)
	for i := range entries {
		entries[i] = &pb.DirEntry{Name: "file" + string(rune('0'+i%10)) + ".txt", Ino: uint64(i)}
	}

	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		path := "dir" + string(rune('A'+i%26))
		cache.Set(path, entries)
	}
}

func BenchmarkDirectoryCache_GetHit(b *testing.B) {
	cache := NewDirectoryCache(time.Hour)
	entries := make([]*pb.DirEntry, 100)
	for i := range entries {
		entries[i] = &pb.DirEntry{Name: "file" + string(rune('0'+i%10)) + ".txt", Ino: uint64(i)}
	}
	path := "test/dir"
	cache.Set(path, entries)

	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		cache.Get(path)
	}
}

func BenchmarkDirectoryCache_LargeDirectory(b *testing.B) {
	cache := NewDirectoryCache(time.Hour)
	// Simulate a large directory with 10000 entries
	entries := make([]*pb.DirEntry, 10000)
	for i := range entries {
		entries[i] = &pb.DirEntry{Name: "file" + string(rune('0'+i)) + ".txt", Ino: uint64(i)}
	}
	path := "large/dir"
	cache.Set(path, entries)

	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		cache.Get(path)
	}
}

// CachedClient Benchmarks (with mock backend)

func BenchmarkCachedClient_ReadCacheHit(b *testing.B) {
	dir := b.TempDir()
	testFile := filepath.Join(dir, "test.txt")
	content := make([]byte, 64*1024) // 64KB
	for i := range content {
		content[i] = byte(i % 256)
	}
	if err := os.WriteFile(testFile, content, 0644); err != nil {
		b.Fatal(err)
	}

	paths := []*pb.ExportPath{{LocalPath: dir, VirtualName: "data"}}
	backend := export.NewBackend(paths)
	stream := newMockStream(backend)
	client := fileproto.NewClient(stream)
	go client.Run()

	cachedClient := NewCachedClient(client, DefaultConfig())
	ctx := context.Background()
	vpath := "data/test.txt"

	// Open file
	openResp, _ := cachedClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Open{Open: &pb.OpenRequest{Path: vpath, Flags: 0}},
	})
	handleID := openResp.GetOpen().HandleId

	// Prime the cache
	cachedClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Read{Read: &pb.ReadRequest{
			Path: vpath, HandleId: handleID, Offset: 0, Size: uint32(len(content)),
		}},
	})

	b.ResetTimer()
	b.SetBytes(int64(len(content)))

	for i := 0; i < b.N; i++ {
		cachedClient.Do(ctx, &pb.FileRequest{
			Op: &pb.FileRequest_Read{Read: &pb.ReadRequest{
				Path: vpath, HandleId: handleID, Offset: 0, Size: uint32(len(content)),
			}},
		})
	}
}

func BenchmarkCachedClient_ReadCacheMiss(b *testing.B) {
	dir := b.TempDir()
	testFile := filepath.Join(dir, "test.txt")
	content := make([]byte, 64*1024)
	if err := os.WriteFile(testFile, content, 0644); err != nil {
		b.Fatal(err)
	}

	paths := []*pb.ExportPath{{LocalPath: dir, VirtualName: "data"}}
	backend := export.NewBackend(paths)
	stream := newMockStream(backend)
	client := fileproto.NewClient(stream)
	go client.Run()

	cachedClient := NewCachedClient(client, DefaultConfig())
	ctx := context.Background()
	vpath := "data/test.txt"

	openResp, _ := cachedClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Open{Open: &pb.OpenRequest{Path: vpath, Flags: 0}},
	})
	handleID := openResp.GetOpen().HandleId

	b.ResetTimer()
	b.SetBytes(int64(len(content)))

	for i := 0; i < b.N; i++ {
		// Invalidate cache before each read to force miss
		cachedClient.InvalidateAll()
		cachedClient.Do(ctx, &pb.FileRequest{
			Op: &pb.FileRequest_Read{Read: &pb.ReadRequest{
				Path: vpath, HandleId: handleID, Offset: 0, Size: uint32(len(content)),
			}},
		})
	}
}

func BenchmarkCachedClient_MetadataHit(b *testing.B) {
	dir := b.TempDir()
	testFile := filepath.Join(dir, "test.txt")
	if err := os.WriteFile(testFile, []byte("content"), 0644); err != nil {
		b.Fatal(err)
	}

	paths := []*pb.ExportPath{{LocalPath: dir, VirtualName: "data"}}
	backend := export.NewBackend(paths)
	stream := newMockStream(backend)
	client := fileproto.NewClient(stream)
	go client.Run()

	cachedClient := NewCachedClient(client, DefaultConfig())
	ctx := context.Background()

	// Prime the cache
	cachedClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_GetAttr{GetAttr: &pb.GetAttrRequest{Path: "data/test.txt"}},
	})

	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		cachedClient.Do(ctx, &pb.FileRequest{
			Op: &pb.FileRequest_GetAttr{GetAttr: &pb.GetAttrRequest{Path: "data/test.txt"}},
		})
	}
}

func BenchmarkCachedClient_LookupHit(b *testing.B) {
	dir := b.TempDir()
	testFile := filepath.Join(dir, "test.txt")
	if err := os.WriteFile(testFile, []byte("content"), 0644); err != nil {
		b.Fatal(err)
	}

	paths := []*pb.ExportPath{{LocalPath: dir, VirtualName: "data"}}
	backend := export.NewBackend(paths)
	stream := newMockStream(backend)
	client := fileproto.NewClient(stream)
	go client.Run()

	cachedClient := NewCachedClient(client, DefaultConfig())
	ctx := context.Background()

	// Prime the cache
	cachedClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Lookup{Lookup: &pb.LookupRequest{Path: "data", Name: "test.txt"}},
	})

	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		cachedClient.Do(ctx, &pb.FileRequest{
			Op: &pb.FileRequest_Lookup{Lookup: &pb.LookupRequest{Path: "data", Name: "test.txt"}},
		})
	}
}

// Comparison Benchmarks (Cached vs Uncached)

func BenchmarkComparison_UncachedRead(b *testing.B) {
	dir := b.TempDir()
	testFile := filepath.Join(dir, "test.txt")
	content := make([]byte, 64*1024)
	if err := os.WriteFile(testFile, content, 0644); err != nil {
		b.Fatal(err)
	}

	paths := []*pb.ExportPath{{LocalPath: dir, VirtualName: "data"}}
	backend := export.NewBackend(paths)
	stream := newMockStream(backend)
	client := fileproto.NewClient(stream)
	go client.Run()

	// Disable caching
	config := DefaultConfig()
	config.Enabled = false
	cachedClient := NewCachedClient(client, config)
	ctx := context.Background()
	vpath := "data/test.txt"

	openResp, _ := cachedClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Open{Open: &pb.OpenRequest{Path: vpath, Flags: 0}},
	})
	handleID := openResp.GetOpen().HandleId

	b.ResetTimer()
	b.SetBytes(int64(len(content)))

	for i := 0; i < b.N; i++ {
		cachedClient.Do(ctx, &pb.FileRequest{
			Op: &pb.FileRequest_Read{Read: &pb.ReadRequest{
				Path: vpath, HandleId: handleID, Offset: 0, Size: uint32(len(content)),
			}},
		})
	}
}

func BenchmarkComparison_CachedRead(b *testing.B) {
	dir := b.TempDir()
	testFile := filepath.Join(dir, "test.txt")
	content := make([]byte, 64*1024)
	if err := os.WriteFile(testFile, content, 0644); err != nil {
		b.Fatal(err)
	}

	paths := []*pb.ExportPath{{LocalPath: dir, VirtualName: "data"}}
	backend := export.NewBackend(paths)
	stream := newMockStream(backend)
	client := fileproto.NewClient(stream)
	go client.Run()

	// Enable caching (default)
	cachedClient := NewCachedClient(client, DefaultConfig())
	ctx := context.Background()
	vpath := "data/test.txt"

	openResp, _ := cachedClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Open{Open: &pb.OpenRequest{Path: vpath, Flags: 0}},
	})
	handleID := openResp.GetOpen().HandleId

	// Prime cache
	cachedClient.Do(ctx, &pb.FileRequest{
		Op: &pb.FileRequest_Read{Read: &pb.ReadRequest{
			Path: vpath, HandleId: handleID, Offset: 0, Size: uint32(len(content)),
		}},
	})

	b.ResetTimer()
	b.SetBytes(int64(len(content)))

	for i := 0; i < b.N; i++ {
		cachedClient.Do(ctx, &pb.FileRequest{
			Op: &pb.FileRequest_Read{Read: &pb.ReadRequest{
				Path: vpath, HandleId: handleID, Offset: 0, Size: uint32(len(content)),
			}},
		})
	}
}

// Parallel Benchmarks

func BenchmarkDataCache_ParallelReads(b *testing.B) {
	cache := NewDataCache(256*1024*1024, 64*1024)
	data := make([]byte, 64*1024)
	path := "test/file.txt"
	cache.Write(path, 0, data)

	b.ResetTimer()
	b.SetBytes(int64(len(data)))

	b.RunParallel(func(pb *testing.PB) {
		for pb.Next() {
			cache.Read(path, 0, int64(len(data)))
		}
	})
}

func BenchmarkMetadataCache_ParallelGets(b *testing.B) {
	cache := NewMetadataCache(time.Hour)
	path := "test/file.txt"
	cache.Set(path, &pb.Attr{Ino: 123, Size: 1024})

	b.ResetTimer()

	b.RunParallel(func(pb *testing.PB) {
		for pb.Next() {
			cache.Get(path)
		}
	})
}

func BenchmarkDirectoryCache_ParallelGets(b *testing.B) {
	cache := NewDirectoryCache(time.Hour)
	entries := make([]*pb.DirEntry, 100)
	for i := range entries {
		entries[i] = &pb.DirEntry{Name: "file" + string(rune('0'+i%10)) + ".txt"}
	}
	path := "test/dir"
	cache.Set(path, entries)

	b.ResetTimer()

	b.RunParallel(func(pb *testing.PB) {
		for pb.Next() {
			cache.Get(path)
		}
	})
}
