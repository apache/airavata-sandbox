#!/usr/bin/env python3
"""
Visualize benchmark results comparing RemoteFS vs SCP performance.
Creates separate plots for each host showing duration and throughput comparisons.
"""

import matplotlib
matplotlib.use('Agg')  # Use non-interactive backend

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import sys
import os
from pathlib import Path

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
    """Create visualization for a single host."""
    
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
    
    # Calculate mean duration and throughput by case and file_size
    duration_means = host_df.groupby(['case', 'file_size'])['duration_sec'].mean().unstack(level=0)
    duration_means = duration_means.reindex(size_order)
    
    throughput_means = host_df.groupby(['case', 'file_size'])['throughput_mbps'].mean().unstack(level=0)
    throughput_means = throughput_means.reindex(size_order)
    
    # Set style
    plt.style.use('seaborn-v0_8-whitegrid')
    
    # Create figure with 2x2 subplots
    fig = plt.figure(figsize=(14, 10))
    fig.suptitle(f'RemoteFS vs SCP Performance - {host}', fontsize=14, fontweight='bold')
    
    x = np.arange(len(size_order))
    width = 0.35
    
    # ===== Plot 1: Duration Comparison (Bar Chart) =====
    ax1 = fig.add_subplot(2, 2, 1)
    
    scp_duration = duration_means['scp'].values if 'scp' in duration_means.columns else np.zeros(len(size_order))
    rfs_duration = duration_means['remotefs'].values if 'remotefs' in duration_means.columns else np.zeros(len(size_order))
    
    bars1 = ax1.bar(x - width/2, scp_duration, width, label='SCP (Transfer+Read)', color='#2ecc71', alpha=0.8)
    bars2 = ax1.bar(x + width/2, rfs_duration, width, label='RemoteFS (On-Demand)', color='#e74c3c', alpha=0.8)
    
    ax1.set_xlabel('File Size', fontsize=11)
    ax1.set_ylabel('Duration (seconds)', fontsize=11)
    ax1.set_title('Mean Read Duration by File Size', fontsize=12, fontweight='bold')
    ax1.set_xticks(x)
    ax1.set_xticklabels(size_order, rotation=45)
    ax1.legend()
    ax1.set_yscale('log')
    ax1.grid(True, alpha=0.3)
    
    # ===== Plot 2: Throughput Comparison (Bar Chart) =====
    ax2 = fig.add_subplot(2, 2, 2)
    
    scp_throughput = throughput_means['scp'].values if 'scp' in throughput_means.columns else np.zeros(len(size_order))
    rfs_throughput = throughput_means['remotefs'].values if 'remotefs' in throughput_means.columns else np.zeros(len(size_order))
    
    bars3 = ax2.bar(x - width/2, scp_throughput, width, label='SCP (Transfer+Read)', color='#2ecc71', alpha=0.8)
    bars4 = ax2.bar(x + width/2, rfs_throughput, width, label='RemoteFS (On-Demand)', color='#e74c3c', alpha=0.8)
    
    ax2.set_xlabel('File Size', fontsize=11)
    ax2.set_ylabel('Throughput (MB/s)', fontsize=11)
    ax2.set_title('Mean Throughput by File Size', fontsize=12, fontweight='bold')
    ax2.set_xticks(x)
    ax2.set_xticklabels(size_order, rotation=45)
    ax2.legend()
    ax2.set_yscale('log')
    ax2.grid(True, alpha=0.3)
    
    # ===== Plot 3: Performance Ratio =====
    ax3 = fig.add_subplot(2, 2, 3)
    
    # Calculate ratio (RemoteFS / SCP) - values < 1 mean RemoteFS is faster
    with np.errstate(divide='ignore', invalid='ignore'):
        ratio = np.where(scp_duration > 0, rfs_duration / scp_duration, 0)
    
    colors = ['#27ae60' if r < 1 else '#e74c3c' for r in ratio]
    bars5 = ax3.bar(x, ratio, width*1.5, color=colors, alpha=0.8)
    
    ax3.set_xlabel('File Size', fontsize=11)
    ax3.set_ylabel('Ratio (RemoteFS / SCP)', fontsize=11)
    ax3.set_title('Performance Ratio (<1 = RemoteFS Faster, >1 = SCP Faster)', fontsize=12, fontweight='bold')
    ax3.set_xticks(x)
    ax3.set_xticklabels(size_order, rotation=45)
    ax3.axhline(y=1, color='black', linestyle='--', alpha=0.7, linewidth=2)
    ax3.grid(True, alpha=0.3)
    
    # Add value labels on bars
    for i, (bar, r) in enumerate(zip(bars5, ratio)):
        if r > 0:
            label = f'{r:.2f}x'
            ax3.annotate(label, (bar.get_x() + bar.get_width()/2, bar.get_height()),
                        ha='center', va='bottom', fontsize=8, rotation=0)
    
    # ===== Plot 4: Line Chart Comparison =====
    ax4 = fig.add_subplot(2, 2, 4)
    
    ax4.plot(size_order, scp_duration, marker='o', linestyle='-', color='#2ecc71', 
             label='SCP (Transfer+Read)', linewidth=2, markersize=8)
    ax4.plot(size_order, rfs_duration, marker='s', linestyle='-', color='#e74c3c', 
             label='RemoteFS (On-Demand)', linewidth=2, markersize=8)
    
    ax4.set_xlabel('File Size', fontsize=11)
    ax4.set_ylabel('Duration (seconds)', fontsize=11)
    ax4.set_title('Duration Trend Across File Sizes', fontsize=12, fontweight='bold')
    ax4.set_yscale('log')
    ax4.legend()
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
    """Create a text summary of results."""
    
    size_order = ['128K', '256K', '512K', '1M', '2M', '8M', '16M', '32M', '64M', '128M']
    hosts = df['host'].unique()
    
    summary_lines = []
    summary_lines.append("=" * 70)
    summary_lines.append("  BENCHMARK SUMMARY")
    summary_lines.append("=" * 70)
    
    for host in hosts:
        host_df = df[(df['host'] == host) & (df['operation'] == 'cat')]
        if len(host_df) == 0:
            continue
            
        host_short = host.split('.')[0]
        summary_lines.append(f"\n--- {host} ---\n")
        summary_lines.append(f"{'Size':<8} {'SCP (s)':<12} {'RemoteFS (s)':<14} {'Ratio':<10} {'Winner':<12}")
        summary_lines.append("-" * 60)
        
        for size in size_order:
            scp_data = host_df[(host_df['file_size'] == size) & (host_df['case'] == 'scp')]
            rfs_data = host_df[(host_df['file_size'] == size) & (host_df['case'] == 'remotefs')]
            
            scp_mean = scp_data['duration_sec'].mean() if len(scp_data) > 0 else 0
            rfs_mean = rfs_data['duration_sec'].mean() if len(rfs_data) > 0 else 0
            
            if scp_mean > 0 and rfs_mean > 0:
                ratio = rfs_mean / scp_mean
                winner = "RemoteFS" if ratio < 1 else "SCP"
                speedup = f"{1/ratio:.1f}x faster" if ratio < 1 else f"{ratio:.1f}x faster"
                summary_lines.append(f"{size:<8} {scp_mean:<12.3f} {rfs_mean:<14.3f} {ratio:<10.2f} {winner} ({speedup})")
            else:
                summary_lines.append(f"{size:<8} {scp_mean:<12.3f} {rfs_mean:<14.3f} {'N/A':<10}")
    
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
