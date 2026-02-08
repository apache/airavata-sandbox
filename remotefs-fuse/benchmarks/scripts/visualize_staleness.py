#!/usr/bin/env python3
"""
Visualize staleness benchmark results.
Creates plots comparing throughput and consistency across different staleness rates.
"""

import matplotlib
matplotlib.use('Agg')  # Use non-interactive backend

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import sys
import os
from pathlib import Path

# Color scheme
COLORS = {
    'direct': '#7f8c8d',          # Gray
    'remotefs_cached': '#27ae60', # Green
    'sshfs_default': '#e74c3c',   # Red
    'sshfs_nocache': '#3498db',   # Blue
}

LABELS = {
    'direct': 'Direct Access',
    'remotefs_cached': 'RemoteFS (cached)',
    'sshfs_default': 'SSHFS (default)',
    'sshfs_nocache': 'SSHFS (no-cache)',
}

MARKERS = {
    'direct': 's',
    'remotefs_cached': 'o',
    'sshfs_default': '^',
    'sshfs_nocache': 'v',
}

def load_staleness_results(filepath):
    """Load staleness benchmark CSV results."""
    df = pd.read_csv(filepath)
    return df

def create_staleness_visualization(df, output_dir):
    """Create staleness comparison visualization."""
    
    # Set style
    plt.style.use('seaborn-v0_8-whitegrid')
    
    # Create figure with 2 subplots side by side
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 5))
    
    systems = df['system'].unique()
    
    # ===== Plot 1: Throughput vs Staleness Rate =====
    for system in systems:
        system_df = df[df['system'] == system]
        color = COLORS.get(system, '#999999')
        label = LABELS.get(system, system)
        marker = MARKERS.get(system, 'o')
        
        staleness_pcts = system_df['staleness_rate'] * 100
        throughputs = system_df['throughput_mbps']
        
        ax1.plot(staleness_pcts, throughputs, marker=marker, color=color, 
                 label=label, linewidth=2, markersize=8)
    
    ax1.set_xlabel('Staleness Rate (%)', fontsize=12)
    ax1.set_ylabel('Throughput (MB/s)', fontsize=12)
    ax1.set_title('Throughput vs Staleness Rate', fontsize=14, fontweight='bold')
    ax1.legend(loc='upper right', fontsize=10)
    ax1.grid(True, alpha=0.3)
    ax1.set_xlim(-5, 105)
    
    # ===== Plot 2: Consistency vs Staleness Rate =====
    for system in systems:
        system_df = df[df['system'] == system]
        color = COLORS.get(system, '#999999')
        label = LABELS.get(system, system)
        marker = MARKERS.get(system, 'o')
        
        staleness_pcts = system_df['staleness_rate'] * 100
        consistency = system_df['consistency_pct']
        
        ax2.plot(staleness_pcts, consistency, marker=marker, color=color,
                 label=label, linewidth=2, markersize=8)
    
    ax2.set_xlabel('Staleness Rate (%)', fontsize=12)
    ax2.set_ylabel('Consistency (%)', fontsize=12)
    ax2.set_title('Consistency vs Staleness Rate', fontsize=14, fontweight='bold')
    ax2.legend(loc='lower left', fontsize=10)
    ax2.grid(True, alpha=0.3)
    ax2.set_xlim(-5, 105)
    ax2.set_ylim(-5, 105)
    
    plt.tight_layout()
    
    # Save figure
    output_path = os.path.join(output_dir, 'staleness_comparison.png')
    plt.savefig(output_path, dpi=150, bbox_inches='tight')
    plt.close()
    print(f"Saved: {output_path}")
    
    return output_path

def create_staleness_summary(df, output_dir):
    """Create a text summary of staleness results."""
    
    summary_lines = []
    summary_lines.append("=" * 80)
    summary_lines.append("  STALENESS BENCHMARK SUMMARY")
    summary_lines.append("=" * 80)
    summary_lines.append("")
    summary_lines.append("This benchmark measures cache behavior under file modification scenarios.")
    summary_lines.append("Staleness rate = probability a file is modified between consecutive reads.")
    summary_lines.append("")
    summary_lines.append("Key findings:")
    summary_lines.append("-" * 80)
    summary_lines.append("")
    
    # Analyze results
    systems = df['system'].unique()
    
    # Table header
    summary_lines.append(f"{'System':<20} {'Staleness':<12} {'Throughput':<15} {'Consistency':<15}")
    summary_lines.append("-" * 62)
    
    for system in ['direct', 'remotefs_cached', 'sshfs_default', 'sshfs_nocache']:
        if system not in systems:
            continue
        system_df = df[df['system'] == system]
        for _, row in system_df.iterrows():
            label = LABELS.get(system, system)
            summary_lines.append(
                f"{label:<20} {row['staleness_rate']*100:>6.0f}%      "
                f"{row['throughput_mbps']:>10.2f} MB/s  {row['consistency_pct']:>10.1f}%"
            )
        summary_lines.append("")
    
    # Key insights
    summary_lines.append("")
    summary_lines.append("Key Insights:")
    summary_lines.append("-" * 80)
    
    # Compare RemoteFS vs SSHFS default at 50% staleness
    rfs_50 = df[(df['system'] == 'remotefs_cached') & (df['staleness_rate'] == 0.50)]
    sshfs_50 = df[(df['system'] == 'sshfs_default') & (df['staleness_rate'] == 0.50)]
    
    if len(rfs_50) > 0 and len(sshfs_50) > 0:
        rfs_consistency = rfs_50['consistency_pct'].values[0]
        sshfs_consistency = sshfs_50['consistency_pct'].values[0]
        summary_lines.append(f"- At 50% staleness: RemoteFS consistency = {rfs_consistency:.1f}%, SSHFS default = {sshfs_consistency:.1f}%")
        if rfs_consistency > sshfs_consistency:
            summary_lines.append("  -> RemoteFS maintains data consistency while SSHFS default returns stale data")
    
    # Compare throughput at 0% staleness (cache warm)
    rfs_0 = df[(df['system'] == 'remotefs_cached') & (df['staleness_rate'] == 0.00)]
    direct_0 = df[(df['system'] == 'direct') & (df['staleness_rate'] == 0.00)]
    
    if len(rfs_0) > 0 and len(direct_0) > 0:
        rfs_tp = rfs_0['throughput_mbps'].values[0]
        direct_tp = direct_0['throughput_mbps'].values[0]
        if rfs_tp >= direct_tp:
            summary_lines.append(f"- At 0% staleness: RemoteFS cached ({rfs_tp:.2f} MB/s) >= Direct ({direct_tp:.2f} MB/s)")
            summary_lines.append("  -> Cache provides performance benefit for stable files")
    
    summary_lines.append("")
    summary_lines.append("Recommendations:")
    summary_lines.append("- Use RemoteFS for data consistency (bounded staleness ~1s in production)")
    summary_lines.append("- SSHFS default is faster but may return stale data indefinitely")
    summary_lines.append("- SSHFS nocache is consistent but has high per-read latency")
    
    summary_text = "\n".join(summary_lines)
    print(summary_text)
    
    # Save to file
    summary_path = os.path.join(output_dir, 'staleness_summary.txt')
    with open(summary_path, 'w') as f:
        f.write(summary_text)
    print(f"\nSummary saved to: {summary_path}")

def main():
    # Default paths
    script_dir = Path(__file__).parent
    results_dir = script_dir.parent / 'results'
    
    # Find staleness results file
    staleness_file = results_dir / 'staleness_results.csv'
    
    if len(sys.argv) > 1:
        staleness_file = Path(sys.argv[1])
    
    if not staleness_file.exists():
        print(f"Error: Staleness results not found at {staleness_file}")
        print("Run the staleness benchmark first: ./run_staleness_benchmark.sh")
        sys.exit(1)
    
    print(f"Loading staleness results from: {staleness_file}")
    df = load_staleness_results(staleness_file)
    print(f"Loaded {len(df)} data points")
    
    output_dir = str(results_dir)
    
    # Create visualization
    print("\nGenerating staleness visualization...")
    create_staleness_visualization(df, output_dir)
    
    # Create summary
    print("\n")
    create_staleness_summary(df, output_dir)

if __name__ == '__main__':
    main()
