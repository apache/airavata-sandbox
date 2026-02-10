#!/bin/bash
# Benchmark ls -la (metadata) operations
# Usage: benchmark_ls.sh <directory> <case> <iteration> <hostname>
# Outputs CSV lines to stdout

DIR="$1"
CASE="$2"
ITER="$3"
HOST="$4"

if [ -z "$DIR" ] || [ -z "$CASE" ] || [ -z "$ITER" ] || [ -z "$HOST" ]; then
    echo "Usage: benchmark_ls.sh <directory> <case> <iteration> <hostname>" >&2
    exit 1
fi

if [ ! -d "$DIR" ]; then
    echo "Error: Directory $DIR does not exist" >&2
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

# Attempt to drop caches
drop_caches

# First read (potentially cold if cache drop worked)
start=$(get_time)
ls -laR "$DIR" > /dev/null 2>&1
end=$(get_time)
first_read=$(echo "$end - $start" | bc)

# Determine cache state label
if [ "$CACHE_DROPPED" -eq 1 ]; then
    first_label="cold"
else
    first_label="first"
fi

# Multiple subsequent reads for stable measurement
NUM_READS=3
total_time=0

for i in $(seq 1 $NUM_READS); do
    start=$(get_time)
    ls -laR "$DIR" > /dev/null 2>&1
    end=$(get_time)
    read_time=$(echo "$end - $start" | bc)
    total_time=$(echo "$total_time + $read_time" | bc)
done

# Calculate average
avg_read=$(echo "scale=9; $total_time / $NUM_READS" | bc)

# Label based on case type
if [ "$CASE" = "remotefs" ]; then
    second_label="repeat"
else
    second_label="cached"
fi

# Output CSV lines
ts=$(date -u +%Y-%m-%dT%H:%M:%SZ)
echo "$ts,$HOST,$CASE,ls,all,$ITER,$first_label,$first_read,0"
echo "$ts,$HOST,$CASE,ls,all,$ITER,$second_label,$avg_read,0"
