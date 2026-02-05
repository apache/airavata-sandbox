# RemoteFS-FUSE Performance Benchmarks

Performance benchmark suite comparing remotefs-fuse against SCP+local file operations.

## Overview

This benchmark suite measures the performance of two approaches for accessing remote files:

- **Case A (SCP Baseline)**: Copy files via `scp` from local to remote, then run operations locally
- **Case B (RemoteFS)**: Mount files via remotefs-fuse with FRP, run operations on FUSE mount

## Test Configuration

- **Publisher (Source)**: Local machine (macOS)
- **Remote Hosts**: 
  - `scigap@expanse`
  - `exouser@vc-airavata-cpu`
- **FRP Server**: `hub.dev.cybershuttle.org:7000:mysecret` (default)
- **File Sizes**: 128K, 256K, 512K, 1M, 2M, 8M, 16M, 32M, 64M, 128M

## Measurements

- **Metadata operations**: `ls -la` (stat calls, directory listing)
- **Data operations**: `cat` (file reads)
- **Cache states**: 
  - **SCP Cold**: Time to `scp` file from source + read locally (simulates on-demand retrieval)
  - **SCP Cached**: Time to read file already present locally
  - **RemoteFS Cold**: Time to read file via FUSE (includes network transfer)
  - **RemoteFS Repeat**: Time to re-read file via FUSE

## Methodology

### Fair Comparison: On-Demand Retrieval

Both SCP and RemoteFS are measured for **on-demand file retrieval**:

1. **SCP Cold**: For each file, we:
   - Delete any existing local copy on the remote host
   - Time the `scp` transfer from source to remote
   - Time the local read of the transferred file
   - Total = transfer time + read time

2. **RemoteFS Cold**: For each file, we:
   - Time the `cat` operation through the FUSE mount
   - This includes network transfer since data is fetched on-demand

This ensures an apples-to-apples comparison: both cases measure the time from "user requests file" to "file data is accessible".

## Prerequisites

1. SSH key-based access to remote hosts (no password prompts)
2. Go 1.21+ for building remotefs
3. FUSE available on remote hosts
4. FRP server accessible from all hosts
5. `bc` command available on remote hosts

## Quick Start

```bash
# From remotefs-fuse root directory
cd benchmarks

# 1. Generate test data locally
./scripts/generate_test_data.sh

# 2. Run full benchmark suite (default: 10 iterations per host)
./scripts/run_benchmark.sh

# 3. View results
./scripts/analyze_results.sh results/*.csv
```

## Scripts

| Script | Description | Runs On |
|--------|-------------|---------|
| `generate_test_data.sh` | Generate test files (128K-128M) | Local |
| `publish_local.sh` | Start remotefs publish with FRP | Local |
| `run_benchmark.sh` | Main orchestrator | Local |
| `run_remote_benchmark.sh` | Execute benchmarks | Remote hosts |
| `benchmark_ls.sh` | Measure ls performance | Remote hosts |
| `benchmark_cat.sh` | Measure cat performance | Remote hosts |
| `analyze_results.sh` | Analyze and summarize results | Local |

## Configuration

Environment variables:

```bash
# Data directory (default: /tmp/remotefs-benchmark-data)
DATA_DIR=/path/to/data ./scripts/run_benchmark.sh

# Number of iterations (default: 10)
ITERATIONS=5 ./scripts/run_benchmark.sh

# FRP server (default: hub.dev.cybershuttle.org:7000:mysecret)
FRP_SERVER=host:port:password ./scripts/run_benchmark.sh

# Remote hosts (space-separated)
REMOTE_HOSTS="user1@host1 user2@host2" ./scripts/run_benchmark.sh

# Results directory (default: benchmarks/results)
RESULTS_DIR=/path/to/results ./scripts/run_benchmark.sh
```

## Manual Testing

For debugging or testing individual components:

### Local: Start Publisher

```bash
# Generate test data first
./scripts/generate_test_data.sh

# Start publisher (note the token output)
./scripts/publish_local.sh
# Output: Forwarding token: abc123:def456
```

### Remote: Run Benchmarks

```bash
# SSH to remote host
ssh scigap@expanse

# Copy remotefs binary (from local)
# scp bin/remotefs-linux-amd64 scigap@expanse:~/remotefs

# Copy benchmark scripts (from local)
# scp benchmarks/scripts/*.sh scigap@expanse:~/

# Run benchmarks
~/run_remote_benchmark.sh \
    --token "abc123:def456" \
    --frp "hub.dev.cybershuttle.org:7000:mysecret" \
    --iterations 5 \
    --output results.csv
```

## Output Format

CSV with columns:

```csv
timestamp,host,case,operation,file_size,iteration,cache_state,duration_sec,throughput_mbps
2026-02-05T10:00:00Z,expanse,scp,cat,128K,1,cold,0.125,1.0       # SCP transfer + read
2026-02-05T10:00:01Z,expanse,scp,cat,128K,1,cached,0.002,62.5    # Local read only
2026-02-05T10:00:02Z,expanse,remotefs,cat,128K,1,cold,0.145,0.86 # FUSE read (includes network)
2026-02-05T10:00:03Z,expanse,remotefs,cat,128K,1,repeat,0.140,0.89 # FUSE re-read
```

### Cache State Meanings

| Case | Cache State | Meaning |
|------|-------------|---------|
| scp | cold | `scp` transfer from source + local `cat` read |
| scp | cached | Local `cat` read only (file already present) |
| remotefs | cold/first | First FUSE read (includes network transfer) |
| remotefs | repeat | Subsequent FUSE read (may benefit from caching) |

## Analysis Output

The `analyze_results.sh` script provides:

1. **Summary by Host**: Number of measurements per host
2. **Mean Duration by Case/Operation**: SCP vs RemoteFS comparison
3. **Duration by File Size**: Detailed breakdown for cat operations
4. **Throughput by File Size**: MB/s for each file size
5. **Performance Ratio**: RemoteFS time / SCP time (>1 means slower)
6. **ls Operations Summary**: Metadata operation times

## Directory Structure

```
benchmarks/
├── scripts/
│   ├── generate_test_data.sh    # Generate test files
│   ├── publish_local.sh         # Start remotefs publish
│   ├── run_benchmark.sh         # Main orchestrator
│   ├── run_remote_benchmark.sh  # Remote benchmark runner
│   ├── benchmark_ls.sh          # ls benchmarks
│   ├── benchmark_cat.sh         # cat benchmarks
│   └── analyze_results.sh       # Results analysis
├── results/
│   └── .gitkeep                 # Results directory
└── README.md                    # This file
```

## Troubleshooting

### SSH Connection Issues

```bash
# Test SSH connectivity
ssh -o ConnectTimeout=10 scigap@expanse "echo ok"

# Ensure key-based auth works
ssh -o BatchMode=yes scigap@expanse "echo ok"
```

### Mount Issues

```bash
# Check if FUSE is available
ls /dev/fuse

# Check if fusermount is available
which fusermount

# Manually unmount if stuck
fusermount -u /tmp/remotefs-benchmark-mount
```

### FRP Connection Issues

```bash
# Test FRP server reachability
nc -zv 149.165.172.97 17000

# Check remotefs publish output for errors
./scripts/publish_local.sh
```

### Cache Dropping

Cache dropping requires root access. If `sudo` requires a password, benchmarks will still run but cold/warm cache measurements may not be accurate:

```bash
# Test if cache dropping works
sudo sh -c 'echo 3 > /proc/sys/vm/drop_caches'
```
