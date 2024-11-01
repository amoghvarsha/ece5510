import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SequentialPrefix implements PrefixInterface {

    private long totalReadTime;
    private long totalWriteTime;
    private long totalPrefixTime;

    public SequentialPrefix() {
        this.totalReadTime = 0;
        this.totalWriteTime = 0;
        this.totalPrefixTime = 0;
    }

    private int readFileToBuffer(BufferedReader reader, int[] buffer, int bufferSize) throws IOException {
        long startTime = System.nanoTime();

        String line;
        int index = 0;

        // Read up to bufferSize lines into the buffer
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

    private int prefixSum(int prefixSum, int[] buffer, int length) {
        long startTime = System.nanoTime();

        // Perform the prefix sum
        for (int i = 0; i < length; i++) {
            buffer[i] += prefixSum;
            prefixSum = buffer[i];
        }

        long endTime = System.nanoTime();
        totalPrefixTime += (endTime - startTime);

        return prefixSum;
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

    public void run(String filename, int bufferSize) {
        String output_file = "./outputs/" + this.getClass().getName() + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename));
             BufferedWriter writer = new BufferedWriter(new FileWriter(output_file))) {

            int prefixSum = 0;
            int length;

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

        int bufferSize = 1024; // Adjust this value based on your requirement
        String input_file = "../inputs/in.txt";

        long totalReadTimeSum = 0;
        long totalWriteTimeSum = 0;
        long totalPrefixTimeSum = 0;
        long totalExecutionTimeSum = 0;

        for (int i = 0; i < totalNumberOfIterations; i++) {
            SequentialPrefix sp = new SequentialPrefix();

            long startTimeTotal = System.nanoTime();
            sp.run(input_file, bufferSize);
            long endTimeTotal = System.nanoTime();

            long totalExecutionTime = (endTimeTotal - startTimeTotal);  // Total time in nanoseconds

            // Sum up the times (excluding warmup iterations)
            if (i >= cacheWarmupIterations) {
                totalReadTimeSum += sp.getTotalReadTime();
                totalWriteTimeSum += sp.getTotalWriteTime();
                totalPrefixTimeSum += sp.getTotalPrefixTime();
                totalExecutionTimeSum += totalExecutionTime;
            }
        }

        // Calculate averages by dividing the sum by the number of iterations (excluding warmup iterations)
        long avgReadTime = totalReadTimeSum / numberOfIterations;
        long avgWriteTime = totalWriteTimeSum / numberOfIterations;
        long avgPrefixTime = totalPrefixTimeSum / numberOfIterations;
        long avgExecutionTime = totalExecutionTimeSum / numberOfIterations;

        // Output the averages in milliseconds
        System.out.println("Averages after " + numberOfIterations + " iterations (excluding warmups):");
        System.out.printf("Average Read time      : %d ms%n", avgReadTime / 1000000);  // Convert to milliseconds
        System.out.printf("Average Write time     : %d ms%n", avgWriteTime / 1000000);
        System.out.printf("Average Prefix time    : %d ms%n", avgPrefixTime / 1000000);
        System.out.printf("Average Execution time : %d ms%n", avgExecutionTime / 1000000);
    }
}
