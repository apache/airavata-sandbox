#!/bin/bash
# Benchmark cat (file read) operations
# Usage: benchmark_cat.sh <file> <size> <case> <iteration> <hostname>
# Outputs CSV lines to stdout
#
# For each file, performs multiple reads and reports:
# - first_read: First read after potential cache drop (may or may not be cold depending on permissions)
# - subsequent_read: Average of subsequent reads (warm cache if local, network-bound if remote)

FILE="$1"
SIZE="$2"
CASE="$3"
ITER="$4"
HOST="$5"

if [ -z "$FILE" ] || [ -z "$SIZE" ] || [ -z "$CASE" ] || [ -z "$ITER" ] || [ -z "$HOST" ]; then
    echo "Usage: benchmark_cat.sh <file> <size> <case> <iteration> <hostname>" >&2
    exit 1
fi

if [ ! -f "$FILE" ]; then
    echo "Error: File $FILE does not exist" >&2
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

# Get file size in bytes for throughput calculation
# Linux uses stat -c%s, macOS uses stat -f%z
FILE_BYTES=$(stat -c%s "$FILE" 2>/dev/null || stat -f%z "$FILE" 2>/dev/null)

# Try to drop caches - track if it worked
CACHE_DROPPED=0
drop_caches() {
    sync
    if [ -w /proc/sys/vm/drop_caches ]; then
        echo 3 > /proc/sys/vm/drop_caches 2>/dev/null && CACHE_DROPPED=1
    elif sudo -n sh -c 'echo 3 > /proc/sys/vm/drop_caches' 2>/dev/null; then
        CACHE_DROPPED=1
    fi
}

# Calculate throughput in MB/s
calc_throughput() {
    local duration="$1"
    if [ -n "$FILE_BYTES" ] && [ "$FILE_BYTES" -gt 0 ]; then
        echo "scale=2; $FILE_BYTES / 1048576 / $duration" | bc 2>/dev/null || echo "0"
    else
        echo "0"
    fi
}

# Attempt to drop caches
drop_caches

# First read (potentially cold if cache drop worked)
start=$(get_time)
cat "$FILE" > /dev/null
end=$(get_time)
first_read=$(echo "$end - $start" | bc)
first_throughput=$(calc_throughput "$first_read")

# Determine cache state label based on whether drop worked
if [ "$CACHE_DROPPED" -eq 1 ]; then
    first_label="cold"
else
    first_label="first"
fi

# Multiple subsequent reads to get stable measurement
NUM_READS=3
total_time=0

for i in $(seq 1 $NUM_READS); do
    start=$(get_time)
    cat "$FILE" > /dev/null
    end=$(get_time)
    read_time=$(echo "$end - $start" | bc)
    total_time=$(echo "$total_time + $read_time" | bc)
done

# Calculate average
avg_read=$(echo "scale=9; $total_time / $NUM_READS" | bc)
avg_throughput=$(calc_throughput "$avg_read")

# For RemoteFS, subsequent reads are still network-bound, not cache-warm
if [ "$CASE" = "remotefs" ]; then
    second_label="repeat"
else
    second_label="cached"
fi

# Output CSV lines
ts=$(date -u +%Y-%m-%dT%H:%M:%SZ)
echo "$ts,$HOST,$CASE,cat,$SIZE,$ITER,$first_label,$first_read,$first_throughput"
echo "$ts,$HOST,$CASE,cat,$SIZE,$ITER,$second_label,$avg_read,$avg_throughput"
