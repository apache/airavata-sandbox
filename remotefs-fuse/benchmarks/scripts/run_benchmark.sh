#!/bin/bash
# Main benchmark orchestrator - runs on local machine
# Compares SCP vs SSHFS vs RemoteFS performance

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_ROOT="$SCRIPT_DIR/../.."
DATA_DIR="${DATA_DIR:-/tmp/remotefs-benchmark-data}"
RESULTS_DIR="${RESULTS_DIR:-$SCRIPT_DIR/../results}"
ITERATIONS="${ITERATIONS:-3}"
FRP_SERVER="${FRP_SERVER:?Set FRP_SERVER=host:port:password}"
SIZES="128K 256K 512K 1M 2M 8M 16M 32M 64M 128M"

# Remote hosts to benchmark (space-separated user@host list).
# Hosts matching PASSTHROUGH_HOST_PATTERN will also run the passthrough test case.
REMOTE_HOSTS="${REMOTE_HOSTS:?Set REMOTE_HOSTS='user@host1 user@host2 ...'}"
PASSTHROUGH_HOST_PATTERN="${PASSTHROUGH_HOST_PATTERN:-gateway}"

# Local SSH settings for SSHFS reverse tunnel
LOCAL_USER="${LOCAL_USER:-$(whoami)}"
LOCAL_SSH_PORT="${LOCAL_SSH_PORT:-22}"

# Convert to array
read -ra HOSTS <<< "$REMOTE_HOSTS"

echo "=============================================="
echo "  RemoteFS Performance Benchmark Suite"
echo "=============================================="
echo ""
echo "Configuration:"
echo "  Data directory: $DATA_DIR"
echo "  Results directory: $RESULTS_DIR"
echo "  Iterations: $ITERATIONS"
echo "  FRP server: $FRP_SERVER"
echo "  Remote hosts: ${HOSTS[*]}"
echo ""

# Create results directory
mkdir -p "$RESULTS_DIR"

# Function to get high-resolution time (works on macOS and Linux)
get_time() {
    if command -v gdate &> /dev/null; then
        gdate +%s.%N
    elif date +%s.%N 2>/dev/null | grep -q '\.' ; then
        date +%s.%N
    else
        python3 -c 'import time; print(f"{time.time():.9f}")'
    fi
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

# ============================================================
# Step 1: Generate test data
# ============================================================
echo "=== Step 1: Generating test data ==="
"$SCRIPT_DIR/generate_test_data.sh"
echo ""

# ============================================================
# Step 2: Build remotefs for Linux
# ============================================================
echo "=== Step 2: Building remotefs for Linux ==="
(cd "$REPO_ROOT" && make build-linux)
echo ""

# Determine which Linux binary to use
ARCH_REMOTE="amd64"
LINUX_BIN="$REPO_ROOT/bin/remotefs-linux-$ARCH_REMOTE"
if [ ! -f "$LINUX_BIN" ]; then
    echo "Error: Linux binary not found at $LINUX_BIN"
    exit 1
fi
echo "Linux binary: $LINUX_BIN"
echo ""

# ============================================================
# Step 3: Start publisher with FRP
# ============================================================
echo "=== Step 3: Starting remotefs publish with FRP ==="
TOKEN_FILE=$(mktemp)
PUBLISH_LOG=$(mktemp)

# Start publisher in background
"$SCRIPT_DIR/publish_local.sh" > "$PUBLISH_LOG" 2>&1 &
PUBLISH_PID=$!

# Wait for token to appear
echo "Waiting for forwarding token..."
TOKEN=""
for i in $(seq 1 30); do
    if grep -q "Forwarding token:" "$PUBLISH_LOG" 2>/dev/null; then
        TOKEN=$(grep "Forwarding token:" "$PUBLISH_LOG" | head -1 | awk '{print $3}')
        break
    fi
    sleep 1
done

if [ -z "$TOKEN" ]; then
    echo "Error: Could not get forwarding token after 30 seconds"
    echo "Publisher log:"
    cat "$PUBLISH_LOG"
    kill $PUBLISH_PID 2>/dev/null || true
    exit 1
fi

echo "Publisher started (PID: $PUBLISH_PID)"
echo "Forwarding token: $TOKEN"
echo ""

# Cleanup function
cleanup() {
    echo ""
    echo "=== Cleaning up ==="
    if [ -n "$PUBLISH_PID" ]; then
        echo "Stopping publisher (PID: $PUBLISH_PID)..."
        kill $PUBLISH_PID 2>/dev/null || true
    fi
    rm -f "$TOKEN_FILE" "$PUBLISH_LOG"
    # Clean up any SSH control sockets
    rm -f /tmp/ssh-benchmark-* 2>/dev/null || true
}
trap cleanup EXIT

# ============================================================
# Step 4: Run benchmarks on each remote host
# ============================================================
echo "=== Step 4: Running benchmarks on remote hosts ==="

for host in "${HOSTS[@]}"; do
    echo ""
    echo "--- Benchmarking: $host ---"
    
    # Extract hostname for result file
    if echo "$host" | grep -q "$PASSTHROUGH_HOST_PATTERN"; then
        hostname="${PASSTHROUGH_HOST_PATTERN}"
    else
        hostname=$(echo "$host" | cut -d'@' -f2 | cut -d'.' -f1)
    fi
    
    # SSH control socket for multiplexing
    SSH_SOCKET="/tmp/ssh-benchmark-${hostname}"
    
    # Check SSH connectivity
    echo "  Checking SSH connectivity..."
    if ! ssh -o ConnectTimeout=10 -o BatchMode=yes "$host" "echo ok" > /dev/null 2>&1; then
        echo "  Warning: Cannot connect to $host, skipping"
        continue
    fi
    
    # Start SSH ControlMaster
    echo "  Starting SSH ControlMaster..."
    ssh -M -S "$SSH_SOCKET" -o ControlPersist=10m -fN "$host"
    sleep 1
    
    # SSH and SCP commands using the control socket
    SSH_CMD="ssh -S $SSH_SOCKET"
    SCP_CMD="scp -o ControlPath=$SSH_SOCKET"
    
    # Copy remotefs binary
    echo "  Copying remotefs binary..."
    $SSH_CMD "$host" "rm -f ~/remotefs" 2>/dev/null || true
    $SCP_CMD -q "$LINUX_BIN" "$host:remotefs"
    $SSH_CMD "$host" "chmod +x ~/remotefs"
    
    # Copy benchmark scripts
    echo "  Copying benchmark scripts..."
    $SCP_CMD -q "$SCRIPT_DIR/run_remote_benchmark.sh" "$host:~/"
    $SSH_CMD "$host" "chmod +x ~/run_remote_benchmark.sh"
    
    # Setup directories
    REMOTE_SCP_DIR="~/remotefs-benchmark-scp"
    REMOTE_MOUNT_DIR="~/remotefs-benchmark-mount"
    $SSH_CMD "$host" "rm -rf $REMOTE_SCP_DIR && mkdir -p $REMOTE_SCP_DIR"
    
    # Get actual hostname for CSV
    ACTUAL_HOSTNAME=$($SSH_CMD "$host" "hostname")
    
    # Initialize result file with header
    RESULT_FILE="$RESULTS_DIR/results_${hostname}.csv"
    echo "timestamp,host,case,operation,file_size,iteration,duration_sec,throughput_mbps" > "$RESULT_FILE"
    
    # ============================================================
    # Case A: SCP benchmark (transfer + read for each iteration)
    # ============================================================
    echo ""
    echo "  === Case A: SCP Benchmark (scp transfer + read) ==="
    
    for iter in $(seq 1 $ITERATIONS); do
        echo "    Iteration $iter/$ITERATIONS (SCP)"
        ts=$(date -u +%Y-%m-%dT%H:%M:%SZ)
        
        for size in $SIZES; do
            local_file="$DATA_DIR/$size/file_${size}.bin"
            remote_file="$REMOTE_SCP_DIR/file_${size}.bin"
            file_bytes=$(size_to_bytes "$size")
            
            if [ ! -f "$local_file" ]; then
                echo "      Warning: Local file $local_file not found, skipping" >&2
                continue
            fi
            
            # Delete remote file to ensure fresh transfer
            $SSH_CMD "$host" "rm -f $remote_file" 2>/dev/null || true
            
            # Time: SCP transfer + read
            start=$(get_time)
            $SCP_CMD -q "$local_file" "$host:$remote_file"
            $SSH_CMD "$host" "cat $remote_file > /dev/null"
            end=$(get_time)
            
            duration=$(echo "$end - $start" | bc)
            throughput=$(calc_throughput "$file_bytes" "$duration")
            echo "$ts,$ACTUAL_HOSTNAME,scp,cat,$size,$iter,$duration,$throughput" >> "$RESULT_FILE"
        done
    done
    
    echo "  Case A (SCP) complete."
    
    # ============================================================
    # Case B: SSHFS benchmark (reverse tunnel + SSHFS mount)
    # ============================================================
    echo ""
    echo "  === Case B: SSHFS Benchmark ==="
    
    # Find an available port for reverse tunnel
    REVERSE_PORT=$((10000 + RANDOM % 50000))
    
    # Create reverse SSH tunnel: remote can connect to localhost:$REVERSE_PORT -> local:22
    echo "    Setting up reverse SSH tunnel (port $REVERSE_PORT)..."
    $SSH_CMD -R ${REVERSE_PORT}:localhost:${LOCAL_SSH_PORT} -fN "$host"
    sleep 2
    
    REMOTE_SSHFS_MOUNT="~/sshfs-benchmark-mount"
    
    for iter in $(seq 1 $ITERATIONS); do
        echo "    Iteration $iter/$ITERATIONS (SSHFS)"
        ts=$(date -u +%Y-%m-%dT%H:%M:%SZ)
        
        for size in $SIZES; do
            file="$REMOTE_SSHFS_MOUNT/$size/file_${size}.bin"
            file_bytes=$(size_to_bytes "$size")
            
            # Mount fresh for each file to avoid kernel page caching
            # Use direct_io,cache=no to bypass caching
            $SSH_CMD "$host" "
                fusermount -u $REMOTE_SSHFS_MOUNT 2>/dev/null || true
                mkdir -p $REMOTE_SSHFS_MOUNT
                sshfs -o StrictHostKeyChecking=no,UserKnownHostsFile=/dev/null,direct_io,cache=no -p $REVERSE_PORT ${LOCAL_USER}@localhost:$DATA_DIR $REMOTE_SSHFS_MOUNT 2>/dev/null
            "
            
            # Wait for mount and verify it works
            sleep 1
            
            # Verify mount and time the read operation
            read_result=$($SSH_CMD "$host" "
                # Verify file exists and is readable
                if [ ! -f $file ]; then
                    echo 'ERROR: File not found'
                    exit 1
                fi
                
                # Get actual file size to verify we read the whole thing
                actual_size=\$(stat -c%s $file 2>/dev/null || stat -f%z $file 2>/dev/null)
                
                # Time the read
                start=\$(date +%s.%N)
                cat $file > /dev/null
                end=\$(date +%s.%N)
                echo \"\$start \$end \$actual_size\"
            " 2>&1)
            
            if echo "$read_result" | grep -q "ERROR"; then
                echo "      Warning: SSHFS mount failed for $size, skipping" >&2
                # Unmount and continue
                $SSH_CMD "$host" "fusermount -u $REMOTE_SSHFS_MOUNT 2>/dev/null || true"
                continue
            fi
            
            # Parse the last line (which should have "start end size") - skip any noise from Lmod etc.
            timing_line=$(echo "$read_result" | grep -E '^[0-9]+\.[0-9]+ [0-9]+\.[0-9]+ [0-9]+' | tail -1)
            
            if [ -z "$timing_line" ]; then
                echo "      Warning: Could not parse timing for SSHFS $size, skipping" >&2
                $SSH_CMD "$host" "fusermount -u $REMOTE_SSHFS_MOUNT 2>/dev/null || true"
                continue
            fi
            
            start_time=$(echo "$timing_line" | awk '{print $1}')
            end_time=$(echo "$timing_line" | awk '{print $2}')
            actual_size=$(echo "$timing_line" | awk '{print $3}')
            duration=$(echo "$end_time - $start_time" | bc)
            throughput=$(calc_throughput "$file_bytes" "$duration")
            echo "$ts,$ACTUAL_HOSTNAME,sshfs,cat,$size,$iter,$duration,$throughput" >> "$RESULT_FILE"
            
            # Unmount after each read
            $SSH_CMD "$host" "fusermount -u $REMOTE_SSHFS_MOUNT 2>/dev/null || true"
        done
    done
    
    echo "  Case B (SSHFS) complete."
    
    # Close the ControlMaster for SSHFS (will reopen for RemoteFS)
    ssh -S "$SSH_SOCKET" -O exit "$host" 2>/dev/null || true
    sleep 1
    
    # ============================================================
    # Case C: RemoteFS benchmark with caching (run on remote host)
    # ============================================================
    echo ""
    echo "  === Case C: RemoteFS Benchmark (Cached) ==="
    
    # Run remote benchmark for RemoteFS (new SSH connection)
    ssh "$host" "~/run_remote_benchmark.sh \
        --token '$TOKEN' \
        --frp '$FRP_SERVER' \
        --iterations $ITERATIONS \
        --output ~/results_remotefs.csv \
        --mount-point $REMOTE_MOUNT_DIR"
    
    # Append RemoteFS results to main result file
    scp -q "$host:~/results_remotefs.csv" "/tmp/results_remotefs_${hostname}.csv"
    tail -n +2 "/tmp/results_remotefs_${hostname}.csv" >> "$RESULT_FILE"
    rm -f "/tmp/results_remotefs_${hostname}.csv"
    
    # ============================================================
    # Case D: RemoteFS without caching (run on remote host)
    # ============================================================
    echo ""
    echo "  === Case D: RemoteFS Benchmark (No Cache) ==="
    
    ssh "$host" "~/run_remote_benchmark.sh \
        --token '$TOKEN' \
        --frp '$FRP_SERVER' \
        --iterations $ITERATIONS \
        --output ~/results_remotefs_nocache.csv \
        --mount-point $REMOTE_MOUNT_DIR \
        --no-cache"
    
    scp -q "$host:~/results_remotefs_nocache.csv" "/tmp/results_remotefs_nocache_${hostname}.csv"
    tail -n +2 "/tmp/results_remotefs_nocache_${hostname}.csv" >> "$RESULT_FILE"
    rm -f "/tmp/results_remotefs_nocache_${hostname}.csv"
    
    # ============================================================
    # Case E: RemoteFS with passthrough (only on hosts matching PASSTHROUGH_HOST_PATTERN)
    # ============================================================
    if echo "$host" | grep -q "$PASSTHROUGH_HOST_PATTERN"; then
        echo ""
        echo "  === Case E: RemoteFS Benchmark (Passthrough) ==="
        
        ssh "$host" "~/run_remote_benchmark.sh \
            --token '$TOKEN' \
            --frp '$FRP_SERVER' \
            --iterations $ITERATIONS \
            --output ~/results_remotefs_passthrough.csv \
            --mount-point $REMOTE_MOUNT_DIR \
            --passthrough"
        
        scp -q "$host:~/results_remotefs_passthrough.csv" "/tmp/results_remotefs_passthrough_${hostname}.csv"
        tail -n +2 "/tmp/results_remotefs_passthrough_${hostname}.csv" >> "$RESULT_FILE"
        rm -f "/tmp/results_remotefs_passthrough_${hostname}.csv"
    fi
    
    echo "  Done with $host"
    echo "  Results: $RESULT_FILE"
done

echo ""

# ============================================================
# Step 5: Generate visualizations
# ============================================================
echo "=== Step 5: Generating visualizations ==="
python3 "$SCRIPT_DIR/visualize_results.py"

echo ""
echo "=============================================="
echo "  Benchmark Complete!"
echo "=============================================="
echo "Results saved in: $RESULTS_DIR"
ls -la "$RESULTS_DIR"/*.csv "$RESULTS_DIR"/*.png 2>/dev/null || echo "No result files found"
