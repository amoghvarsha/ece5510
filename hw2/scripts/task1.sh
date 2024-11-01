#!/bin/bash

# Get the directory where this script is located
script_dir=$(dirname "$0")

# Array of thread counts to test
thread_counts=(2 4 8 16 20 32)

# Total operations to perform
total_operations=32000

# Create the output directory if it doesn't exist
mkdir -p "$script_dir/../outputs"

# Output file with a simpler timestamp for each run
output_file="$script_dir/../outputs/task1_results_$(date +"%Y%m%d_%H%M").txt"

# Run tests for both Bakery and Filter locks
for lock in "Bakery" "Filter"; do
    echo "[$(date +"%Y-%m-%d %H:%M")] Testing $lock lock" >> $output_file
    for threads in "${thread_counts[@]}"; do
        echo "[$(date +"%Y-%m-%d %H:%M")] Running with $threads threads..." >> $output_file
        result=$(java -cp "$script_dir/../Homework2/build/libs/homework2-1.0-SNAPSHOT.jar" edu.vt.ece.Test2 $lock $threads $total_operations)
        echo "Threads: $threads, Total: $total_operations, Result: " >> $output_file
        echo "$result" >> $output_file
        echo "---------------------------------" >> $output_file
    done
    echo "" >> $output_file
done

echo "[$(date +"%Y-%m-%d %H:%M")] Task 1 data collection complete. Results saved in $output_file."
