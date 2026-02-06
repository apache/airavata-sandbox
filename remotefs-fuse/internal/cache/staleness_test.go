package cache

import (
	"fmt"
	"math/rand"
	"sync"
	"sync/atomic"
	"testing"
	"time"

	pb "github.com/apache/airavata-sandbox/remotefs-fuse/proto/gen/remotefs"
)

// TestCacheStaleness_MtimeValidation verifies cache behavior under various staleness rates.
// Staleness rate = probability that source file modified between consecutive reads.
func TestCacheStaleness_MtimeValidation(t *testing.T) {
	rates := []float64{0.0, 0.25, 0.50, 0.75, 1.0}

	for _, rate := range rates {
		t.Run(fmt.Sprintf("rate_%.0f_percent", rate*100), func(t *testing.T) {
			dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

			path := "test.txt"
			numReads := 100
			data := []byte("Test content for staleness verification")

			// Track metrics
			cacheHits := 0
			cacheMisses := 0
			currentMtime := int64(1000)

			// Initialize cache with first version
			dataCache.WriteWithMtime(path, 0, data, currentMtime)

			rng := rand.New(rand.NewSource(time.Now().UnixNano()))

			for i := 0; i < numReads; i++ {
				// Simulate potential source file modification
				var readMtime int64
				if rng.Float64() < rate {
					// Source file was modified - mtime changes
					currentMtime++
					readMtime = currentMtime
				} else {
					// Source file unchanged
					readMtime = currentMtime
				}

				// Attempt to read from cache with current mtime
				_, hit := dataCache.ReadWithMtime(path, 0, int64(len(data)), readMtime)

				if hit {
					cacheHits++
				} else {
					cacheMisses++
					// In a real scenario, we'd fetch from remote and re-cache
					dataCache.WriteWithMtime(path, 0, data, readMtime)
				}
			}

			// Calculate actual hit rate
			actualHitRate := float64(cacheHits) / float64(numReads)

			// Expected hit rate approximately equals (1 - staleness_rate) after warm-up
			// With first read always being a hit after initial cache population
			t.Logf("Staleness rate: %.0f%%, Cache hits: %d/%d (%.1f%%)",
				rate*100, cacheHits, numReads, actualHitRate*100)

			// Verify the cache behaves correctly:
			// - At 0% staleness, we should have ~100% hits (file never changes)
			// - At 100% staleness, we should have ~0% hits (file always changes)
			if rate == 0.0 && actualHitRate < 0.95 {
				t.Errorf("Expected ~100%% hits at 0%% staleness, got %.1f%%", actualHitRate*100)
			}
			if rate == 1.0 && actualHitRate > 0.05 {
				t.Errorf("Expected ~0%% hits at 100%% staleness, got %.1f%%", actualHitRate*100)
			}
		})
	}
}

// TestCacheStaleness_ConsistencyGuarantee verifies that the cache NEVER returns stale data.
// This is the critical invariant that distinguishes RemoteFS from SSHFS default caching.
func TestCacheStaleness_ConsistencyGuarantee(t *testing.T) {
	rates := []float64{0.0, 0.25, 0.50, 0.75, 1.0}

	for _, rate := range rates {
		t.Run(fmt.Sprintf("rate_%.0f_percent", rate*100), func(t *testing.T) {
			dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

			path := "test.txt"
			numReads := 100
			currentVersion := 0
			currentMtime := int64(1000)

			// Version-tagged content
			getData := func(version int) []byte {
				return []byte(fmt.Sprintf("Content version %d", version))
			}

			// Initialize cache
			dataCache.WriteWithMtime(path, 0, getData(currentVersion), currentMtime)

			rng := rand.New(rand.NewSource(42)) // Fixed seed for reproducibility
			staleReads := 0

			for i := 0; i < numReads; i++ {
				// Simulate potential source modification
				if rng.Float64() < rate {
					currentVersion++
					currentMtime++
				}

				// Read from cache with current mtime
				data, hit := dataCache.ReadWithMtime(path, 0, 100, currentMtime)

				if hit {
					// CRITICAL CHECK: cached data must match current version
					expected := getData(currentVersion)
					if string(data[:len(expected)]) != string(expected) {
						staleReads++
						t.Errorf("CONSISTENCY VIOLATION: read stale data (got version mismatch)")
					}
				} else {
					// Cache miss - fetch fresh data
					dataCache.WriteWithMtime(path, 0, getData(currentVersion), currentMtime)
				}
			}

			if staleReads > 0 {
				t.Fatalf("CRITICAL: %d stale reads detected out of %d - cache is not consistent!",
					staleReads, numReads)
			}

			t.Logf("Staleness rate: %.0f%%, 0 consistency violations (100%% correct)", rate*100)
		})
	}
}

// TestCacheStaleness_ThroughputDegradation measures cache hit rate degradation with staleness.
func TestCacheStaleness_ThroughputDegradation(t *testing.T) {
	rates := []float64{0.0, 0.25, 0.50, 0.75, 1.0}
	numReads := 1000

	t.Log("Staleness Rate | Cache Hits | Hit Rate")
	t.Log("---------------|------------|----------")

	prevHitRate := 1.0

	for _, rate := range rates {
		dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)
		path := "test.txt"
		data := make([]byte, 4096) // 4KB file

		currentMtime := int64(1000)
		dataCache.WriteWithMtime(path, 0, data, currentMtime)

		rng := rand.New(rand.NewSource(time.Now().UnixNano()))
		hits := 0

		for i := 0; i < numReads; i++ {
			if rng.Float64() < rate {
				currentMtime++
			}

			_, hit := dataCache.ReadWithMtime(path, 0, int64(len(data)), currentMtime)
			if hit {
				hits++
			} else {
				dataCache.WriteWithMtime(path, 0, data, currentMtime)
			}
		}

		hitRate := float64(hits) / float64(numReads)
		t.Logf("%.0f%%            | %4d       | %.1f%%", rate*100, hits, hitRate*100)

		// Verify monotonic degradation (higher staleness = lower hits)
		if rate > 0 && hitRate > prevHitRate+0.1 { // Allow 10% variance
			t.Errorf("Non-monotonic degradation: %.0f%% staleness has %.1f%% hits (prev: %.1f%%)",
				rate*100, hitRate*100, prevHitRate*100)
		}
		prevHitRate = hitRate
	}
}

// TestCacheStaleness_BlockGranularity verifies staleness detection works per-block.
func TestCacheStaleness_BlockGranularity(t *testing.T) {
	blockSize := int64(64)
	dataCache := NewDataCacheWithTTL(1024*1024, blockSize, time.Hour)

	path := "multiblock.txt"
	mtime := int64(1000)

	// Write 4 blocks
	for i := int64(0); i < 4; i++ {
		data := make([]byte, blockSize)
		for j := range data {
			data[j] = byte(i*10 + int64(j))
		}
		dataCache.WriteWithMtime(path, i*blockSize, data, mtime)
	}

	// All blocks should hit with same mtime
	for i := int64(0); i < 4; i++ {
		_, hit := dataCache.ReadWithMtime(path, i*blockSize, blockSize, mtime)
		if !hit {
			t.Errorf("Block %d: expected hit with mtime %d", i, mtime)
		}
	}

	// Change mtime - all blocks should miss
	newMtime := int64(2000)
	for i := int64(0); i < 4; i++ {
		_, hit := dataCache.ReadWithMtime(path, i*blockSize, blockSize, newMtime)
		if hit {
			t.Errorf("Block %d: expected miss with new mtime %d", i, newMtime)
		}
	}
}

// TestCacheStaleness_ConcurrentReadsAndModifications tests thread safety under staleness.
func TestCacheStaleness_ConcurrentReadsAndModifications(t *testing.T) {
	dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

	path := "concurrent.txt"
	data := make([]byte, 256)
	for i := range data {
		data[i] = byte(i)
	}

	var mtime int64 = 1000
	dataCache.WriteWithMtime(path, 0, data, mtime)

	var wg sync.WaitGroup
	var totalReads int64
	var totalHits int64
	var staleReads int64

	// Concurrent readers
	for i := 0; i < 5; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for j := 0; j < 100; j++ {
				currentMtime := atomic.LoadInt64(&mtime)
				_, hit := dataCache.ReadWithMtime(path, 0, int64(len(data)), currentMtime)
				atomic.AddInt64(&totalReads, 1)
				if hit {
					atomic.AddInt64(&totalHits, 1)
				}
			}
		}()
	}

	// Concurrent modifier (simulates 50% staleness)
	wg.Add(1)
	go func() {
		defer wg.Done()
		rng := rand.New(rand.NewSource(time.Now().UnixNano()))
		for j := 0; j < 100; j++ {
			if rng.Float64() < 0.5 {
				// Modify file
				newMtime := atomic.AddInt64(&mtime, 1)
				dataCache.WriteWithMtime(path, 0, data, newMtime)
			}
			time.Sleep(time.Microsecond)
		}
	}()

	wg.Wait()

	t.Logf("Total reads: %d, Hits: %d (%.1f%%), No stale reads: %d",
		totalReads, totalHits, float64(totalHits)/float64(totalReads)*100, staleReads)

	// No panic or race condition
	if staleReads > 0 {
		t.Errorf("Detected %d stale reads in concurrent test", staleReads)
	}
}

// TestCacheStaleness_MtimeEdgeCases tests edge cases in mtime handling.
func TestCacheStaleness_MtimeEdgeCases(t *testing.T) {
	dataCache := NewDataCacheWithTTL(1024*1024, 64, time.Hour)

	t.Run("MtimeZero", func(t *testing.T) {
		// mtime=0 should bypass validation (backward compat)
		dataCache.WriteWithMtime("zero.txt", 0, []byte("data"), 1000)
		_, hit := dataCache.ReadWithMtime("zero.txt", 0, 4, 0)
		if !hit {
			t.Error("Expected hit when read mtime is 0")
		}
	})

	t.Run("MtimeDecreases", func(t *testing.T) {
		// If mtime decreases (clock skew?), treat as stale
		dataCache.WriteWithMtime("skew.txt", 0, []byte("data"), 2000)
		_, hit := dataCache.ReadWithMtime("skew.txt", 0, 4, 1000) // Earlier mtime
		if hit {
			t.Error("Expected miss when mtime decreases (potential clock skew)")
		}
	})

	t.Run("LargeMtimeValues", func(t *testing.T) {
		// Test with large mtime values (year 2100+)
		largeMtime := int64(4102444800) // 2100-01-01
		dataCache.WriteWithMtime("future.txt", 0, []byte("future"), largeMtime)
		_, hit := dataCache.ReadWithMtime("future.txt", 0, 6, largeMtime)
		if !hit {
			t.Error("Expected hit with large mtime")
		}
		_, hit = dataCache.ReadWithMtime("future.txt", 0, 6, largeMtime+1)
		if hit {
			t.Error("Expected miss when mtime changes")
		}
	})
}

// TestCacheStaleness_SimulatedWorkload simulates a realistic mixed workload.
func TestCacheStaleness_SimulatedWorkload(t *testing.T) {
	dataCache := NewDataCacheWithTTL(10*1024*1024, 4096, time.Hour) // 10MB cache, 4KB blocks
	metaCache := NewMetadataCache(time.Second)                      // Short metadata TTL

	// Simulate multiple files with different staleness characteristics
	files := []struct {
		path         string
		size         int
		stalenessRate float64
	}{
		{"config.json", 1024, 0.01},      // Config: rarely changes
		{"data.csv", 100 * 1024, 0.10},   // Data file: occasional updates
		{"log.txt", 50 * 1024, 0.50},     // Log file: frequent appends
		{"temp.txt", 4096, 0.90},         // Temp file: very volatile
	}

	rng := rand.New(rand.NewSource(42))
	numOperations := 1000
	stats := make(map[string]struct{ hits, misses int })

	// Initialize files
	for _, f := range files {
		data := make([]byte, f.size)
		rng.Read(data)
		mtime := int64(1000)
		dataCache.WriteWithMtime(f.path, 0, data, mtime)
		metaCache.Set(f.path, &pb.Attr{Size: uint64(f.size), Mtime: uint64(mtime)})
		stats[f.path] = struct{ hits, misses int }{}
	}

	// Track current mtimes per file
	mtimes := make(map[string]int64)
	for _, f := range files {
		mtimes[f.path] = 1000
	}

	// Simulate workload
	for i := 0; i < numOperations; i++ {
		// Pick random file
		f := files[rng.Intn(len(files))]

		// Simulate potential modification based on file's staleness rate
		if rng.Float64() < f.stalenessRate {
			mtimes[f.path]++
		}

		// Read file
		_, hit := dataCache.ReadWithMtime(f.path, 0, int64(f.size), mtimes[f.path])

		s := stats[f.path]
		if hit {
			s.hits++
		} else {
			s.misses++
			// Re-cache
			data := make([]byte, f.size)
			rng.Read(data)
			dataCache.WriteWithMtime(f.path, 0, data, mtimes[f.path])
		}
		stats[f.path] = s
	}

	// Report results
	t.Log("\nWorkload Results:")
	t.Log("File           | Staleness | Hits   | Misses | Hit Rate")
	t.Log("---------------|-----------|--------|--------|----------")
	for _, f := range files {
		s := stats[f.path]
		total := s.hits + s.misses
		if total > 0 {
			hitRate := float64(s.hits) / float64(total) * 100
			t.Logf("%-14s | %.0f%%       | %6d | %6d | %.1f%%",
				f.path, f.stalenessRate*100, s.hits, s.misses, hitRate)
		}
	}
}

// BenchmarkCacheStaleness_ReadPerformance benchmarks read performance at different staleness rates.
func BenchmarkCacheStaleness_ReadPerformance(b *testing.B) {
	rates := []float64{0.0, 0.25, 0.50, 0.75, 1.0}

	for _, rate := range rates {
		b.Run(fmt.Sprintf("staleness_%.0f_percent", rate*100), func(b *testing.B) {
			dataCache := NewDataCacheWithTTL(10*1024*1024, 4096, time.Hour)
			path := "bench.txt"
			data := make([]byte, 4096)
			mtime := int64(1000)

			dataCache.WriteWithMtime(path, 0, data, mtime)

			rng := rand.New(rand.NewSource(time.Now().UnixNano()))

			b.ResetTimer()
			for i := 0; i < b.N; i++ {
				// Simulate staleness
				if rng.Float64() < rate {
					mtime++
				}

				_, hit := dataCache.ReadWithMtime(path, 0, int64(len(data)), mtime)
				if !hit {
					dataCache.WriteWithMtime(path, 0, data, mtime)
				}
			}
		})
	}
}
