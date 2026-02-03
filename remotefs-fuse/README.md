# remotefs

Publish a local folder over gRPC and mount it on a remote machine via FUSE. Single binary: `remotefs publish` (server in-process) and `remotefs mount`.

## Prerequisites

- **Go 1.21+** (for building)
- **protoc** plus **protoc-gen-go** and **protoc-gen-go-grpc** (for `make proto`)
- **Docker** (optional; for integration tests)
- **Linux with FUSE** (for running the mount; publish works on any OS)

## Build

```bash
make proto   # generate Go from proto (required once)
make build   # builds bin/remotefs
```

Or install into `$GOPATH/bin`:

```bash
go install ./cmd/remotefs
```

## Usage

### 1. On your machine: publish a folder

```bash
remotefs publish /path/to/folder --addr :50051
# Or: remotefs publish -f /path/to/folder --addr :50051
```

This starts a gRPC server on `:50051` and serves the folder. Tunnel that port to the remote (e.g. SSH reverse tunnel, cloudflared); the tunnel is external to this tool.

### 2. On the remote machine: mount

```bash
remotefs mount -s <tunnel_endpoint> -m /target/dir
# Example: remotefs mount -s localhost:50051 -m /mnt/remote
```

`<tunnel_endpoint>` is where the publish server is reachable (e.g. after `ssh -R 50051:localhost:50051 user@remote`, use `localhost:50051` on the remote). The mounted tree appears at `/target/dir/<virtualname>/...` (virtual name is the folder’s base name).

## Docker integration

The repo includes a two-service setup: **publish** (gRPC server in-process) and **remote** (FUSE mount to publish). No separate coordinator or tunnel between them.

- **Files:** `compose.yml`, `Dockerfile`
- **Run:** from the repo root:

```bash
docker compose -f compose.yml up -d
# Wait for mount (~15–20s), then:
./scripts/verify.sh
```

Or use **`make verify`** (builds binary, builds images, brings up the stack, waits, then runs the verify script). Each run updates **`STATUS.md`** with the last run time, result (SUCCESS/FAILURE), and verify output for GitOps.

| Service  | Role |
|----------|------|
| **publish** | `remotefs publish -f /export/data --addr 0.0.0.0:50051` with `testdata/export` mounted; healthcheck on port 50051. |
| **remote**  | `remotefs mount -s publish:50051 -m /mnt/remote` (needs `SYS_ADMIN` and `/dev/fuse`). |

The verify script checks that the mount is ready, then runs file ops and checksum comparison between the publish folder and the remote mount.

**Note:** FUSE in the remote container requires `--device /dev/fuse` and `--cap-add SYS_ADMIN`. On macOS, Docker may not expose `/dev/fuse`; the script skips file-op checks in that case.

## Testing

- **Unit tests:** `make unit-test` or `go test ./...`
- **Integration (Docker):** `make verify` or `make test` (unit tests then Docker verify)

## Project layout

```
.
├── compose.yml          # Docker Compose: publish + remote
├── Dockerfile           # Multi-stage: Go build, Ubuntu 22.04 runtime
├── Makefile             # proto, build, unit-test, verify
├── cmd/remotefs/        # CLI: publish, mount
├── internal/             # export, fileproto, mount, resolver, source
├── proto/                # remotefs.proto, gen/
├── scripts/verify.sh    # Integration test (checksums + file ops)
├── STATUS.md            # Last verify result (updated by make verify; for GitOps)
└── testdata/export/      # Test data for Docker verify
```

## Protocol

- Publish runs a gRPC server that implements **ConnectSink** only (RegisterExport and ConnectSource return Unimplemented).
- Mount connects with **ConnectSink**; first message is `ConnectSinkRequest` (session_id unused). Then request/response **FileMessage** stream for file ops (GetAttr, Lookup, Open, Read, Write, ReadDir, etc.).

## License

BSD-style.
