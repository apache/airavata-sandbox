# RemoteFS

A high-performance remote filesystem over gRPC with intelligent caching. Publish a local directory over gRPC and mount it on a remote machine via FUSE.

**One binary**: `remotefs publish` runs the server; `remotefs mount` runs the FUSE client (Linux only).

## Features

- **Block-level caching** with mtime-based consistency validation
- **Memory-mapped file cache** option for large workloads (Linux)
- **FUSE passthrough** support for kernel-level performance (Linux 6.9+)
- **FRP integration** for NAT traversal without direct network paths
- **100% data consistency** - never returns stale data

## Performance

Benchmarked against SCP and SSHFS across three HPC environments (SDSC Expanse, JetStream2, Cybershuttle Gateway) with 3 iterations per configuration:

| Metric | Result |
|--------|--------|
| Throughput vs SCP | Up to **60x** faster (128MB, warm cache) |
| Cache vs no-cache | Up to **79x** faster (128MB, repeated reads) |
| Data consistency | **100%** at all staleness rates (SSHFS default drops to 10%) |

See [benchmarks/results/BENCHMARK_REPORT.md](benchmarks/results/BENCHMARK_REPORT.md) for detailed results.

## Requirements

- **Go 1.24+** for building
- **Linux with FUSE** for `remotefs mount` (`remotefs publish` runs on any OS)
- **protoc** + Go plugins – only if editing `proto/remotefs.proto` (generated code is checked in)
- **Docker** – optional, for integration tests (`make verify`)

## Quick Start

### Build

```bash
make build
```

If you modify `proto/remotefs.proto`, regenerate the Go bindings:

```bash
make proto   # requires protoc, protoc-gen-go, protoc-gen-go-grpc
```

Binary: `bin/remotefs`. Or install into `$GOPATH/bin`:

```bash
go install ./cmd/remotefs
```

### Cross-compile for distribution

```bash
make build-all  # Creates binaries for darwin/linux amd64/arm64
```

## Usage

### Direct Mode (tunnel the gRPC port yourself)

**Publish** (on the machine that has the folder):

```bash
remotefs publish /path/to/folder [--addr :50051]
```

**Mount** (on the remote machine):

```bash
remotefs mount /mountpoint --addr <host:port>
# Example: remotefs mount /mnt/remote -a localhost:50051
```

### Via FRP (no direct network path)

Use an [FRP](https://github.com/fatedier/frp) server so publish and mount can connect without a direct tunnel.

**Publish** (register with FRP; prints a one-time token):

```bash
remotefs publish /path/to/folder --frp hostname:port:password
```

**Mount** (on the other machine):

```bash
remotefs mount /mountpoint --token <id>:<secret> --frp hostname:port:password
```

### Mount Options

| Flag | Description |
|------|-------------|
| `-a, --addr` | gRPC server address (for direct mode) |
| `--token` | Forwarding token from publish (for FRP mode) |
| `--frp` | FRP server connection string |
| `--allow-other` | Allow other users to access the mount |
| `--no-cache` | Disable caching entirely |
| `--cache-size` | Maximum cache size in MB (default: 256) |
| `--cache-block-size` | Cache block size in KB (default: 256) |
| `--cache-ttl` | Metadata/directory cache TTL in seconds (default: 30) |
| `--cache-dir` | Directory for file-backed cache |
| `--file-cache-size` | Maximum file cache size in MB (default: 1024) |
| `--mmap-cache` | Use memory-mapped file cache (requires `--cache-dir`) |
| `--passthrough` | Enable FUSE passthrough (Linux 6.9+, requires `--cache-dir`) |

### Caching Modes

1. **In-memory block cache** (default): Fast LRU cache in memory
2. **Memory-mapped file cache** (`--mmap-cache`): File-backed cache for large workloads
3. **FUSE passthrough** (`--passthrough`): Kernel-level caching with file backing

```bash
# Standard caching (in-memory)
remotefs mount /mnt/remote --addr localhost:50051

# Memory-mapped cache (for large files)
remotefs mount /mnt/remote --addr localhost:50051 --cache-dir /tmp/cache --mmap-cache

# FUSE passthrough (Linux 6.9+ only, best performance)
remotefs mount /mnt/remote --addr localhost:50051 --cache-dir /tmp/cache --passthrough
```

## Docker

Compose runs a publish service and a remote (mount) service for integration tests:

```bash
docker compose -f compose.yml up -d
# After ~15–20s:
./scripts/verify.sh
```

Or `make verify` (builds, brings up stack, runs verify, updates `STATUS.md`).

## Tests

```bash
# Unit tests
make unit-test

# Integration tests (requires Docker)
make verify

# Full test suite
make test
```

## Benchmarks

```bash
# Run full benchmark suite (generates data, builds, benchmarks, visualizes)
cd benchmarks/scripts
./run_benchmark.sh

# Run cache consistency (staleness) test
./run_staleness_benchmark.sh
```

See [benchmarks/README.md](benchmarks/README.md) for configuration and detailed documentation.

## Project Layout

```
cmd/remotefs/        CLI (publish, mount commands)
internal/
  cache/             Block cache, metadata cache, mmap cache
  export/            Export backend (server-side file operations)
  fileproto/         gRPC client wrapper
  frpclient/         FRP client integration
  mount/             FUSE mount implementation
  resolver/          Token/address resolution
  source/            Source server implementation
proto/               remotefs.proto and generated Go code
scripts/             verify.sh, entrypoint-*.sh
benchmarks/          Performance benchmark suite
  scripts/           Benchmark scripts
  results/           Benchmark results and reports
compose.yml          Docker Compose (publish + remote)
Makefile             proto, build, test, verify targets
```

## Cache Consistency

RemoteFS validates cached data using file modification times (mtime):

1. Metadata is revalidated from the server every 30 seconds (configurable via `--cache-ttl`)
2. On every read, the cached mtime is checked against the latest known metadata
3. If the source file changed, cached blocks are invalidated and re-fetched
4. Data blocks are cached for up to 5 minutes, but are never served without a recent mtime check

This ensures RemoteFS does not return stale data, unlike SSHFS default caching which can serve outdated content indefinitely.

## License

BSD-3-Clause. See [LICENSE](LICENSE).
