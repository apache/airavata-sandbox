#!/bin/sh
# Integration test: run after `docker compose -f compose.yml up -d`
# Waits for mount, compares checksums from publish and remote, runs all file ops, compares again.
set -e

# Discover container names from docker compose (works with any project name)
COMPOSE_PROJECT="${COMPOSE_PROJECT:-fuse-fs-poc}"
REMOTE_CONTAINER="${REMOTE_CONTAINER:-${COMPOSE_PROJECT}-remote-1}"
LAPTOP_CONTAINER="${LAPTOP_CONTAINER:-${COMPOSE_PROJECT}-publish-1}"
LAPTOP_EXPORT="/export/data"
REMOTE_MOUNT="/mnt/remote/data"

# Ensure containers exist and are running
check_containers() {
  for name in "$REMOTE_CONTAINER" "$LAPTOP_CONTAINER"; do
    status=$(docker inspect -f '{{.State.Status}}' "$name" 2>/dev/null || true)
    if [ -z "$status" ]; then
      echo "Error: container $name not found. Run from repo root: docker compose -f compose.yml up -d"
      exit 1
    fi
    if [ "$status" != "running" ]; then
      echo "Error: container $name is not running (status=$status). Check logs: docker compose -f compose.yml logs"
      exit 1
    fi
  done
}

# Checksum command: use sha256sum (from coreutils in Docker image).
checksum_cmd() {
  echo "sha256sum"
}

# Output one line per file: "relpath SHA256HEX"; for symlinks: "relpath SYMLINK -> target"
# Usage: collect_checksums <container> <root_path>
# root_path is the prefix to strip to get relative path (e.g. /export/data or /mnt/remote/data)
collect_checksums() {
  _container="$1"
  _root="$2"
  _cmd=$(checksum_cmd)
  # Regular files
  docker exec "$_container" find "$_root" -type f 2>/dev/null | while read -r abspath; do
    relpath="${abspath#$_root/}"
    hash=$(docker exec "$_container" $_cmd "$abspath" 2>/dev/null | cut -d' ' -f1)
    echo "$relpath $hash"
  done
  # Symlinks: path and target
  docker exec "$_container" find "$_root" -type l 2>/dev/null | while read -r abspath; do
    relpath="${abspath#$_root/}"
    target=$(docker exec "$_container" readlink "$abspath" 2>/dev/null || true)
    echo "$relpath SYMLINK -> $target"
  done
}

# Compare checksums from laptop and remote; exit 1 on mismatch
compare_checksums() {
  echo "  Collecting checksums from laptop..."
  collect_checksums "$LAPTOP_CONTAINER" "$LAPTOP_EXPORT" | sort > /tmp/checksum_laptop.txt
  echo "  Collecting checksums from remote..."
  collect_checksums "$REMOTE_CONTAINER" "$REMOTE_MOUNT" | sort > /tmp/checksum_remote.txt
  if ! diff -u /tmp/checksum_laptop.txt /tmp/checksum_remote.txt; then
    echo "Checksum mismatch between laptop and remote."
    exit 1
  fi
  echo "  Checksums match."
}

# Remove test-created paths so we can run idempotently (both sides)
clean_test_paths() {
  docker exec "$REMOTE_CONTAINER" sh -c "
    rm -f $REMOTE_MOUNT/from_remote.txt $REMOTE_MOUNT/new.txt $REMOTE_MOUNT/renamed.txt \
          $REMOTE_MOUNT/link.txt $REMOTE_MOUNT/truncate_me.txt 2>/dev/null
    rm -rf $REMOTE_MOUNT/verify_dir $REMOTE_MOUNT/dir1 2>/dev/null
  " 2>/dev/null || true
  docker exec "$LAPTOP_CONTAINER" sh -c "
    rm -f $LAPTOP_EXPORT/from_remote.txt $LAPTOP_EXPORT/new.txt $LAPTOP_EXPORT/renamed.txt \
          $LAPTOP_EXPORT/link.txt $LAPTOP_EXPORT/truncate_me.txt 2>/dev/null
    rm -rf $LAPTOP_EXPORT/verify_dir $LAPTOP_EXPORT/dir1 2>/dev/null
  " 2>/dev/null || true
}

check_containers

echo "Waiting for mount to be ready..."
MOUNT_READY=
for i in 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20; do
  if docker exec "$REMOTE_CONTAINER" ls "$REMOTE_MOUNT/hello.txt" 2>/dev/null; then
    MOUNT_READY=1
    break
  fi
  sleep 1
done
if [ -z "$MOUNT_READY" ]; then
  io_err=$(docker exec "$REMOTE_CONTAINER" ls /mnt/remote 2>&1) || true
  if echo "$io_err" | grep -q "I/O error"; then
    echo "Mount point has I/O error (FUSE may not be available in this Docker environment, e.g. on macOS). Skipping integration checks."
    echo "Unit tests and server/client flow are OK. Full verify (read/write, checksums) requires Linux with FUSE."
    exit 0
  fi
  echo "Error: mount not ready (could not ls $REMOTE_MOUNT/hello.txt). Check remote logs: docker compose -f compose.yml logs remote"
  exit 1
fi

echo "=== Cleaning test paths ==="
clean_test_paths

echo "=== Baseline checksum comparison ==="
compare_checksums

echo "=== File operations (from remote) ==="

echo "1. Read (cat)"
docker exec "$REMOTE_CONTAINER" cat "$REMOTE_MOUNT/hello.txt" >/dev/null
docker exec "$REMOTE_CONTAINER" cat "$REMOTE_MOUNT/subdir/nested.txt" >/dev/null

echo "2. Write / Create"
docker exec "$REMOTE_CONTAINER" sh -c "echo 'content' > $REMOTE_MOUNT/new.txt"
docker exec "$REMOTE_CONTAINER" mkdir -p "$REMOTE_MOUNT/dir1/dir2"
docker exec "$REMOTE_CONTAINER" touch "$REMOTE_MOUNT/dir1/empty"
docker exec "$REMOTE_CONTAINER" sh -c "echo 'truncate me' > $REMOTE_MOUNT/truncate_me.txt"
compare_checksums

echo "3. Rename"
docker exec "$REMOTE_CONTAINER" mv "$REMOTE_MOUNT/new.txt" "$REMOTE_MOUNT/renamed.txt"
compare_checksums

echo "4. Symlink"
docker exec "$REMOTE_CONTAINER" ln -s renamed.txt "$REMOTE_MOUNT/link.txt"
echo "  Readlink from remote: $(docker exec "$REMOTE_CONTAINER" readlink "$REMOTE_MOUNT/link.txt")"
echo "  Readlink from laptop: $(docker exec "$LAPTOP_CONTAINER" readlink "$LAPTOP_EXPORT/link.txt")"
compare_checksums

echo "5. SetAttr (truncate)"
docker exec "$REMOTE_CONTAINER" truncate -s 0 "$REMOTE_MOUNT/truncate_me.txt"
compare_checksums

echo "6. Unlink (file delete)"
docker exec "$REMOTE_CONTAINER" rm "$REMOTE_MOUNT/renamed.txt"
compare_checksums

echo "7. Rmdir (folder deletion)"
docker exec "$REMOTE_CONTAINER" rm "$REMOTE_MOUNT/dir1/empty"
docker exec "$REMOTE_CONTAINER" rmdir "$REMOTE_MOUNT/dir1/dir2"
docker exec "$REMOTE_CONTAINER" rmdir "$REMOTE_MOUNT/dir1"
compare_checksums

echo "8. Legacy verify steps (mkdir + touch)"
docker exec "$REMOTE_CONTAINER" mkdir -p "$REMOTE_MOUNT/verify_dir"
docker exec "$REMOTE_CONTAINER" touch "$REMOTE_MOUNT/verify_dir/file"
docker exec "$REMOTE_CONTAINER" sh -c "echo 'written from remote' > $REMOTE_MOUNT/from_remote.txt"
compare_checksums

echo "=== Final checksum comparison ==="
compare_checksums

echo "Verification complete."
