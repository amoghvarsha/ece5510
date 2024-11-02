#!/bin/bash

# Get the directory where this script is located
SCRIPT_DIR=$(dirname "$0")

# Output file with a simpler timestamp for each run
OUTPUT_FILE="$SCRIPT_DIR/../outputs/task1_results.txt"

MODE="normal"
LOCK="BackoffLock"

# Array of thread counts to test
THREAD_COUNTS=(8 12 16 24 32 48 64)
#THREAD_COUNTS=(8 16)

# Total operations to perform
TOTAL_OPERATIONS=32000

# Create the output directory if it doesn't exist
mkdir -p "$SCRIPT_DIR/../outputs"

cat /dev/null > $OUTPUT_FILE

echo "Mode: $MODE" >> $OUTPUT_FILE
echo "Lock: $LOCK" >> $OUTPUT_FILE
echo "" >> $OUTPUT_FILE

for lock_type in "Exponential" "Fibonacci" "Linear" "Fixed 50" "Polynomial 50"; do
    for threads in "${THREAD_COUNTS[@]}"; do
        result=$(java -cp "$SCRIPT_DIR/../homework4/build/libs/homework4.jar" edu.vt.ece.hw4.Benchmark $MODE $LOCK $threads $TOTAL_OPERATIONS $lock_type)
        
        echo "---------------------------------" >> $OUTPUT_FILE
        echo "Lock Type: $lock_type" >> $OUTPUT_FILE
        echo "Threads: $threads"     >> $OUTPUT_FILE 

        echo "Result: "              >> $OUTPUT_FILE
        echo "$result"               >> $OUTPUT_FILE
        echo "---------------------------------" >> $OUTPUT_FILE
    done
done

echo "" >> $OUTPUT_FILE
