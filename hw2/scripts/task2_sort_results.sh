#!/bin/bash

# Define the pattern for the input files in the outputs directory
input_dir="outputs"
output_dir="outputs"
files=("$input_dir"/task2_*.txt)

# Loop through each file that matches the pattern
for file in "${files[@]}"; do
  # Initialize variables for storing the thread count and capturing data
  threads=""
  capture=false
  buffer=""

  # Read the file line by line
  while IFS= read -r line; do
    # Capture the timestamp from the lines that contain the date and time
    if [[ $line =~ ^\[([0-9-]+)\ ([0-9:]+)\] ]]; then
      timestamp="${BASH_REMATCH[1]} ${BASH_REMATCH[2]}"
    fi

    # Detect when a section begins (e.g., "Running with X threads...")
    if [[ $line =~ Running\ with\ ([0-9]+)\ threads ]]; then
      threads=${BASH_REMATCH[1]}
      capture=true
      buffer="Run Time: $timestamp\n$line\n"
    elif [[ $line == "---------------------------------" ]]; then
      # If we reach the section delimiter, write the buffered section to the appropriate file
      if [[ $capture == true ]]; then
        buffer+="$line\n"
        output_file="$output_dir/threads_${threads}.txt"
        echo -e "$buffer" >> "$output_file"
        capture=false
      fi
    elif [[ $capture == true ]]; then
      # Continue capturing data for the current section
      buffer+="$line\n"
    fi

  done < "$file"
done

echo "Data separation by threads complete!"
