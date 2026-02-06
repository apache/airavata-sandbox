#!/bin/bash
# Remote benchmark runner - runs on remote hosts
# Executes RemoteFS benchmarks via FUSE mount
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Default values
ITERATIONS="${ITERATIONS:-3}"
OUTPUT="${OUTPUT:-results.csv}"
MOUNT_POINT="/tmp/remotefs-benchmark-mount"
SIZES="128K 256K 512K 1M 2M 8M 16M 32M 64M 128M"

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --token)
            TOKEN="$2"
            shift 2
            ;;
        --frp)
            FRP_SERVER="$2"
            shift 2
            ;;
        --iterations)
            ITERATIONS="$2"
            shift 2
            ;;
        --output)
            OUTPUT="$2"
            shift 2
            ;;
        --mount-point)
            MOUNT_POINT="$2"
            shift 2
            ;;
        --help)
            echo "Usage: run_remote_benchmark.sh [options]"
            echo ""
            echo "Options:"
            echo "  --token TOKEN       Forwarding token from publish --frp"
            echo "  --frp SERVER        FRP server connection (hostname:port:password)"
            echo "  --iterations N      Number of iterations (default: 3)"
            echo "  --output FILE       Output CSV file (default: results.csv)"
            echo "  --mount-point DIR   Mount point for remotefs"
            exit 0
            ;;
        *)
            echo "Unknown option: $1" >&2
            exit 1
            ;;
    esac
done

# Validate required arguments
if [ -z "$TOKEN" ] || [ -z "$FRP_SERVER" ]; then
    echo "Error: --token and --frp are required"
    exit 1
fi

# Function to get high-resolution time
get_time() {
    if command -v gdate &> /dev/null; then
        gdate +%s.%N
    else
        date +%s.%N
    fi
}

# Function to convert size string to bytes
size_to_bytes() {
    local size="$1"
    case "$size" in
        128K) echo 131072 ;;
        256K) echo 262144 ;;
        512K) echo 524288 ;;
        1M) echo 1048576 ;;
        2M) echo 2097152 ;;
        8M) echo 8388608 ;;
        16M) echo 16777216 ;;
        32M) echo 33554432 ;;
        64M) echo 67108864 ;;
        128M) echo 134217728 ;;
        *) echo 0 ;;
    esac
}

# Function to calculate throughput in MB/s
calc_throughput() {
    local bytes="$1"
    local duration="$2"
    if [ -n "$bytes" ] && [ "$bytes" -gt 0 ]; then
        echo "scale=2; $bytes / 1048576 / $duration" | bc 2>/dev/null || echo "0"
    else
        echo "0"
    fi
}

HOSTNAME=$(hostname)
echo "=== RemoteFS Benchmark Runner ==="
echo "Host: $HOSTNAME"
echo "Iterations: $ITERATIONS"
echo "Output: $OUTPUT"
echo ""

# Write CSV header
echo "timestamp,host,case,operation,file_size,iteration,duration_sec,throughput_mbps" > "$OUTPUT"

# ============================================================
# Mount RemoteFS
# ============================================================
echo "=== Mounting RemoteFS ==="
echo "Token: ${TOKEN:0:8}..."
echo "FRP Server: $FRP_SERVER"
echo "Mount point: $MOUNT_POINT"

# Ensure we use system fusermount for cleanup too
export PATH=/usr/local/bin:/usr/bin:/bin:$PATH

# Cleanup any existing mount
fusermount -u "$MOUNT_POINT" 2>/dev/null || true
mkdir -p "$MOUNT_POINT"

# Find remotefs binary
REMOTEFS_BIN="$HOME/remotefs"
if [ ! -x "$REMOTEFS_BIN" ]; then
    echo "Error: remotefs binary not found at $REMOTEFS_BIN" >&2
    exit 1
fi

echo "Using binary: $REMOTEFS_BIN"

# Ensure we use system fusermount (not user-installed versions without setuid)
# This is needed for HPC systems like Expanse where ~/.local/bin may have a non-setuid fusermount
export PATH=/usr/local/bin:/usr/bin:/bin:$PATH

# Start mount in background
"$REMOTEFS_BIN" mount "$MOUNT_POINT" --token "$TOKEN" --frp "$FRP_SERVER" &
MOUNT_PID=$!

# Wait for mount to be ready
echo "Waiting for mount to be ready..."
MOUNT_READY=0
for i in $(seq 1 60); do
    if ls "$MOUNT_POINT" 2>/dev/null | grep -q .; then
        MOUNT_READY=1
        break
    fi
    sleep 1
done

if [ "$MOUNT_READY" -eq 0 ]; then
    echo "Error: Mount not ready after 60 seconds" >&2
    kill $MOUNT_PID 2>/dev/null || true
    exit 1
fi

# Find the virtual name (basename of published directory)
VIRTUAL_NAME=$(ls "$MOUNT_POINT" | head -1)
FUSE_DATA="$MOUNT_POINT/$VIRTUAL_NAME"

echo "Mounted at: $FUSE_DATA"
echo ""

# ============================================================
# Run RemoteFS Benchmarks
# ============================================================
echo "=== Running RemoteFS Benchmarks ==="

for iter in $(seq 1 $ITERATIONS); do
    echo "  Iteration $iter/$ITERATIONS (RemoteFS)"
    ts=$(date -u +%Y-%m-%dT%H:%M:%SZ)
    
    for size in $SIZES; do
        file="$FUSE_DATA/$size/file_${size}.bin"
        file_bytes=$(size_to_bytes "$size")
        
        if [ ! -f "$file" ]; then
            echo "    Warning: File $file not found, skipping" >&2
            continue
        fi
        
        # Time the read operation
        start=$(get_time)
        cat "$file" > /dev/null
        end=$(get_time)
        
        duration=$(echo "$end - $start" | bc)
        throughput=$(calc_throughput "$file_bytes" "$duration")
        echo "$ts,$HOSTNAME,remotefs,cat,$size,$iter,$duration,$throughput" >> "$OUTPUT"
    done
done

# ============================================================
# Cleanup
# ============================================================
echo ""
echo "=== Cleanup ==="
echo "Unmounting..."
kill $MOUNT_PID 2>/dev/null || true
sleep 2
fusermount -u "$MOUNT_POINT" 2>/dev/null || true

echo ""
echo "=== Benchmark Complete ==="
echo "Results written to: $OUTPUT"
echo "Total lines: $(wc -l < "$OUTPUT")"
