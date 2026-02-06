#!/usr/bin/env python3
"""
Visualize benchmark results comparing file access methods (SCP, SSHFS, RemoteFS).
Creates separate plots for each host showing duration and throughput comparisons.

Handles 5 experimental conditions:
- scp: Copy file + read (baseline)
- sshfs: SSHFS with kernel caching (default behavior)
- sshfs_nocache: SSHFS with direct_io (no kernel cache)
- remotefs: RemoteFS with block cache
- remotefs_nocache: RemoteFS without block cache
"""

import matplotlib
matplotlib.use('Agg')  # Use non-interactive backend

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import sys
import os
from pathlib import Path

# Color scheme for the methods
COLORS = {
    'scp': '#2ecc71',           # Green
    'sshfs': '#3498db',         # Blue  
    'sshfs_nocache': '#85c1e9', # Light blue
    'remotefs': '#e74c3c',      # Red
    'remotefs_nocache': '#f1948a', # Light red
    'remotefs_passthrough': '#9b59b6', # Purple - FUSE passthrough
}

LABELS = {
    'scp': 'SCP (transfer + read)',
    'sshfs': 'SSHFS (cached)',
    'sshfs_nocache': 'SSHFS (no cache)',
    'remotefs': 'RemoteFS (cached)',
    'remotefs_nocache': 'RemoteFS (no cache)',
    'remotefs_passthrough': 'RemoteFS (passthrough)',
}

def parse_size_to_bytes(size_str):
    """Convert size string (e.g., '128K', '1M') to bytes for sorting."""
    if size_str == 'all':
        return 0
    multipliers = {'K': 1024, 'M': 1024*1024, 'G': 1024*1024*1024}
    for suffix, mult in multipliers.items():
        if size_str.endswith(suffix):
            return int(size_str[:-1]) * mult
    return int(size_str)

def load_results(result_files):
    """Load and combine result CSV files."""
    dfs = []
    for f in result_files:
        if os.path.exists(f):
            df = pd.read_csv(f)
            dfs.append(df)
    if not dfs:
        raise ValueError("No result files found")
    return pd.concat(dfs, ignore_index=True)

def create_host_visualization(df, host, output_dir):
    """Create visualization for a single host with all benchmark variations."""
    
    # Filter to this host and cat operations
    host_df = df[(df['host'] == host) & (df['operation'] == 'cat')].copy()
    
    if len(host_df) == 0:
        print(f"  No data for host {host}, skipping")
        return None
    
    # Add size in bytes for sorting
    host_df['size_bytes'] = host_df['file_size'].apply(parse_size_to_bytes)
    host_df = host_df.sort_values('size_bytes')
    
    # Define ordered sizes
    size_order = ['128K', '256K', '512K', '1M', '2M', '8M', '16M', '32M', '64M', '128M']
    
    # Define case order for consistent display
    all_cases = ['scp', 'sshfs', 'sshfs_nocache', 'remotefs', 'remotefs_nocache', 'remotefs_passthrough']
    available_cases = [c for c in all_cases if c in host_df['case'].unique()]
    num_cases = len(available_cases)
    
    # Calculate mean duration and throughput by case and file_size
    duration_means = host_df.groupby(['case', 'file_size'])['duration_sec'].mean().unstack(level=0)
    duration_means = duration_means.reindex(size_order)
    
    throughput_means = host_df.groupby(['case', 'file_size'])['throughput_mbps'].mean().unstack(level=0)
    throughput_means = throughput_means.reindex(size_order)
    
    # Set style
    plt.style.use('seaborn-v0_8-whitegrid')
    
    # Create figure with 2x2 subplots
    fig = plt.figure(figsize=(18, 14))
    
    # Dynamic title
    title = f'File Access Benchmark - {host}'
    fig.suptitle(title, fontsize=14, fontweight='bold')
    
    x = np.arange(len(size_order))
    width = 0.8 / num_cases  # Adaptive width based on number of cases
    
    # ===== Plot 1: Duration Comparison (Bar Chart) =====
    ax1 = fig.add_subplot(2, 2, 1)
    
    offsets = np.linspace(-width * (num_cases - 1) / 2, width * (num_cases - 1) / 2, num_cases)
    
    for i, case in enumerate(available_cases):
        durations = duration_means[case].values if case in duration_means.columns else np.zeros(len(size_order))
        ax1.bar(x + offsets[i], durations, width, label=LABELS.get(case, case), 
                color=COLORS.get(case, '#999999'), alpha=0.8)
    
    ax1.set_xlabel('File Size', fontsize=11)
    ax1.set_ylabel('Duration (seconds)', fontsize=11)
    ax1.set_title('Mean Read Duration by File Size', fontsize=12, fontweight='bold')
    ax1.set_xticks(x)
    ax1.set_xticklabels(size_order, rotation=45)
    ax1.legend(loc='upper left', fontsize=8)
    ax1.set_yscale('log')
    ax1.grid(True, alpha=0.3)
    
    # ===== Plot 2: Throughput Comparison (Bar Chart) =====
    ax2 = fig.add_subplot(2, 2, 2)
    
    for i, case in enumerate(available_cases):
        throughputs = throughput_means[case].values if case in throughput_means.columns else np.zeros(len(size_order))
        ax2.bar(x + offsets[i], throughputs, width, label=LABELS.get(case, case),
                color=COLORS.get(case, '#999999'), alpha=0.8)
    
    ax2.set_xlabel('File Size', fontsize=11)
    ax2.set_ylabel('Throughput (MB/s)', fontsize=11)
    ax2.set_title('Mean Throughput by File Size', fontsize=12, fontweight='bold')
    ax2.set_xticks(x)
    ax2.set_xticklabels(size_order, rotation=45)
    ax2.legend(loc='upper left', fontsize=8)
    ax2.set_yscale('log')
    ax2.grid(True, alpha=0.3)
    
    # ===== Plot 3: Cached vs No-Cache Comparison =====
    ax3 = fig.add_subplot(2, 2, 3)
    
    # Compare SSHFS cached vs no-cache and RemoteFS cached vs no-cache
    comparison_pairs = [
        ('sshfs', 'sshfs_nocache', 'SSHFS'),
        ('remotefs', 'remotefs_nocache', 'RemoteFS')
    ]
    
    bar_width = 0.35
    
    for idx, (cached, nocache, name) in enumerate(comparison_pairs):
        if cached not in duration_means.columns or nocache not in duration_means.columns:
            continue
        
        cached_dur = duration_means[cached].values
        nocache_dur = duration_means[nocache].values
        
        # Calculate cache speedup (how much faster cached is)
        with np.errstate(divide='ignore', invalid='ignore'):
            speedup = np.where(cached_dur > 0, nocache_dur / cached_dur, 0)
        
        offset = -bar_width/2 + idx * bar_width
        color = COLORS.get(cached, '#999999')
        bars = ax3.bar(x + offset, speedup, bar_width, label=f'{name} Cache Speedup', 
                       color=color, alpha=0.8)
        
        # Add value labels
        for bar, s in zip(bars, speedup):
            if s > 0 and s < 100:
                ax3.annotate(f'{s:.1f}x', (bar.get_x() + bar.get_width()/2, bar.get_height()),
                            ha='center', va='bottom', fontsize=7)
    
    ax3.set_xlabel('File Size', fontsize=11)
    ax3.set_ylabel('Cache Speedup (no-cache time / cached time)', fontsize=11)
    ax3.set_title('Cache Effectiveness (Higher = More Benefit from Caching)', fontsize=12, fontweight='bold')
    ax3.set_xticks(x)
    ax3.set_xticklabels(size_order, rotation=45)
    ax3.axhline(y=1, color='black', linestyle='--', alpha=0.7, linewidth=2, label='No benefit')
    ax3.legend(loc='upper left', fontsize=8)
    ax3.grid(True, alpha=0.3)
    
    # ===== Plot 4: Throughput Line Chart =====
    ax4 = fig.add_subplot(2, 2, 4)
    
    markers = {'scp': 'o', 'sshfs': '^', 'sshfs_nocache': 'v', 'remotefs': 's', 'remotefs_nocache': 'd', 'remotefs_passthrough': 'p'}
    linestyles = {'scp': '-', 'sshfs': '-', 'sshfs_nocache': '--', 'remotefs': '-', 'remotefs_nocache': '--', 'remotefs_passthrough': '-.'}
    
    for case in available_cases:
        throughputs = throughput_means[case].values if case in throughput_means.columns else np.zeros(len(size_order))
        ax4.plot(size_order, throughputs, marker=markers.get(case, 'o'), linestyle=linestyles.get(case, '-'), 
                 color=COLORS.get(case, '#999999'), label=LABELS.get(case, case), 
                 linewidth=2, markersize=6)
    
    ax4.set_xlabel('File Size', fontsize=11)
    ax4.set_ylabel('Throughput (MB/s)', fontsize=11)
    ax4.set_title('Throughput Scaling Across File Sizes', fontsize=12, fontweight='bold')
    ax4.set_yscale('log')
    ax4.legend(loc='upper left', fontsize=8)
    ax4.grid(True, alpha=0.3)
    plt.setp(ax4.xaxis.get_majorticklabels(), rotation=45)
    
    plt.tight_layout()
    
    # Save figure
    host_short = host.split('.')[0]
    output_path = os.path.join(output_dir, f'benchmark_{host_short}.png')
    plt.savefig(output_path, dpi=150, bbox_inches='tight')
    plt.close()
    print(f"  Saved: {output_path}")
    
    return output_path

def create_summary_table(df, output_dir):
    """Create a comprehensive text summary of results with all variations."""
    
    size_order = ['128K', '256K', '512K', '1M', '2M', '8M', '16M', '32M', '64M', '128M']
    hosts = df['host'].unique()
    
    all_cases = ['scp', 'sshfs', 'sshfs_nocache', 'remotefs', 'remotefs_nocache', 'remotefs_passthrough']
    
    summary_lines = []
    summary_lines.append("=" * 120)
    summary_lines.append("  COMPREHENSIVE BENCHMARK SUMMARY")
    summary_lines.append("=" * 120)
    summary_lines.append("")
    summary_lines.append("Experimental Conditions:")
    summary_lines.append("  SCP:             scp local_file remote:/tmp/ && ssh remote cat /tmp/file > /dev/null")
    summary_lines.append("  SSHFS (cached):  sshfs mount with kernel page cache (default)")
    summary_lines.append("  SSHFS (nocache): sshfs -o direct_io (bypasses kernel cache)")
    summary_lines.append("  RemoteFS (cached):   RemoteFS with block cache enabled")
    summary_lines.append("  RemoteFS (nocache):  RemoteFS with --no-cache flag")
    summary_lines.append("")
    summary_lines.append("Notes:")
    summary_lines.append("  - All tests use fresh mounts for each file size to ensure fair comparison")
    summary_lines.append("  - Multiple iterations per configuration, results show mean values")
    summary_lines.append("  - Cached variants include warm-cache performance from repeated reads")
    summary_lines.append("=" * 120)
    
    for host in hosts:
        host_df = df[(df['host'] == host) & (df['operation'] == 'cat')]
        if len(host_df) == 0:
            continue
        
        available = [c for c in all_cases if c in host_df['case'].unique()]
        
        summary_lines.append(f"\n{'='*50}")
        summary_lines.append(f"  HOST: {host}")
        summary_lines.append(f"  Available methods: {', '.join(available)}")
        summary_lines.append(f"{'='*50}\n")
        
        # Full comparison table
        summary_lines.append("THROUGHPUT COMPARISON (MB/s):")
        header = f"{'Size':<8}"
        for case in available:
            label = LABELS.get(case, case).split(' ')[0]
            header += f" {label:<14}"
        header += f" {'Best':<15} {'vs SCP':<12}"
        summary_lines.append(header)
        summary_lines.append("-" * len(header))
        
        for size in size_order:
            row = f"{size:<8}"
            throughputs = {}
            
            for case in available:
                data = host_df[(host_df['file_size'] == size) & (host_df['case'] == case)]
                tp = data['throughput_mbps'].mean() if len(data) > 0 else 0
                throughputs[case] = tp
                row += f" {tp:<14.2f}"
            
            # Find best
            valid = {k: v for k, v in throughputs.items() if v > 0}
            if valid:
                best_case = max(valid, key=valid.get)
                best_label = LABELS.get(best_case, best_case).split(' ')[0]
                row += f" {best_label:<15}"
                
                # Compare to SCP
                if 'scp' in throughputs and throughputs['scp'] > 0:
                    ratio = throughputs[best_case] / throughputs['scp']
                    row += f" {ratio:.1f}x"
                else:
                    row += f" {'N/A':<12}"
            else:
                row += f" {'N/A':<15} {'N/A':<12}"
            
            summary_lines.append(row)
        
        summary_lines.append("")
        
        # Cache effectiveness analysis
        if 'sshfs' in available and 'sshfs_nocache' in available:
            summary_lines.append("SSHFS CACHE EFFECTIVENESS:")
            summary_lines.append(f"{'Size':<8} {'Cached (MB/s)':<15} {'No-cache (MB/s)':<18} {'Speedup':<10}")
            summary_lines.append("-" * 55)
            for size in size_order:
                cached_data = host_df[(host_df['file_size'] == size) & (host_df['case'] == 'sshfs')]
                nocache_data = host_df[(host_df['file_size'] == size) & (host_df['case'] == 'sshfs_nocache')]
                cached_tp = cached_data['throughput_mbps'].mean() if len(cached_data) > 0 else 0
                nocache_tp = nocache_data['throughput_mbps'].mean() if len(nocache_data) > 0 else 0
                speedup = cached_tp / nocache_tp if nocache_tp > 0 else 0
                summary_lines.append(f"{size:<8} {cached_tp:<15.2f} {nocache_tp:<18.2f} {speedup:<10.1f}x")
            summary_lines.append("")
        
        if 'remotefs' in available and 'remotefs_nocache' in available:
            summary_lines.append("REMOTEFS CACHE EFFECTIVENESS:")
            summary_lines.append(f"{'Size':<8} {'Cached (MB/s)':<15} {'No-cache (MB/s)':<18} {'Speedup':<10}")
            summary_lines.append("-" * 55)
            for size in size_order:
                cached_data = host_df[(host_df['file_size'] == size) & (host_df['case'] == 'remotefs')]
                nocache_data = host_df[(host_df['file_size'] == size) & (host_df['case'] == 'remotefs_nocache')]
                cached_tp = cached_data['throughput_mbps'].mean() if len(cached_data) > 0 else 0
                nocache_tp = nocache_data['throughput_mbps'].mean() if len(nocache_data) > 0 else 0
                speedup = cached_tp / nocache_tp if nocache_tp > 0 else 0
                summary_lines.append(f"{size:<8} {cached_tp:<15.2f} {nocache_tp:<18.2f} {speedup:<10.1f}x")
            summary_lines.append("")
        
        # Fair comparison: no-cache vs no-cache
        if 'sshfs_nocache' in available and 'remotefs_nocache' in available:
            summary_lines.append("FAIR COMPARISON (No-cache vs No-cache):")
            summary_lines.append(f"{'Size':<8} {'SSHFS nocache':<15} {'RemoteFS nocache':<18} {'RemoteFS Advantage':<20}")
            summary_lines.append("-" * 65)
            for size in size_order:
                sshfs_data = host_df[(host_df['file_size'] == size) & (host_df['case'] == 'sshfs_nocache')]
                rfs_data = host_df[(host_df['file_size'] == size) & (host_df['case'] == 'remotefs_nocache')]
                sshfs_tp = sshfs_data['throughput_mbps'].mean() if len(sshfs_data) > 0 else 0
                rfs_tp = rfs_data['throughput_mbps'].mean() if len(rfs_data) > 0 else 0
                advantage = rfs_tp / sshfs_tp if sshfs_tp > 0 else 0
                summary_lines.append(f"{size:<8} {sshfs_tp:<15.2f} {rfs_tp:<18.2f} {advantage:<20.1f}x faster")
            summary_lines.append("")
        
        # FUSE Passthrough comparison
        if 'remotefs' in available and 'remotefs_passthrough' in available:
            summary_lines.append("FUSE PASSTHROUGH COMPARISON (kernel 6.9+ feature):")
            summary_lines.append(f"{'Size':<8} {'RemoteFS (cached)':<20} {'RemoteFS (passthrough)':<25} {'Passthrough Speedup':<20}")
            summary_lines.append("-" * 75)
            for size in size_order:
                cached_data = host_df[(host_df['file_size'] == size) & (host_df['case'] == 'remotefs')]
                passthrough_data = host_df[(host_df['file_size'] == size) & (host_df['case'] == 'remotefs_passthrough')]
                cached_tp = cached_data['throughput_mbps'].mean() if len(cached_data) > 0 else 0
                passthrough_tp = passthrough_data['throughput_mbps'].mean() if len(passthrough_data) > 0 else 0
                speedup = passthrough_tp / cached_tp if cached_tp > 0 else 0
                summary_lines.append(f"{size:<8} {cached_tp:<20.2f} {passthrough_tp:<25.2f} {speedup:<20.1f}x")
            summary_lines.append("")
    
    summary_text = "\n".join(summary_lines)
    print(summary_text)
    
    # Save to file
    summary_path = os.path.join(output_dir, 'benchmark_summary.txt')
    with open(summary_path, 'w') as f:
        f.write(summary_text)
    print(f"\nSummary saved to: {summary_path}")
    
    return summary_path

def main():
    # Default to results directory
    script_dir = Path(__file__).parent
    results_dir = script_dir.parent / 'results'
    
    if len(sys.argv) > 1:
        result_files = sys.argv[1:]
    else:
        result_files = list(results_dir.glob('results_*.csv'))
    
    if not result_files:
        print("No result files found. Usage: visualize_results.py [result_files...]")
        sys.exit(1)
    
    print(f"Loading results from: {result_files}")
    df = load_results(result_files)
    print(f"Loaded {len(df)} data points")
    
    output_dir = str(results_dir)
    
    # Get unique hosts
    hosts = df['host'].unique()
    print(f"\nHosts found: {hosts}")
    
    # Create visualization for each host
    print("\nGenerating visualizations...")
    for host in hosts:
        print(f"\nProcessing {host}...")
        create_host_visualization(df, host, output_dir)
    
    # Create summary
    print("\n")
    create_summary_table(df, output_dir)

if __name__ == '__main__':
    main()
