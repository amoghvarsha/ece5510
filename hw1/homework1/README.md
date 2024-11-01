## How to Run

### Requirements:
- **Java (JDK)** and **Python 3** installed.

### Steps:
1. Make the script executable:
   ```bash
   chmod +x compile_and_run.sh
   ```

2. Run the script from code directory:
   ```bash
   ./compile_and_run.sh
   ```

This will:
- Compile all Java files.
- Run the benchmarks for each implementation.
- Compare the output files.
- Generate performance plots in `perf_plots/`.

## Files and Directories

- **src/**: Contains Java source code.
- **out/**: Compiled `.class` files.
- **outputs/**: Results of each implementation.
- **perf_plots/**: Performance plots and Python script to generate them.
- **stats/**: Benchmark results and logs.

## Answer to Question 4

The performance charts can be found in the `perf_plots/` directory:
- **execution_time_vs_buffer_size.png**: Displays the execution time for each implementation as the buffer size changes.
- **speedup_vs_buffer_size.png**: Shows the speedup achieved by each implementation compared to the sequential version.


## Answer to Question 5

To improve the performance of **ParallelPrefix** and bring it closer to **ParallelPrefixInternal**, we implemented **BetterParallelPrefix** using the **ForkJoinPool** framework. This replaces manual thread creation and management with a more efficient task execution model tailored for divide-and-conquer algorithms. **ForkJoinPool** dynamically balances workloads across threads, reducing overhead and improving synchronization during the up and down sweeps. As a result, **BetterParallelPrefix** significantly reduces execution time, particularly in the prefix computation and sweeping phases, offering improved scalability and performance compared to the original **ParallelPrefix** implementation.
