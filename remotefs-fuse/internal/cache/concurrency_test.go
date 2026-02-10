package cache

import (
	"sync"
	"testing"
	"time"

	pb "github.com/apache/airavata-sandbox/remotefs-fuse/proto/gen/remotefs"
)

// Data Cache Concurrency Tests

func TestDataCache_ConcurrentReads(t *testing.T) {
	cache := NewDataCache(1024*1024, 64)

	// Populate cache
	path := "test/file.txt"
	data := []byte("Hello, concurrent readers!")
	cache.Write(path, 0, data)

	// Concurrent reads
	var wg sync.WaitGroup
	numReaders := 100
	errors := make(chan error, numReaders)

	for i := 0; i < numReaders; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			result, complete := cache.Read(path, 0, int64(len(data)))
			if !complete {
				errors <- nil // Just flag it
				return
			}
			if string(result) != string(data) {
				errors <- nil
			}
		}()
	}

	wg.Wait()
	close(errors)

	for err := range errors {
		if err != nil {
			t.Fatal("concurrent read returned incorrect data")
		}
	}
}

func TestDataCache_ConcurrentWrites(t *testing.T) {
	cache := NewDataCache(1024*1024, 64)

	// Concurrent writes to different paths
	var wg sync.WaitGroup
	numWriters := 100

	for i := 0; i < numWriters; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			path := "file" + string(rune('A'+id%26)) + ".txt"
			data := make([]byte, 100)
			for j := range data {
				data[j] = byte(id)
			}
			cache.Write(path, 0, data)
		}(i)
	}

	wg.Wait()

	// Verify cache is in consistent state
	if cache.Size() == 0 {
		t.Fatal("expected cache to have data after concurrent writes")
	}
}

func TestDataCache_ConcurrentReadWrite(t *testing.T) {
	cache := NewDataCache(1024*1024, 64)

	path := "test/file.txt"
	data := []byte("Initial data for concurrent test")
	cache.Write(path, 0, data)

	var wg sync.WaitGroup
	stop := make(chan struct{})

	// Start readers
	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for {
				select {
				case <-stop:
					return
				default:
					cache.Read(path, 0, 100)
				}
			}
		}()
	}

	// Start writers
	for i := 0; i < 5; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for j := 0; j < 100; j++ {
				select {
				case <-stop:
					return
				default:
					newData := make([]byte, 50)
					for k := range newData {
						newData[k] = byte(id + j)
					}
					cache.Write(path, int64(j*50), newData)
				}
			}
		}(i)
	}

	// Let it run for a bit
	time.Sleep(100 * time.Millisecond)
	close(stop)
	wg.Wait()

	// Test passes if no panic/deadlock occurred
}

func TestDataCache_ConcurrentEviction(t *testing.T) {
	// Small cache to force eviction
	cache := NewDataCache(1024, 64) // Only ~16 blocks

	var wg sync.WaitGroup
	numGoroutines := 50

	for i := 0; i < numGoroutines; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for j := 0; j < 20; j++ {
				path := "file" + string(rune('A'+id%26)) + string(rune('0'+j%10)) + ".txt"
				data := make([]byte, 100)
				cache.Write(path, 0, data)
				cache.Read(path, 0, 50)
			}
		}(i)
	}

	wg.Wait()

	// Cache should be at or under max size
	if cache.Size() > 1024 {
		t.Fatalf("cache size %d exceeds max %d", cache.Size(), 1024)
	}
}

func TestDataCache_ConcurrentInvalidate(t *testing.T) {
	cache := NewDataCache(1024*1024, 64)

	path := "test/file.txt"

	var wg sync.WaitGroup
	stop := make(chan struct{})

	// Writers
	wg.Add(1)
	go func() {
		defer wg.Done()
		for {
			select {
			case <-stop:
				return
			default:
				cache.Write(path, 0, []byte("data"))
			}
		}
	}()

	// Invalidators
	wg.Add(1)
	go func() {
		defer wg.Done()
		for {
			select {
			case <-stop:
				return
			default:
				cache.InvalidateAll(path)
			}
		}
	}()

	// Readers
	wg.Add(1)
	go func() {
		defer wg.Done()
		for {
			select {
			case <-stop:
				return
			default:
				cache.Read(path, 0, 10)
			}
		}
	}()

	time.Sleep(100 * time.Millisecond)
	close(stop)
	wg.Wait()
}

// Metadata Cache Concurrency Tests

func TestMetadataCache_ConcurrentReads(t *testing.T) {
	cache := NewMetadataCache(time.Hour)

	path := "test/file.txt"
	cache.Set(path, &pb.Attr{Ino: 123, Size: 1000})

	var wg sync.WaitGroup
	numReaders := 100

	for i := 0; i < numReaders; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for j := 0; j < 100; j++ {
				attr, ok := cache.Get(path)
				if ok && attr.Ino != 123 {
					t.Error("got incorrect ino")
				}
			}
		}()
	}

	wg.Wait()
}

func TestMetadataCache_ConcurrentSetGet(t *testing.T) {
	cache := NewMetadataCache(time.Hour)

	var wg sync.WaitGroup
	stop := make(chan struct{})

	// Writers
	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for {
				select {
				case <-stop:
					return
				default:
					path := "file" + string(rune('A'+id)) + ".txt"
					cache.Set(path, &pb.Attr{Ino: uint64(id)})
				}
			}
		}(i)
	}

	// Readers
	for i := 0; i < 20; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for {
				select {
				case <-stop:
					return
				default:
					path := "file" + string(rune('A'+id%10)) + ".txt"
					cache.Get(path)
				}
			}
		}(i)
	}

	// Invalidators
	for i := 0; i < 5; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			for {
				select {
				case <-stop:
					return
				default:
					path := "file" + string(rune('A'+id%10)) + ".txt"
					cache.Invalidate(path)
				}
			}
		}(i)
	}

	time.Sleep(100 * time.Millisecond)
	close(stop)
	wg.Wait()
}

func TestMetadataCache_ConcurrentExpiration(t *testing.T) {
	cache := NewMetadataCache(10 * time.Millisecond)

	var wg sync.WaitGroup
	stop := make(chan struct{})

	// Writers - constantly adding entries
	for i := 0; i < 5; i++ {
		wg.Add(1)
		go func(id int) {
			defer wg.Done()
			counter := 0
			for {
				select {
				case <-stop:
					return
				default:
					path := "file" + string(rune('A'+counter%26)) + ".txt"
					cache.Set(path, &pb.Attr{Ino: uint64(id)})
					counter++
				}
			}
		}(i)
	}

	// Readers - reading potentially expired entries
	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for {
				select {
				case <-stop:
					return
				default:
					for c := 'A'; c <= 'Z'; c++ {
						path := "file" + string(c) + ".txt"
						cache.Get(path)
					}
				}
			}
		}()
	}

	// Cleanup runner
	wg.Add(1)
	go func() {
		defer wg.Done()
		for {
			select {
			case <-stop:
				return
			default:
				cache.Cleanup()
				time.Sleep(5 * time.Millisecond)
			}
		}
	}()

	time.Sleep(100 * time.Millisecond)
	close(stop)
	wg.Wait()
}

// Directory Cache Concurrency Tests

func TestDirectoryCache_ConcurrentReads(t *testing.T) {
	cache := NewDirectoryCache(time.Hour)

	path := "test/dir"
	entries := []*pb.DirEntry{
		{Name: "file1.txt", Ino: 1},
		{Name: "file2.txt", Ino: 2},
	}
	cache.Set(path, entries)

	var wg sync.WaitGroup
	numReaders := 100

	for i := 0; i < numReaders; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for j := 0; j < 100; j++ {
				result, ok := cache.Get(path)
				if ok && len(result) != 2 {
					t.Error("got incorrect number of entries")
				}
			}
		}()
	}

	wg.Wait()
}

func TestDirectoryCache_ConcurrentModifications(t *testing.T) {
	cache := NewDirectoryCache(time.Hour)

	path := "test/dir"
	cache.Set(path, []*pb.DirEntry{{Name: "initial.txt", Ino: 1}})

	var wg sync.WaitGroup
	stop := make(chan struct{})

	// Add entries
	wg.Add(1)
	go func() {
		defer wg.Done()
		counter := 0
		for {
			select {
			case <-stop:
				return
			default:
				cache.AddEntry(path, &pb.DirEntry{
					Name: "file" + string(rune('0'+counter%10)) + ".txt",
					Ino:  uint64(counter),
				})
				counter++
			}
		}
	}()

	// Remove entries
	wg.Add(1)
	go func() {
		defer wg.Done()
		for {
			select {
			case <-stop:
				return
			default:
				for i := 0; i < 10; i++ {
					cache.RemoveEntry(path, "file"+string(rune('0'+i))+".txt")
				}
			}
		}
	}()

	// Readers
	wg.Add(1)
	go func() {
		defer wg.Done()
		for {
			select {
			case <-stop:
				return
			default:
				cache.Get(path)
			}
		}
	}()

	// HasEntry checkers
	wg.Add(1)
	go func() {
		defer wg.Done()
		for {
			select {
			case <-stop:
				return
			default:
				for i := 0; i < 10; i++ {
					cache.HasEntry(path, "file"+string(rune('0'+i))+".txt")
				}
			}
		}
	}()

	time.Sleep(100 * time.Millisecond)
	close(stop)
	wg.Wait()
}

// Cross-Cache Concurrency Tests

func TestAllCaches_ConcurrentOperations(t *testing.T) {
	dataCache := NewDataCache(1024*1024, 64)
	metaCache := NewMetadataCache(time.Hour)
	dirCache := NewDirectoryCache(time.Hour)

	var wg sync.WaitGroup
	stop := make(chan struct{})

	// Data cache operations
	wg.Add(1)
	go func() {
		defer wg.Done()
		for {
			select {
			case <-stop:
				return
			default:
				for i := 0; i < 10; i++ {
					path := "data" + string(rune('0'+i)) + ".txt"
					dataCache.Write(path, 0, []byte("test data"))
					dataCache.Read(path, 0, 10)
					dataCache.InvalidateAll(path)
				}
			}
		}
	}()

	// Metadata cache operations
	wg.Add(1)
	go func() {
		defer wg.Done()
		for {
			select {
			case <-stop:
				return
			default:
				for i := 0; i < 10; i++ {
					path := "meta" + string(rune('0'+i)) + ".txt"
					metaCache.Set(path, &pb.Attr{Ino: uint64(i)})
					metaCache.Get(path)
					metaCache.Invalidate(path)
				}
			}
		}
	}()

	// Directory cache operations
	wg.Add(1)
	go func() {
		defer wg.Done()
		for {
			select {
			case <-stop:
				return
			default:
				for i := 0; i < 10; i++ {
					path := "dir" + string(rune('0'+i))
					dirCache.Set(path, []*pb.DirEntry{{Name: "f.txt"}})
					dirCache.Get(path)
					dirCache.AddEntry(path, &pb.DirEntry{Name: "new.txt"})
					dirCache.RemoveEntry(path, "new.txt")
					dirCache.Invalidate(path)
				}
			}
		}
	}()

	time.Sleep(200 * time.Millisecond)
	close(stop)
	wg.Wait()
}

// Race Detection Tests (run with -race flag)

func TestDataCache_RaceCondition(t *testing.T) {
	cache := NewDataCache(1024, 64)
	path := "race/file.txt"

	var wg sync.WaitGroup
	for i := 0; i < 10; i++ {
		wg.Add(3)

		go func() {
			defer wg.Done()
			cache.Write(path, 0, []byte("data"))
		}()

		go func() {
			defer wg.Done()
			cache.Read(path, 0, 10)
		}()

		go func() {
			defer wg.Done()
			cache.Invalidate(path, 0, 10)
		}()
	}

	wg.Wait()
}

func TestMetadataCache_RaceCondition(t *testing.T) {
	cache := NewMetadataCache(time.Hour)
	path := "race/file.txt"

	var wg sync.WaitGroup
	for i := 0; i < 10; i++ {
		wg.Add(3)

		go func() {
			defer wg.Done()
			cache.Set(path, &pb.Attr{Ino: 1})
		}()

		go func() {
			defer wg.Done()
			cache.Get(path)
		}()

		go func() {
			defer wg.Done()
			cache.Invalidate(path)
		}()
	}

	wg.Wait()
}

func TestDirectoryCache_RaceCondition(t *testing.T) {
	cache := NewDirectoryCache(time.Hour)
	path := "race/dir"

	var wg sync.WaitGroup
	for i := 0; i < 10; i++ {
		wg.Add(4)

		go func() {
			defer wg.Done()
			cache.Set(path, []*pb.DirEntry{{Name: "f.txt"}})
		}()

		go func() {
			defer wg.Done()
			cache.Get(path)
		}()

		go func() {
			defer wg.Done()
			cache.AddEntry(path, &pb.DirEntry{Name: "new.txt"})
		}()

		go func() {
			defer wg.Done()
			cache.Invalidate(path)
		}()
	}

	wg.Wait()
}
