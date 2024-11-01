import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.Arrays;

class SweepTask extends RecursiveAction {
    private int[] buffer;
    private int d;
    private int k;
    private SweepType sweepType;
    private int[] powersOfTwo;

    public SweepTask(int[] buffer, int d, int k, SweepType sweepType, int[] powersOfTwo) {
        this.buffer = buffer;
        this.d = d;
        this.k = k;
        this.sweepType = sweepType;
        this.powersOfTwo = powersOfTwo;
    }

    @Override
    protected void compute() {
        int x = k + powersOfTwo[d + 1] - 1;
        int y = k + powersOfTwo[d] - 1;

        switch (sweepType) {
            case UP:
                buffer[x] += buffer[y];
                break;

            case DOWN:
                int temp = buffer[y];
                buffer[y] = buffer[x];
                buffer[x] += temp;
                break;
        }
    }
}

public class BetterParallelPrefix implements PrefixInterface {

    private long totalReadTime;
    private long totalWriteTime;
    private long totalPrefixTime;
    private long totalSweepTime;

    public BetterParallelPrefix() {
        this.totalReadTime = 0;
        this.totalWriteTime = 0;
        this.totalPrefixTime = 0;
        this.totalSweepTime = 0;
    }

    private static boolean isPowerOfTwo(int n) {
        return (n > 0) && (n & (n - 1)) == 0;
    }

    private static int closestPowerOfTwo(int n) {
        
        if (isPowerOfTwo(n)) {
            return n;
        } else {
            int closestPower = 1;
            while (closestPower <= n) {
                closestPower <<= 1;
            }
            return closestPower >> 1;
        }
    }

    private static int log2(int N) {
        return (int) (Math.log(N) / Math.log(2));
    }

    private int readFileToBuffer(BufferedReader reader, int[] buffer, int bufferSize) throws IOException {
        long startTime = System.nanoTime();

        String line;
        int index = 0;
        while (index < bufferSize && (line = reader.readLine()) != null) {
            buffer[index] = Integer.parseInt(line.trim());
            index++;
        }

        long endTime = System.nanoTime();
        totalReadTime += (endTime - startTime);

        return index;
    }

    private void writeBufferToFile(BufferedWriter writer, int[] buffer, int length) throws IOException {
        long startTime = System.nanoTime();

        for (int i = 0; i < length; i++) {
            writer.write(String.valueOf(buffer[i]));
            writer.newLine();
        }

        long endTime = System.nanoTime();
        totalWriteTime += (endTime - startTime);
    }

    private void sweep(int[] buffer, int length, int d, SweepType sweepType, int[] powersOfTwo) {
        long startTime = System.nanoTime();

        // Creating a list of ForkJoinTask for parallel execution
        List<ForkJoinTask<Void>> tasks = new ArrayList<>();

        // Create tasks for parallel processing and add them to the list
        for (int k = 0; k < length; k += powersOfTwo[d + 1]) {
            tasks.add(new SweepTask(buffer, d, k, sweepType, powersOfTwo));
        }

        // Invoke all tasks to execute them in parallel
        ForkJoinTask.invokeAll(tasks);

        long endTime = System.nanoTime();
        totalSweepTime += (endTime - startTime);

        // No need to shut down the common pool; it's managed by the JVM.
    }

    private int prefixSum(int prefixSum, int[] buffer, int length) {
        long startTime = System.nanoTime();

        if (buffer.length != length) {
            Arrays.fill(buffer, length, buffer.length, 0); // Efficient padding
            length = buffer.length;
        }

        buffer[0] += prefixSum;

        int logLength = log2(length);
        int[] powersOfTwo = new int[logLength + 2]; // Store powers of 2 up to required range
        for (int i = 0; i <= logLength + 1; i++) {
            powersOfTwo[i] = 1 << i;
        }

        // Perform Up Sweep
        for (int d = 0; d < logLength; d++) {
            sweep(buffer, length, d, SweepType.UP, powersOfTwo);
        }

        prefixSum = buffer[length - 1];
        buffer[length - 1] = 0;

        // Perform Down Sweep
        for (int d = logLength - 1; d >= 0; d--) {
            sweep(buffer, length, d, SweepType.DOWN, powersOfTwo);
        }

        // Shift the result left by one position
        System.arraycopy(buffer, 1, buffer, 0, length - 1);
        buffer[length - 1] = prefixSum;

        long endTime = System.nanoTime();
        totalPrefixTime += (endTime - startTime);

        return buffer[length - 1];
    }


    public long getTotalReadTime() {
        return totalReadTime;
    }

    public long getTotalWriteTime() {
        return totalWriteTime;
    }

    public long getTotalPrefixTime() {
        return totalPrefixTime;
    }

    public long getTotalSweepTime() {
        return totalSweepTime;
    }

    public void run(String filename, int bufferSize) {
        String output_file = "./outputs/" + this.getClass().getName() + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename));
             BufferedWriter writer = new BufferedWriter(new FileWriter(output_file))) {

            int prefixSum = 0;
            int length;

            bufferSize = closestPowerOfTwo(bufferSize);
            int[] buffer = new int[bufferSize];

            while ((length = this.readFileToBuffer(reader, buffer, bufferSize)) != 0) {
                prefixSum = this.prefixSum(prefixSum, buffer, length);
                this.writeBufferToFile(writer, buffer, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        int cacheWarmupIterations = 10;
        int numberOfIterations = 10;
        int totalNumberOfIterations = numberOfIterations + cacheWarmupIterations;

        int bufferSize = 1024;
        String input_file = "../inputs/in.txt";

        long totalReadTimeSum = 0;
        long totalWriteTimeSum = 0;
        long totalPrefixTimeSum = 0;
        long totalSweepTimeSum = 0;
        long totalExecutionTimeSum = 0;

        for (int i = 0; i < totalNumberOfIterations; i++) {
            BetterParallelPrefix bpp = new BetterParallelPrefix();

            long startTimeTotal = System.nanoTime();
            bpp.run(input_file, bufferSize);
            long endTimeTotal = System.nanoTime();

            long totalExecutionTime = (endTimeTotal - startTimeTotal);  // Total time in nanoseconds

            // Sum up the times (excluding warmup iterations)
            if (i >= cacheWarmupIterations) {
                totalReadTimeSum += bpp.getTotalReadTime();
                totalWriteTimeSum += bpp.getTotalWriteTime();
                totalPrefixTimeSum += bpp.getTotalPrefixTime();
                totalSweepTimeSum += bpp.getTotalSweepTime();
                totalExecutionTimeSum += totalExecutionTime;
            }
        }

        // Calculate averages by dividing the sum by the number of iterations (excluding warmup iterations)
        long avgReadTime = totalReadTimeSum / numberOfIterations;
        long avgWriteTime = totalWriteTimeSum / numberOfIterations;
        long avgPrefixTime = totalPrefixTimeSum / numberOfIterations;
        long avgSweepTime = totalSweepTimeSum / numberOfIterations;
        long avgExecutionTime = totalExecutionTimeSum / numberOfIterations;

        // Output the averages in milliseconds
        System.out.println("Averages after " + numberOfIterations + " iterations (excluding warmups):");
        System.out.printf("Average Read time      : %d ms%n", avgReadTime / 1000000);  // Convert to milliseconds
        System.out.printf("Average Write time     : %d ms%n", avgWriteTime / 1000000);
        System.out.printf("Average Prefix time    : %d ms%n", avgPrefixTime / 1000000);
        System.out.printf("Average Sweep time     : %d ms%n", avgSweepTime / 1000000);
        System.out.printf("Average Execution time : %d ms%n", avgExecutionTime / 1000000);
    }
}
