import java.io.*;
import java.util.*;
import java.util.function.IntBinaryOperator;

public class ParallelPrefixInternal implements PrefixInterface {

    private long totalReadTime;
    private long totalWriteTime;
    private long totalPrefixTime;
    
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
    
        IntBinaryOperator intBinaryOperator = (a, b) -> a + b;

        long startTime = System.nanoTime();
        buffer[0] += prefixSum;
        Arrays.parallelPrefix(buffer, 0, length, intBinaryOperator);  // Perform parallel prefix on the array
        long endTime = System.nanoTime();

        totalPrefixTime += (endTime - startTime);

        return buffer[length-1];

    }
    
    // Getters for time tracking
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
            int length    = 0;
                
            int[] buffer  = new int[bufferSize];

            while ((length = this.readFileToBuffer(reader, buffer, bufferSize)) != 0) {
                prefixSum = this.prefixSum(prefixSum, buffer, length);
                this.writeBufferToFile(writer, buffer, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main method
    public static void main(String[] args) {

        int cacheWarmupIterations = 10;
        int numberOfIterations    = 10;
        int totalNumberOfIterations = numberOfIterations + cacheWarmupIterations;
    
        int bufferSize = 1024;
        String input_file = "../inputs/in.txt";
    
        long totalReadTimeSum = 0;
        long totalWriteTimeSum = 0;
        long totalPrefixTimeSum = 0;
        long totalExecutionTimeSum = 0;
    
        for (int i = 0; i < totalNumberOfIterations; i++) {
            
            ParallelPrefixInternal ppi = new ParallelPrefixInternal();
    
            long startTimeTotal = System.nanoTime();
            ppi.run(input_file, bufferSize);
            long endTimeTotal = System.nanoTime();
    
            long totalExecutionTime = (endTimeTotal - startTimeTotal);  // Total time in nanoseconds
    
            // Sum up the times (excluding warmup iterations)
            if (i >= cacheWarmupIterations) {
                totalReadTimeSum += ppi.getTotalReadTime();
                totalWriteTimeSum += ppi.getTotalWriteTime();
                totalPrefixTimeSum += ppi.getTotalPrefixTime();
                totalExecutionTimeSum += totalExecutionTime;
            }
        }
    
        // Calculate averages by dividing by the number of iterations (excluding warmup iterations)
        long avgReadTime = totalReadTimeSum / numberOfIterations;
        long avgWriteTime = totalWriteTimeSum / numberOfIterations;
        long avgPrefixTime = totalPrefixTimeSum / numberOfIterations;
        long avgExecutionTime = totalExecutionTimeSum / numberOfIterations;
    
        System.out.println("Averages after " + numberOfIterations + " iterations (excluding warmups):");
        System.out.printf("Average Read time      : %d ms%n", avgReadTime / 1000000);
        System.out.printf("Average Write time     : %d ms%n", avgWriteTime / 1000000);
        System.out.printf("Average Prefix time    : %d ms%n", avgPrefixTime / 1000000);
        System.out.printf("Average Execution time : %d ms%n", avgExecutionTime / 1000000);
    }
}
