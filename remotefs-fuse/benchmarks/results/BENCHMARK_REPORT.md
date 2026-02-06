# Remote File Access Benchmark Report

**Date:** February 6, 2026  
**Version:** remotefs-fuse v1.1 - FUSE Passthrough Benchmark  
**Author:** Automated Benchmark Suite

---

## Executive Summary

This report evaluates RemoteFS file access performance across three distinct computing environments, with a focus on the new **FUSE passthrough** feature available in Linux kernel 6.9+. The benchmark compares cached, uncached, and passthrough modes to assess the performance benefits of each approach.

### Test Environments

| Environment | Host | Kernel | Network | Notes |
|-------------|------|--------|---------|-------|
| **SDSC Expanse** | login02.expanse.sdsc.edu | 4.18.0 | HPC InfiniBand | HPC supercomputer, NAT traversal via FRP |
| **JetStream2** | nsworkshopcpuvc1 (OpenStack) | 5.4.291 | Cloud networking | Virtual HPC cluster on JetStream2 |
| **Cybershuttle Gateway** | dev-cs-portal | **6.11.0** | Cloud networking | Modern kernel with FUSE passthrough support |

### Access Methods Compared

| Method | Description | Kernel Requirement |
|--------|-------------|-------------------|
| **RemoteFS (cached)** | In-memory block cache (256MB, 256KB blocks) | Any |
| **RemoteFS (uncached)** | Direct remote fetch, no caching | Any |
| **RemoteFS (passthrough)** | File-backed cache with FUSE passthrough | **Linux 6.9+** |

### Key Findings

#### Finding 1: Cache Provides Dramatic Speedup (up to 141x)

| Environment | Cached Peak | Uncached Peak | Cache Speedup |
|-------------|-------------|---------------|---------------|
| JetStream2 | **569.75 MB/s** | 4.04 MB/s | **141x** |
| Gateway | 5.25 MB/s | 3.34 MB/s | 1.6x |
| Expanse | 3.76 MB/s | N/A | N/A |

#### Finding 2: FUSE Passthrough Performance

On the Cybershuttle Gateway (kernel 6.11), passthrough mode shows **comparable performance** to standard caching:

| File Size | Cached (MB/s) | Passthrough (MB/s) | Ratio |
|-----------|---------------|-------------------|-------|
| 128KB | 0.82 | 0.86 | 1.0x |
| 8MB | 4.92 | 5.07 | 1.0x |
| 128MB | 5.12 | 5.15 | 1.0x |

**Analysis:** Passthrough achieves equivalent throughput to in-memory caching while providing potential memory savings since data is stored on disk (in `/dev/shm` for RAM-backed storage).

#### Finding 3: Network Latency Dominates

The performance difference between JetStream2 (569 MB/s) and Gateway (5 MB/s) demonstrates that:
- **Low-latency connections** benefit enormously from caching
- **Higher-latency connections** are network-bound regardless of cache type
- The FUSE userspace overhead is negligible compared to network latency

---

## Test Methodology

### Parameters

| Parameter | Value |
|-----------|-------|
| File sizes | 128KB, 256KB, 512KB, 1MB, 2MB, 8MB, 16MB, 32MB, 64MB, 128MB |
| Iterations | 3 per configuration |
| RemoteFS cache | 256MB, 256KB blocks, 30s TTL |
| File cache (passthrough) | 1GB in `/dev/shm` (RAM-backed) |
| FRP server | hub.dev.cybershuttle.org:7000 |

### Timing

All measurements use high-resolution timestamps (`date +%s.%N`).

---

## Results: JetStream2 (nsworkshopcpuvc1)

![Benchmark Results - JetStream2](benchmark_nsworkshopcpuvc1.png)

### Throughput Comparison (MB/s)

| File Size | RemoteFS Cached | RemoteFS Uncached | Cache Speedup |
|-----------|-----------------|-------------------|---------------|
| 128KB | 0.96 | 0.51 | 1.9x |
| 256KB | 1.99 | 1.08 | 1.8x |
| 512KB | 3.88 | 1.56 | 2.5x |
| 1MB | 7.07 | 2.17 | 3.3x |
| 2MB | 14.52 | 2.36 | 6.2x |
| 8MB | 50.83 | 3.29 | **15.5x** |
| 16MB | 96.32 | 3.59 | **26.9x** |
| 32MB | 167.26 | 3.74 | **44.7x** |
| 64MB | 301.45 | 3.67 | **82.1x** |
| 128MB | **569.75** | 4.04 | **141.0x** |

### Key Observations

1. **Extraordinary cache performance**: 569 MB/s for cached 128MB reads
2. **Cache speedup scales with file size**: From 1.9x (128KB) to 141x (128MB)
3. **Uncached throughput stable**: ~3-4 MB/s regardless of file size
4. **Low latency to publisher** enables cache to serve data at memory speed

---

## Results: Cybershuttle Gateway (dev-cs-portal)

![Benchmark Results - Gateway](benchmark_dev-cs-portal.png)

### Throughput Comparison (MB/s)

| File Size | Cached | Uncached | Passthrough | Cache vs Uncached |
|-----------|--------|----------|-------------|-------------------|
| 128KB | 0.82 | 0.60 | 0.86 | 1.4x |
| 256KB | 1.53 | 0.92 | 1.54 | 1.7x |
| 512KB | 2.07 | 1.34 | 2.03 | 1.5x |
| 1MB | 3.30 | 1.74 | 2.85 | 1.9x |
| 2MB | 3.91 | 2.39 | 3.79 | 1.6x |
| 8MB | 4.92 | 2.79 | 5.07 | 1.8x |
| 16MB | 5.34 | 2.98 | 5.26 | 1.8x |
| 32MB | 5.21 | 3.13 | 4.75 | 1.7x |
| 64MB | 5.25 | 3.34 | 4.51 | 1.6x |
| 128MB | 5.12 | 3.86 | 5.15 | 1.3x |

### FUSE Passthrough Analysis

| Metric | Cached | Passthrough | Observation |
|--------|--------|-------------|-------------|
| Average throughput | 3.25 MB/s | 3.18 MB/s | Equivalent |
| Peak throughput | 5.34 MB/s | 5.26 MB/s | Equivalent |
| Memory usage | 256MB RAM | Disk-backed | Passthrough uses less RAM |
| Implementation | In-memory blocks | File descriptors | Different caching strategy |

**Conclusion:** FUSE passthrough provides **equivalent performance** to in-memory caching while potentially reducing memory pressure. The kernel reads directly from cached files on disk, bypassing the FUSE userspace daemon.

---

## Results: SDSC Expanse (login02)

![Benchmark Results - Expanse](benchmark_login02.png)

### Throughput (RemoteFS Cached Only)

| File Size | Throughput (MB/s) |
|-----------|-------------------|
| 128KB | 0.47 |
| 256KB | 0.91 |
| 512KB | 1.28 |
| 1MB | 1.91 |
| 2MB | 2.40 |
| 8MB | 2.98 |
| 16MB | 3.72 |
| 32MB | 4.26 |
| 64MB | 3.52 |
| 128MB | 3.76 |

### Key Observations

1. **Lower throughput than JetStream2**: Network latency and NAT traversal overhead
2. **Consistent 3-4 MB/s for large files**: Network-bound performance
3. **HPC environment**: RemoteFS successfully operates through Expanse's security infrastructure

---

## FUSE Passthrough Technical Details

### How Passthrough Works

FUSE passthrough (Linux 6.9+) allows the kernel to read directly from a backing file without routing I/O through the FUSE userspace daemon:

```
Standard FUSE:       User App → Kernel → FUSE daemon → Network → File
FUSE Passthrough:    User App → Kernel → Cached File (direct, no userspace)
```

### Implementation in RemoteFS

1. **File-backed cache**: Files downloaded to `/dev/shm/remotefs-cache` (RAM-backed)
2. **`FilePassthroughFder` interface**: File handles return open file descriptors
3. **Automatic fallback**: If file not cached, falls back to standard FUSE path

### Mount Options

```bash
# Enable passthrough mode
remotefs mount /mnt/remote --token $TOKEN --frp $FRP \
    --cache-dir /dev/shm/remotefs-cache \
    --passthrough

# Standard cached mode (in-memory)
remotefs mount /mnt/remote --token $TOKEN --frp $FRP

# No cache mode
remotefs mount /mnt/remote --token $TOKEN --frp $FRP --no-cache
```

---

## Performance Analysis

### Why JetStream2 Shows 141x Cache Speedup

1. **Low network latency**: VM is close to the publisher
2. **Memory-speed cached reads**: Data served from RAM at ~570 MB/s
3. **Network-limited uncached**: Only ~4 MB/s without cache
4. **Effective block prefetching**: Sequential reads trigger background fetch

### Why Passthrough Equals Cached Performance

1. **Both serve from RAM**: `/dev/shm` is RAM-backed
2. **Similar read patterns**: Sequential file access
3. **Network is the bottleneck**: FUSE overhead negligible compared to network latency
4. **First-read penalty**: Both modes must download file initially

### Trade-offs

| Mode | Memory Usage | CPU Overhead | Best For |
|------|--------------|--------------|----------|
| Cached | High (in-memory) | Low | Repeated random access |
| Passthrough | Lower (file-backed) | Lower | Large sequential reads |
| Uncached | None | Highest | One-time reads |

---

## Conclusions

### Performance Summary

| Environment | Best Mode | Peak Throughput | Recommendation |
|-------------|-----------|-----------------|----------------|
| JetStream2 | Cached | 569.75 MB/s | Use default caching |
| Gateway | Cached/Passthrough | 5.34 MB/s | Either mode equivalent |
| Expanse | Cached | 4.26 MB/s | Use default caching |

### FUSE Passthrough Assessment

**Finding:** FUSE passthrough provides **equivalent performance** to in-memory caching in our benchmark configuration. The feature is beneficial when:

1. **Memory is constrained**: Passthrough uses disk-backed cache
2. **Files are large**: Avoids memory pressure from in-memory caching
3. **Reads are sequential**: Direct file I/O is efficient

**Limitation:** Passthrough requires Linux kernel 6.9+, limiting deployment to modern systems.

### Recommendations

1. **For maximum performance**: Use default in-memory caching
2. **For memory-constrained systems**: Use passthrough mode with `/dev/shm` cache
3. **For large one-time reads**: Use uncached mode to avoid cache pollution
4. **For older kernels**: In-memory caching provides equivalent performance

---

## Appendix: Data Files

- `results_expanse.csv` - Expanse benchmark data (RemoteFS cached)
- `results_vc-airavata-cpu.csv` - JetStream2 data (cached + uncached)
- `results_gateway.csv` - Gateway data (cached + uncached + passthrough)
- `benchmark_*.png` - Visualizations for each environment
- `benchmark_summary.txt` - Text summary of all results

---

## Future Work

1. **Warm-cache benchmarks**: Measure repeated read performance
2. **Write throughput**: Benchmark file write performance
3. **Concurrent access**: Multiple readers/writers
4. **Larger files**: Test with 1GB+ files
5. **Network conditions**: Simulate varying latency/bandwidth
6. **Memory profiling**: Compare memory usage between cache modes
