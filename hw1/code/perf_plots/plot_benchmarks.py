import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# Read the CSV file containing benchmark results
data = pd.read_csv('./stats/benchmark_results.csv')

# Extract unique types and buffer sizes
prefix_types = data['PrefixType'].unique()
buffer_sizes = data['BufferSize'].unique()

# Plot Execution Time vs Buffer Size
def plot_execution_time(data, buffer_sizes, prefix_types, output_file):
    fig, ax = plt.subplots(figsize=(10, 6))  # Adjust figure size for better visibility
    
    bar_width = 0.2
    index = np.arange(len(buffer_sizes))
    
    # Plot a bar for each prefix type
    for i, prefix_type in enumerate(prefix_types):
        times = data[data['PrefixType'] == prefix_type]['AverageTime(ms)']
        bars = ax.bar(index + i * bar_width, times, bar_width, label=prefix_type)
        
        # Add value labels on top of the bars
        for bar in bars:
            yval = bar.get_height()
            ax.text(bar.get_x() + bar.get_width()/2, yval, round(yval, 2), va='bottom', ha='center')
    
    # Set chart properties
    ax.set_xlabel('Buffer Size')
    ax.set_ylabel('Average Execution Time (ms)')
    ax.set_title('Execution Time vs Buffer Size')
    ax.set_xticks(index + bar_width / 2 * len(prefix_types))
    ax.set_xticklabels(buffer_sizes)
    ax.set_yscale('log')  # Use logarithmic scale for the y-axis to improve visibility
    ax.legend(loc='upper left')

    # Save the plot to a file
    plt.tight_layout()
    plt.savefig(output_file)

# Plot Speedup vs Buffer Size
def plot_speedup(data, buffer_sizes, prefix_types, output_file):
    fig, ax = plt.subplots(figsize=(10, 6))  # Adjust figure size for better visibility
    
    bar_width = 0.2
    index = np.arange(len(buffer_sizes))
    
    # Plot a bar for each prefix type
    for i, prefix_type in enumerate(prefix_types):
        speedup = data[data['PrefixType'] == prefix_type]['Speedup']
        bars = ax.bar(index + i * bar_width, speedup, bar_width, label=prefix_type)
        
        # Add value labels on top of the bars
        for bar in bars:
            yval = bar.get_height()
            ax.text(bar.get_x() + bar.get_width()/2, yval, round(yval, 2), va='bottom', ha='center')
    
    # Set chart properties
    ax.set_xlabel('Buffer Size')
    ax.set_ylabel('Speedup')
    ax.set_title('Speedup vs Buffer Size')
    ax.set_xticks(index + bar_width / 2 * len(prefix_types))
    ax.set_xticklabels(buffer_sizes)
    ax.legend(loc='upper left')

    # Save the plot to a file
    plt.tight_layout()
    plt.savefig(output_file)

# Call the plotting functions and save plots to file
plot_execution_time(data, buffer_sizes, prefix_types, './perf_plots/execution_time_vs_buffer_size.png')
plot_speedup(data, buffer_sizes, prefix_types, './perf_plots/speedup_vs_buffer_size.png')

print("Plots saved to directory: './perf_plots/'")
