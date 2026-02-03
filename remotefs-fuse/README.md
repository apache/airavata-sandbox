# remotefs

Publish a local directory over gRPC and mount it on a remote machine via FUSE. One binary: `remotefs publish` runs the server; `remotefs mount` runs the FUSE client (Linux only).

## Requirements

- **Go 1.21+** for building
- **protoc**, **protoc-gen-go**, **protoc-gen-go-grpc** for `make proto`
- **Linux with FUSE** for `mount` (publish runs on any OS)
- **Docker** optional, for integration tests

## Build

```bash
make proto
make build
```

Binary: `bin/remotefs`. Or install into `$GOPATH/bin`:

```bash
go install ./cmd/remotefs
```

## Usage

### Direct (tunnel the gRPC port yourself)

**Publish** (on the machine that has the folder):

```bash
remotefs publish /path/to/folder [--addr :50051]
```

**Mount** (on the remote machine; use SSH reverse tunnel or similar so the publish server is reachable):

```bash
remotefs mount /mountpoint --addr <host:port>
# Example: remotefs mount /mnt/remote -a localhost:50051
```

The mount shows the folder under `/mountpoint/<basename>/...` (e.g. `/mountpoint/data/...` if the folder is `data`).

### Via FRP (no direct network path)

Use an [FRP](https://github.com/fatedier/frp) server so publish and mount can connect without a direct tunnel.

**Publish** (register with FRP; prints a one-time token):

```bash
remotefs publish /path/to/folder --frp hostname:port:password
```

Example output: `Forwarding token: <id>:<secret>`. Copy that token.

**Mount** (on the other machine):

```bash
remotefs mount /mountpoint --token <id>:<secret> --frp hostname:port:password
```

FRP connection string is **hostname:port:password**. You can set `REMOTEFS_FRP` to that value instead of passing `--frp`. Default (if unset) is `149.165.172.97:17000:mysecret`.

### Mount options

- `-a, --addr` — gRPC server address (for direct mode)
- `--token` — forwarding token from publish (for FRP mode)
- `--frp` — FRP server connection string (for FRP mode)
- `--allow-other` — allow other users to access the mount (requires `user_allow_other` in `/etc/fuse.conf`)

## Docker

Compose runs a publish service and a remote (mount) service for integration tests:

```bash
docker compose -f compose.yml up -d
# After ~15–20s:
./scripts/verify.sh
```

Or `make verify` (builds, brings up stack, runs verify, updates `STATUS.md`). The remote container needs `SYS_ADMIN` and `/dev/fuse`; on macOS Docker may not provide FUSE.

## Tests

- Unit: `make unit-test` or `go test ./...`
- Integration: `make verify`

## Project layout

```
cmd/remotefs/     CLI (publish, mount)
internal/         export, fileproto, frpclient, mount, resolver, source
proto/            remotefs.proto and generated Go
scripts/          verify.sh, entrypoint-*.sh
compose.yml       Docker Compose (publish + remote)
Makefile          proto, build, unit-test, verify
```

## License

BSD-3-Clause. See [LICENSE](LICENSE).
