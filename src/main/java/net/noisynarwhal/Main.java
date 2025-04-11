package net.noisynarwhal;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Main {
    // Characters used to create random DNA sequences
    private static final char[] NUCLEOTIDES = {'A', 'T', 'C', 'G'};
    private static final String QUALITY_SCORES = "!" + new String(new char[40]).replace("\0", "I");

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Main <sequenceLength> <fileSizeInMB>");
            System.out.println("  sequenceLength: Length of each DNA sequence in base pairs (e.g., 100)");
            System.out.println("  fileSizeInMB: Desired output file size in megabytes (e.g., 10)");
            System.exit(1);
        }

        try {
            final int sequenceLength = Integer.parseInt(args[0]);
            final long fileSizeInMB = Long.parseLong(args[1]);
            
            if (sequenceLength <= 0 || fileSizeInMB <= 0) {
                System.err.println("Error: Both sequence length and file size must be positive numbers");
                System.exit(1);
            }

            final String filename = generateRandomFilename();
            final long fileSize = fileSizeInMB * 1024L * 1024L; // Convert MB to bytes

            generateFastqFile(filename, sequenceLength, fileSize);
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

    // Method to generate a random filename with a timestamp
    private static String generateRandomFilename() {
        final String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return "simulated_" + timestamp + ".fastq";
    }

    // Method to generate a FASTQ file with random sequences and quality scores
    public static void generateFastqFile(String filename, int sequenceLength, long fileSize) throws IOException {
        final Random random = new Random();
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            long currentSize = 0;
            int sequenceCount = 0;
            while (currentSize < fileSize) {
                final String sequenceId = "@SEQ" + (sequenceCount + 1);
                final String sequence = generateRandomSequence(sequenceLength, random);
                final String plusLine = "+";
                final String quality = generateQualityScores(sequenceLength, random);

                final StringBuilder fastqEntry = new StringBuilder();
                fastqEntry.append(sequenceId).append("\n")
                        .append(sequence).append("\n")
                        .append(plusLine).append("\n")
                        .append(quality).append("\n");

                writer.write(fastqEntry.toString());
                currentSize += fastqEntry.length();
                sequenceCount++;
            }
            writer.flush();
        }
    }

    // Method to generate a random DNA sequence
    private static String generateRandomSequence(int length, Random random) {
        StringBuilder sequence = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sequence.append(NUCLEOTIDES[random.nextInt(NUCLEOTIDES.length)]);
        }
        return sequence.toString();
    }

    // Method to generate a random quality score for the sequence
    private static String generateQualityScores(int length, Random random) {
        StringBuilder quality = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            quality.append(QUALITY_SCORES.charAt(random.nextInt(QUALITY_SCORES.length())));
        }
        return quality.toString();
    }
}
