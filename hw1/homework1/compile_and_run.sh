#!/bin/bash

# Ensure the script stops if any command fails
set -e

rm -rf compile_and_run_output.log
rm -rf sources.txt

# Redirect output to a log file
exec > >(tee -i compile_and_run_output.log)
exec 2>&1

# Set the input file and stats file inside the script
input_file="./inputs/in.txt"
stats_file="./stats/benchmark_results.csv"

# Set the expected output file for comparison
expected_output="./outputs/out.txt"

output_files=(./outputs/SequentialPrefix.txt ./outputs/ParallelPrefix.txt ./outputs/BetterParallelPrefix.txt ./outputs/ParallelPrefixInternal.txt)

find ./src -name "*.java" > sources.txt

mkdir -p out
rm -rf ./out/*

javac @sources.txt -d ./out

echo "Compilation successful. Running the program with input file: $input_file and stats file: $stats_file"
java -cp ./out Benchmark "$input_file" "$stats_file"

for output_file in "${output_files[@]}"
do
    if diff "$expected_output" "$output_file" > /dev/null; then
        echo "OK - $output_file matches expected output."
    else
        echo "Mismatch - $output_file does not match expected output."
    fi
done

if [ -d "./perf_plots" ]; then
    rm -f ./perf_plots/*.png
    echo ""
    echo "Deleted previous plot PNG files in perf_plots directory."
  
fi

python3 ./perf_plots/plot_benchmarks.py

echo ""
echo "Program Ended"