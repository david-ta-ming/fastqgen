package net.noisynarwhal.fastqgen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * A utility class for generating simulated FASTQ files containing random DNA sequences.
 * FASTQ is a text-based format for storing both a biological sequence (usually nucleotide sequence)
 * and its corresponding quality scores. This generator creates valid FASTQ files with
 * random sequences and quality scores.
 */
public class Main {
    /** Array of valid nucleotide characters used for sequence generation */
    private static final char[] NUCLEOTIDES = {'A', 'T', 'C', 'G'};
    
    /** String containing valid quality score characters (ASCII 33-73) */
    private static final String QUALITY_SCORES = "!" + new String(new char[40]).replace("\0", "I");

    /**
     * Main entry point for the FASTQ file generator.
     * 
     * @param args Command line arguments:
     *             args[0] - sequenceLength: Length of each DNA sequence in base pairs
     *             args[1] - fileSizeInMB: Desired output file size in megabytes
     * @throws NumberFormatException If the provided arguments are not valid numbers
     * @throws IOException If there's an error writing the output file
     */
    public static void main(String[] args) {
        // Validate command line arguments
        if (args.length != 2) {
            System.out.println("Usage: java Main <sequenceLength> <fileSizeInMB>");
            System.out.println("  sequenceLength: Length of each DNA sequence in base pairs (e.g., 100)");
            System.out.println("  fileSizeInMB: Desired output file size in megabytes (e.g., 10)");
            System.exit(1);
        }

        try {
            // Parse and validate input parameters
            final int sequenceLength = Integer.parseInt(args[0]);
            final long fileSizeInMB = Long.parseLong(args[1]);
            
            // Ensure parameters are positive
            if (sequenceLength <= 0 || fileSizeInMB <= 0) {
                System.err.println("Error: Both sequence length and file size must be positive numbers");
                System.exit(1);
            }

            // Generate output filename and convert file size to bytes
            final String filename = generateRandomFilename();
            final long fileSize = fileSizeInMB * 1024L * 1024L; // Convert MB to bytes (1 MB = 1024 KB, 1 KB = 1024 bytes)

            // Generate the FASTQ file
            generateFastqFile(filename, sequenceLength, fileSize);
            
            // Print success message with details
            System.out.println("FASTQ file generated successfully: " + filename);
            System.out.println("Sequence length: " + sequenceLength + " bp");
            System.out.println("Target file size: " + fileSizeInMB + " MB");
        } catch (NumberFormatException e) {
            System.err.println("Error: Please provide valid numbers for sequence length and file size");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error writing FASTQ file: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Generates a unique filename for the output FASTQ file using the current timestamp.
     * 
     * @return A string in the format "simulated_YYYYMMDDHHMMSS.fastq"
     */
    private static String generateRandomFilename() {
        // Create timestamp in format YYYYMMDDHHMMSS
        final String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return "simulated_" + timestamp + ".fastq";
    }

    /**
     * Generates a FASTQ file with random DNA sequences and quality scores.
     * The file will contain sequences until the specified file size is reached.
     * 
     * @param filename The name of the output file to create
     * @param sequenceLength The length of each DNA sequence in base pairs
     * @param fileSize The target file size in bytes
     * @throws IOException If there's an error writing to the file
     */
    public static void generateFastqFile(String filename, int sequenceLength, long fileSize) throws IOException {
        // Initialize random number generator
        final Random random = new Random();
        
        // Use try-with-resources to ensure proper resource cleanup
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            long currentSize = 0;
            int sequenceCount = 0;
            
            // Continue generating sequences until target file size is reached
            while (currentSize < fileSize) {
                // Generate FASTQ entry components
                final String sequenceId = "@SEQ" + (sequenceCount + 1);  // FASTQ format requires '@' prefix
                final String sequence = generateRandomSequence(sequenceLength, random);
                final String plusLine = "+";  // FASTQ format requires '+' line
                final String quality = generateQualityScores(sequenceLength, random);

                // Build the complete FASTQ entry
                final StringBuilder fastqEntry = new StringBuilder();
                fastqEntry.append(sequenceId).append("\n")  // Sequence identifier
                        .append(sequence).append("\n")      // DNA sequence
                        .append(plusLine).append("\n")      // Plus line
                        .append(quality).append("\n");      // Quality scores

                // Write entry to file and update size counter
                writer.write(fastqEntry.toString());
                currentSize += fastqEntry.length();
                sequenceCount++;
            }
            // Ensure all data is written to disk
            writer.flush();
        }
    }

    /**
     * Generates a random DNA sequence of specified length.
     * 
     * @param length The desired length of the sequence
     * @param random A Random instance for generating random numbers
     * @return A string containing a random sequence of nucleotides (A, T, C, G)
     */
    private static String generateRandomSequence(int length, Random random) {
        // Pre-allocate StringBuilder with exact capacity for better performance
        StringBuilder sequence = new StringBuilder(length);
        
        // Generate random sequence by selecting nucleotides with equal probability
        for (int i = 0; i < length; i++) {
            sequence.append(NUCLEOTIDES[random.nextInt(NUCLEOTIDES.length)]);
        }
        return sequence.toString();
    }

    /**
     * Generates random quality scores for a sequence of specified length.
     * Quality scores are represented as ASCII characters, where higher ASCII values
     * indicate higher quality. The scores range from '!' (ASCII 33) to 'I' (ASCII 73).
     * 
     * @param length The length of the sequence to generate quality scores for
     * @param random A Random instance for generating random numbers
     * @return A string of quality score characters
     */
    private static String generateQualityScores(int length, Random random) {
        // Pre-allocate StringBuilder with exact capacity for better performance
        StringBuilder quality = new StringBuilder(length);
        
        // Generate random quality scores by selecting from valid quality characters
        for (int i = 0; i < length; i++) {
            quality.append(QUALITY_SCORES.charAt(random.nextInt(QUALITY_SCORES.length())));
        }
        return quality.toString();
    }
}
