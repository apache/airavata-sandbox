#!/bin/bash
# Analyze benchmark results and generate summary statistics
# Usage: analyze_results.sh <result_files...>
set -e

if [ $# -eq 0 ]; then
    echo "Usage: analyze_results.sh <result_files...>"
    exit 1
fi

echo "=============================================="
echo "  RemoteFS Benchmark Results Analysis"
echo "=============================================="
echo ""

# Combine all result files for analysis
COMBINED=$(mktemp)
HEADER_WRITTEN=0

for file in "$@"; do
    if [ ! -f "$file" ]; then
        echo "Warning: File $file not found, skipping" >&2
        continue
    fi
    
    if [ $HEADER_WRITTEN -eq 0 ]; then
        cat "$file" > "$COMBINED"
        HEADER_WRITTEN=1
    else
        tail -n +2 "$file" >> "$COMBINED"
    fi
done

if [ ! -s "$COMBINED" ]; then
    echo "Error: No data to analyze"
    rm -f "$COMBINED"
    exit 1
fi

echo "Analyzing $(wc -l < "$COMBINED") data points from $# file(s)"
echo ""

# ============================================================
# Summary by Host
# ============================================================
echo "=== Results by Host ==="
echo ""

awk -F',' 'NR>1 {
    hosts[$2]++
}
END {
    for (h in hosts) print "  " h ": " hosts[h] " measurements"
}' "$COMBINED" | sort

echo ""

# ============================================================
# Summary by Case (SCP vs RemoteFS)
# ============================================================
echo "=== Mean Duration by Case and Operation ==="
echo ""
echo "Format: case/operation/cache_state: mean_duration (n=count)"
echo ""

awk -F',' 'NR>1 {
    key = $3 "/" $4 "/" $7
    sum[key] += $8
    count[key]++
}
END {
    for (k in sum) {
        printf "  %-25s %.6fs (n=%d)\n", k ":", sum[k]/count[k], count[k]
    }
}' "$COMBINED" | sort

echo ""

# ============================================================
# Detailed Summary by File Size (cat operations)
# ============================================================
echo "=== Mean Duration by File Size (cat operations) ==="
echo ""
printf "  %-8s %-14s %-14s %-14s %-14s\n" "Size" "SCP Cold" "SCP Cached" "RFS Cold" "RFS Repeat"
printf "  %-8s %-14s %-14s %-14s %-14s\n" "----" "--------" "----------" "--------" "----------"

awk -F',' 'NR>1 && $4=="cat" {
    key = $5 "/" $3 "/" $7
    sum[key] += $8
    count[key]++
}
END {
    # Get unique sizes in order
    split("128K 256K 512K 1M 2M 8M 16M 32M 64M 128M", sizes, " ")
    for (i in sizes) {
        size = sizes[i]
        # SCP uses "cold" (transfer+read) and "cached" (local read)
        # Also check for "first" which was used in older versions
        scp_cold = 0
        if (count[size"/scp/cold"] > 0) scp_cold = sum[size"/scp/cold"]/count[size"/scp/cold"]
        else if (count[size"/scp/first"] > 0) scp_cold = sum[size"/scp/first"]/count[size"/scp/first"]
        
        scp_cached = 0
        if (count[size"/scp/cached"] > 0) scp_cached = sum[size"/scp/cached"]/count[size"/scp/cached"]
        
        # RemoteFS uses "cold"/"first" and "repeat"
        rfs_cold = 0
        if (count[size"/remotefs/cold"] > 0) rfs_cold = sum[size"/remotefs/cold"]/count[size"/remotefs/cold"]
        else if (count[size"/remotefs/first"] > 0) rfs_cold = sum[size"/remotefs/first"]/count[size"/remotefs/first"]
        
        rfs_repeat = 0
        if (count[size"/remotefs/repeat"] > 0) rfs_repeat = sum[size"/remotefs/repeat"]/count[size"/remotefs/repeat"]
        
        if (scp_cold > 0 || rfs_cold > 0) {
            printf "  %-8s %-14.6f %-14.6f %-14.6f %-14.6f\n", size, scp_cold, scp_cached, rfs_cold, rfs_repeat
        }
    }
}' "$COMBINED"

echo ""

# ============================================================
# Throughput Summary (cat operations)
# ============================================================
echo "=== Mean Throughput by File Size (cat operations, MB/s) ==="
echo ""
printf "  %-8s %-14s %-14s %-14s %-14s\n" "Size" "SCP Cold" "SCP Cached" "RFS Cold" "RFS Repeat"
printf "  %-8s %-14s %-14s %-14s %-14s\n" "----" "--------" "----------" "--------" "----------"

awk -F',' 'NR>1 && $4=="cat" {
    key = $5 "/" $3 "/" $7
    sum[key] += $9
    count[key]++
}
END {
    split("128K 256K 512K 1M 2M 8M 16M 32M 64M 128M", sizes, " ")
    for (i in sizes) {
        size = sizes[i]
        # SCP uses "cold" (transfer+read) and "cached" (local read)
        scp_cold = 0
        if (count[size"/scp/cold"] > 0) scp_cold = sum[size"/scp/cold"]/count[size"/scp/cold"]
        else if (count[size"/scp/first"] > 0) scp_cold = sum[size"/scp/first"]/count[size"/scp/first"]
        
        scp_cached = 0
        if (count[size"/scp/cached"] > 0) scp_cached = sum[size"/scp/cached"]/count[size"/scp/cached"]
        
        # RemoteFS uses "cold"/"first" and "repeat"
        rfs_cold = 0
        if (count[size"/remotefs/cold"] > 0) rfs_cold = sum[size"/remotefs/cold"]/count[size"/remotefs/cold"]
        else if (count[size"/remotefs/first"] > 0) rfs_cold = sum[size"/remotefs/first"]/count[size"/remotefs/first"]
        
        rfs_repeat = 0
        if (count[size"/remotefs/repeat"] > 0) rfs_repeat = sum[size"/remotefs/repeat"]/count[size"/remotefs/repeat"]
        
        if (scp_cold > 0 || rfs_cold > 0) {
            printf "  %-8s %-14.2f %-14.2f %-14.2f %-14.2f\n", size, scp_cold, scp_cached, rfs_cold, rfs_repeat
        }
    }
}' "$COMBINED"

echo ""

# ============================================================
# Performance Comparison (RemoteFS vs SCP ratio)
# ============================================================
echo "=== Performance Ratio (RemoteFS / SCP) ==="
echo ""
echo "Values > 1 mean RemoteFS is slower, < 1 means faster"
echo "Cold comparison: SCP (transfer+read) vs RemoteFS (on-demand read)"
echo ""
printf "  %-8s %-15s\n" "Size" "Cold Ratio"
printf "  %-8s %-15s\n" "----" "----------"

awk -F',' 'NR>1 && $4=="cat" {
    key = $5 "/" $3 "/" $7
    sum[key] += $8
    count[key]++
}
END {
    split("128K 256K 512K 1M 2M 8M 16M 32M 64M 128M", sizes, " ")
    for (i in sizes) {
        size = sizes[i]
        # SCP cold (transfer+read)
        scp_cold = 0
        if (count[size"/scp/cold"] > 0) scp_cold = sum[size"/scp/cold"]/count[size"/scp/cold"]
        else if (count[size"/scp/first"] > 0) scp_cold = sum[size"/scp/first"]/count[size"/scp/first"]
        
        # RemoteFS cold (on-demand read)
        rfs_cold = 0
        if (count[size"/remotefs/cold"] > 0) rfs_cold = sum[size"/remotefs/cold"]/count[size"/remotefs/cold"]
        else if (count[size"/remotefs/first"] > 0) rfs_cold = sum[size"/remotefs/first"]/count[size"/remotefs/first"]
        
        if (scp_cold > 0 && rfs_cold > 0) {
            cold_ratio = rfs_cold / scp_cold
            printf "  %-8s %-15.2fx\n", size, cold_ratio
        }
    }
}' "$COMBINED"

echo ""

# ============================================================
# ls Operation Summary
# ============================================================
echo "=== ls (Metadata) Operations ==="
echo ""

awk -F',' 'NR>1 && $4=="ls" {
    key = $2 "/" $3 "/" $7
    sum[key] += $8
    count[key]++
}
END {
    for (k in sum) {
        printf "  %-35s %.6fs (n=%d)\n", k ":", sum[k]/count[k], count[k]
    }
}' "$COMBINED" | sort

echo ""

# Cleanup
rm -f "$COMBINED"

echo "=============================================="
echo "  Analysis Complete"
echo "=============================================="
