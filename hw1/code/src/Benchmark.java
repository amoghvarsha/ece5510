import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Benchmark {

    /* Total number of elements to hold in memory at a time */
    public static int[] bufferSizes = {1024, 16384, 131072};

    public static PrefixInterface[] prefixSums = {
        new SequentialPrefix(), 
        new ParallelPrefix(), 
        new ParallelPrefixInternal(), 
        new BetterParallelPrefix()
    };

    /* Find the average of numRuns # of runs for each type of prefix sum */
    public static int numWarmupRuns = 10;  // Warmup runs to stabilize cache
    public static int numRuns = 10;        // Actual runs to measure performance

    public static void main(String[] args) {

        // Ensure that both input and stats files are provided as arguments
        if (args.length < 2) {
            System.err.println("Error: Please provide the input file and stats file as command-line arguments.");
            System.err.println("Usage: java Benchmark <inputFile> <statsFile>");
            System.exit(1);  // Exit with an error code
        }

        String inputFileName = args[0]; // First argument: input file
        String statsFile = args[1]; // Second argument: stats file

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(statsFile))) {
            writer.write("PrefixType,BufferSize,AverageTime(ms),Speedup\n"); // Write header

            System.out.printf("Running benchmarks...\n");

            double[] seqTime = new double[bufferSizes.length];  // To store SequentialPrefix execution times in ms

            /* Run for various buffer sizes  */
            for (int i = 0; i < bufferSizes.length; i++) {
                int bufferSize = bufferSizes[i];
                System.out.printf("Using buffer size of %d...\n\n", bufferSize);

                for (PrefixInterface s : prefixSums) {
                    System.out.printf("Running %s...\n", s.getClass().getName());

                    // Warmup phase
                    for (int warmup = 0; warmup < numWarmupRuns; warmup++) {
                        s.run(inputFileName, bufferSize);  // Run to warm up the cache
                    }

                    // Benchmarking phase
                    double execTime = 0;
                    double averageTime;
                    double speedup = 0;

                    for (int k = 0; k < numRuns; k++) {
                        long startTime = System.nanoTime();  // Use nanoTime for better accuracy
                        s.run(inputFileName, bufferSize);
                        long endTime = System.nanoTime();

                        // Convert to milliseconds when adding to execTime
                        execTime += (endTime - startTime) / 1_000_000.0;
                    }

                    averageTime = execTime / numRuns;  // Average execution time in milliseconds

                    // Store SequentialPrefix time for speedup calculation
                    if (s instanceof SequentialPrefix) {
                        seqTime[i] = averageTime;
                        speedup = 1.0;  // Speedup is 1 for SequentialPrefix
                    } else {
                        speedup = seqTime[i] / averageTime;  // Calculate speedup
                    }

                    /* Write the result to the file */
                    writer.write(s.getClass().getSimpleName() + "," + bufferSize + "," + averageTime + "," + speedup + "\n");

                    /* Print results */
                    System.out.printf("Average time: %.3f ms\n", averageTime);  // Now in milliseconds
                    System.out.printf("Speedup: %.2f\n", speedup);
                    System.out.println("----------------------------------------------------------\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
