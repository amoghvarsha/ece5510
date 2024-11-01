#!/bin/bash

# Get the directory where this script is located
script_dir=$(dirname "$0")

# Run tests for L-exclusion using LBakery lock
lock="LBakery"
threads="8"
L="4"

# Create the output directory if it doesn't exist
mkdir -p "$script_dir/../outputs"

# Output file with a timestamp for each run
output_file="$script_dir/../outputs/task3_results_$(date +"%Y%m%d_%H%M").txt"

result=$(java -cp "$script_dir/../Homework2/build/libs/homework2-1.0-SNAPSHOT.jar" edu.vt.ece.Test $lock $threads $L)
echo "$result" >> $output_file

echo "[$(date +"%Y-%m-%d %H:%M")] Task 3 data collection complete. Results saved in $output_file."
