# RemoteFS Benchmarks

Performance benchmark suite comparing RemoteFS against SCP and SSHFS.

## Quick Start

```bash
# Set required environment variables
export FRP_SERVER="host:port:password"
export REMOTE_HOSTS="user@host1 user@host2"

# Run full benchmark suite
./scripts/run_benchmark.sh

# Run cache consistency (staleness) test (local, no remote hosts needed)
./scripts/run_staleness_benchmark.sh
```

## Configuration

| Variable | Required | Description |
|----------|----------|-------------|
| `FRP_SERVER` | Yes | FRP server for NAT traversal (`host:port:password`) |
| `REMOTE_HOSTS` | Yes | Space-separated `user@host` list |
| `ITERATIONS` | No | Iterations per test (default: 3) |
| `PASSTHROUGH_HOST_PATTERN` | No | Grep pattern for hosts that support FUSE passthrough (default: `gateway`) |
| `DATA_DIR` | No | Local test data directory (default: `/tmp/remotefs-benchmark-data`) |
| `RESULTS_DIR` | No | Results output directory (default: `benchmarks/results`) |

## Scripts

| Script | Purpose |
|--------|---------|
| `run_benchmark.sh` | Main orchestrator (generates data, builds, benchmarks all hosts, visualizes) |
| `run_remote_benchmark.sh` | RemoteFS benchmark runner (deployed to remote hosts automatically) |
| `run_staleness_benchmark.sh` | Cache consistency test (runs locally) |
| `generate_test_data.sh` | Create test files (128KB–128MB) |
| `visualize_results.py` | Generate throughput plots from CSV results |
| `visualize_staleness.py` | Generate consistency comparison plots |

## Test Cases

1. **SCP** – Transfer + read (baseline)
2. **SSHFS** – Reverse SSH tunnel mount with kernel page cache
3. **RemoteFS (cached)** – Block caching enabled (default)
4. **RemoteFS (no-cache)** – Direct fetch (`--no-cache`)
5. **RemoteFS (passthrough)** – Kernel passthrough (Linux 6.9+, passthrough hosts only)

## Results

See [results/BENCHMARK_REPORT.md](results/BENCHMARK_REPORT.md) for the latest benchmark report.
