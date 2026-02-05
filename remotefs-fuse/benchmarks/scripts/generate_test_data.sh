#!/bin/bash
# Generate test data files of various sizes for benchmarking
# Runs on local machine, creates files in /tmp/remotefs-benchmark-data
set -e

DATA_DIR="${DATA_DIR:-/tmp/remotefs-benchmark-data}"
SIZES="128K 256K 512K 1M 2M 8M 16M 32M 64M 128M"

echo "=== Generating test data in $DATA_DIR ==="

mkdir -p "$DATA_DIR"

for size in $SIZES; do
    dir="$DATA_DIR/$size"
    mkdir -p "$dir"
    file="$dir/file_${size}.bin"
    
    if [ -f "$file" ]; then
        echo "  $size: already exists, skipping"
        continue
    fi
    
    echo "  Generating $size file..."
    
    # Convert size to bytes for dd
    # Handle K, M suffixes
    case "$size" in
        *K) bytes=$((${size%K} * 1024)) ;;
        *M) bytes=$((${size%M} * 1024 * 1024)) ;;
        *)  bytes=$size ;;
    esac
    
    # Use dd with 1K block size for efficiency
    if [ "$bytes" -ge 1024 ]; then
        blocks=$((bytes / 1024))
        dd if=/dev/urandom of="$file" bs=1024 count=$blocks 2>/dev/null
    else
        dd if=/dev/urandom of="$file" bs=$bytes count=1 2>/dev/null
    fi
done

echo ""
echo "=== Test data summary ==="
for size in $SIZES; do
    file="$DATA_DIR/$size/file_${size}.bin"
    if [ -f "$file" ]; then
        actual_size=$(ls -lh "$file" | awk '{print $5}')
        echo "  $size: $actual_size"
    fi
done

echo ""
echo "Test data ready in $DATA_DIR"
echo "Total size: $(du -sh "$DATA_DIR" | cut -f1)"
