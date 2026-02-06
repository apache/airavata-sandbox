# RemoteFS Performance Benchmark Report

**Date:** February 2026

## Executive Summary

RemoteFS provides high-performance remote file access with intelligent caching and 1-1 data consistency. Benchmarks across three environments demonstrate up to **60x faster** throughput than SCP, with the cache providing up to **75x** speedup over uncached mode for large files.

| Metric | Value | Environment |
|--------|-------|-------------|
| Peak speedup vs SCP | **60x** (128MB) | JetStream2 |
| Peak cache benefit | **75x** over no-cache | Gateway |
| Consistency guarantee | **100%** | All environments |
| SSHFS consistency at 50% staleness | **57%** (stale data!) | Local test |

---

## Test Environments

| Environment | Host | Description |
|-------------|------|-------------|
| SDSC Expanse | login02.expanse.sdsc.edu | HPC supercomputer |
| JetStream2 | nsworkshopcpuvc1.novalocal | Virtual HPC cluster |
| Gateway | dev-cs-portal | Linux 6.11 (passthrough support) |

**Configuration:** 3 iterations per configuration, file sizes 128KB–128MB, FRP NAT traversal

---

## Results

### SDSC Expanse (login02)

| File Size | SCP (MB/s) | SSHFS (MB/s) | RemoteFS Cached (MB/s) | RemoteFS No-cache (MB/s) | Cache Speedup |
|-----------|-----------|-------------|----------------------|------------------------|--------------|
| 128KB | 0.04 | 0.23 | **0.71** | 0.53 | 1.4x |
| 256KB | 0.08 | 0.37 | **1.47** | 1.08 | 1.4x |
| 1MB | 0.32 | 0.66 | **5.74** | 2.89 | 2.0x |
| 8MB | 2.11 | 0.85 | **38.70** | 5.54 | 7.0x |
| 32MB | 5.43 | 0.88 | **143.30** | 5.97 | 24.0x |
| 128MB | 7.78 | 0.88 | **446.16** | 6.14 | 72.7x |

**Key Finding:** RemoteFS cached delivers up to **57x faster** throughput than SCP for large files on SDSC Expanse. The cache provides a **72.7x** speedup over no-cache mode for 128MB files.

<img src="benchmark_login02.png" alt="Expanse" width="100%" style="max-width:720px;">

### JetStream2 (nsworkshopcpuvc1)

| File Size | SCP (MB/s) | SSHFS (MB/s) | RemoteFS Cached (MB/s) | RemoteFS No-cache (MB/s) | Cache Speedup |
|-----------|-----------|-------------|----------------------|------------------------|--------------|
| 128KB | 0.05 | 0.49 | **1.03** | 0.75 | 1.4x |
| 256KB | 0.12 | 0.75 | **2.09** | 1.49 | 1.4x |
| 1MB | 0.45 | 1.18 | **8.30** | 3.73 | 2.2x |
| 8MB | 2.28 | 1.39 | **55.86** | 6.54 | 8.5x |
| 32MB | 6.44 | 1.42 | **196.06** | 7.09 | 27.7x |
| 128MB | 9.27 | 1.42 | **555.89** | 7.04 | 79.0x |

**Key Finding:** RemoteFS cached is up to **60x faster** than SCP and **79x faster** than its own no-cache mode.

<img src="benchmark_nsworkshopcpuvc1.png" alt="JetStream2" width="100%" style="max-width:720px;">

### Gateway (dev-cs-portal) – FUSE Passthrough

| File Size | SCP (MB/s) | Cached (MB/s) | No-cache (MB/s) | Passthrough (MB/s) | Cache Speedup |
|-----------|-----------|-------------|---------------|------------------|--------------|
| 128KB | 0.28 | **1.36** | 0.88 | 1.30 | 1.5x |
| 256KB | 0.54 | **2.85** | 1.65 | 2.90 | 1.7x |
| 1MB | 1.90 | **11.03** | 3.66 | 10.16 | 3.0x |
| 8MB | 6.20 | **64.61** | 6.77 | 61.02 | 9.5x |
| 32MB | 9.34 | **174.00** | 7.13 | 159.62 | 24.4x |
| 128MB | 10.68 | **465.77** | 6.16 | 339.74 | 75.7x |

Passthrough mode (Linux kernel 6.9+) achieves ~73% of cached throughput for large files with lower memory usage.

<img src="benchmark_dev-cs-portal.png" alt="Gateway" width="100%" style="max-width:720px;">

---

## Cache Effectiveness

The cache benefit scales with file size. For small files (128KB), the cache provides a modest 1.4x speedup. For large files (128MB), the speedup is **72–79x** across all environments. This is because:

1. **First read** fills the block cache from the network (cold miss)
2. **Subsequent reads** serve directly from in-memory cache (warm hit), validated by mtime
3. **Data blocks stay cached for 5 minutes** while metadata is revalidated every 30 seconds

| File Size | Avg Cache Speedup (across environments) |
|-----------|----------------------------------------|
| 128KB | 1.4x |
| 1MB | 2.3x |
| 8MB | 8.3x |
| 32MB | 27.4x |
| 128MB | 75.8x |

---

## Cache Consistency Analysis

RemoteFS uses **mtime-based validation** to guarantee 1-1 data consistency. This is the key differentiator from SSHFS default caching.

### Staleness Test Methodology

We simulated different file modification rates (staleness) and measured:
- **Throughput**: How fast data is read (with simulated network latencies)
- **Consistency**: Whether the returned data matches the current file state

### Results: Consistency vs Staleness

| Staleness Rate | RemoteFS Consistency | SSHFS Default Consistency |
|----------------|---------------------|---------------------------|
| 0% (no changes) | **100%** | 100% |
| 25% | **100%** | 78% |
| 50% | **100%** | **57%** (stale!) |
| 75% | **100%** | 34% |
| 100% (always changing) | **100%** | **10%** (stale!) |

### Throughput Under Staleness

| Staleness Rate | RemoteFS (MB/s) | SSHFS Default (MB/s) | Direct (MB/s) | SSHFS No-cache (MB/s) |
|----------------|----------------|---------------------|--------------|---------------------|
| 0% | 727 | 1541 | 154 | 83 |
| 50% | 240 | 1474 | 153 | 85 |
| 100% | 148 | 1496 | 166 | 88 |

**Note:** SSHFS default is faster because it skips validation entirely, serving stale kernel-cached data. RemoteFS is slower but always correct. SSHFS no-cache is slowest due to per-read SSH overhead.

### Key Insights

1. **RemoteFS always returns fresh data** – Even with 100% file modification rate, consistency is 100%
2. **SSHFS default returns stale data** – At 50% staleness, nearly half of reads return outdated content
3. **RemoteFS cached is 4.7x faster than direct** at 0% staleness – The mtime-validated cache provides both speed and correctness
4. **SSHFS no-cache is consistent but slow** – Always 100% consistent but ~8x slower than RemoteFS cached

<img src="staleness_comparison.png" alt="Staleness Comparison" width="100%" style="max-width:720px;">

---

## Performance Characteristics

### When RemoteFS Excels
- **Repeated reads of the same files**: Up to 79x faster with cache
- **Small to medium files** (128KB–8MB): Up to 24x faster than SCP
- **Data consistency critical**: Always returns fresh data
- **HPC workflows**: Scientific data analysis where files are read multiple times

### When SCP/Direct Transfer is Comparable
- **One-time transfers of very large files** (>128MB): No cache benefit on first read
- **Write-heavy workloads**: Cache provides no benefit for writes

---

## Recommendations

| Use Case | Recommended Method |
|----------|-------------------|
| Interactive file browsing | RemoteFS (cached) |
| Scientific data analysis | RemoteFS (guaranteed fresh, high cache reuse) |
| One-time large transfers | SCP |
| Memory-constrained systems | RemoteFS (passthrough) |
| NAT/firewall traversal | RemoteFS with FRP |

---

## Technical Details

### Cache Configuration
- **Block size:** 256KB
- **Max cache size:** 256MB
- **Data cache TTL:** 5 minutes (blocks stay cached; mtime validation handles staleness)
- **Metadata TTL:** 30 seconds (triggers mtime revalidation from server)
- **Consistency model:** Mtime-validated (close-to-open semantics)

### Performance Optimizations
- **Separated data and metadata TTL**: Data blocks live for 5 minutes; metadata revalidates every 30 seconds via mtime checks.
- **Read-lock fast path**: Cache hits use a read lock only; write lock acquired only on miss or invalidation.
- **Fast-path mtime validation**: When both data and metadata caches agree on mtime, data is served without a network call.
- **Parallel block fetching**: Large reads fetch multiple blocks concurrently (configurable concurrency).

### How the Cache Maintains Consistency
1. On every read, the fast path checks if the **metadata cache** (30s TTL) confirms the file's mtime matches the **data cache**'s stored mtime.
2. If metadata has expired, the slow path fetches fresh mtime from the server.
3. If mtime differs, all cached blocks for that file are invalidated and re-fetched.
4. Data blocks remain cached for 5 minutes to amortize the cost of network fetches across multiple reads.

---

## Data Files

| File | Description |
|------|-------------|
| `results_expanse.csv` | SDSC Expanse benchmark data (3 iterations) |
| `results_vc-airavata-cpu.csv` | JetStream2 benchmark data (3 iterations) |
| `results_gateway.csv` | Gateway benchmark data (3 iterations) |
| `staleness_results.csv` | Cache consistency test data |
| `benchmark_*.png` | Performance visualizations |
| `staleness_comparison.png` | Consistency comparison chart |
