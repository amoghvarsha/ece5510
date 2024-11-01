import java.io.*;
import java.nio.file.*;
import java.util.stream.Stream;

public class PrefixGenerator {

    // Method to calculate prefix sum and write to output file
    public void run(String inputFile, String outputFile) {
        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))
        ) {
            String line;
            int prefixSum = 0;

            // Process each line in the file
            while ((line = reader.readLine()) != null) {
                int value = Integer.parseInt(line.trim());

                // Perform the prefix sum calculation
                prefixSum += value;

                // Write the prefix sum result to the output file
                writer.write(String.valueOf(prefixSum));
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle all files in the input directory
    public void processDirectory(String inputDir, String outputDir) {
        try (Stream<Path> paths = Files.walk(Paths.get(inputDir))) {
            paths
                .filter(Files::isRegularFile) // Process only regular files
                .forEach(inputPath -> {
                    // Get the input file name
                    String inputFileName = inputPath.getFileName().toString();

                    // Replace "in" prefix with "out" for output file name
                    String outputFileName = inputFileName.replace("in", "out");

                    // Create the full output file path
                    Path outputPath = Paths.get(outputDir, outputFileName);

                    // Generate the prefix sum for the current file
                    run(inputPath.toString(), outputPath.toString());
                });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String inputDir = "../inputs"; // Path to input directory
        String outputDir = "./outputs"; // Path to output directory

        // Create the output directory if it doesn't exist
        File outputDirectory = new File(outputDir);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        PrefixGenerator pg = new PrefixGenerator();
        pg.processDirectory(inputDir, outputDir);

        System.out.println("Prefix sum generation complete for all files in the directory.");
    }
}
