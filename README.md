# FastqGen

A Java-based FASTQ file generator for bioinformatics testing and development.

## Overview

FastqGen is a lightweight tool that generates simulated FASTQ files with random DNA sequences. FASTQ files are text-based format for storing both biological sequence data (usually nucleotide sequence) and its corresponding quality scores. This tool is particularly useful for:

- Testing bioinformatics pipelines
- Benchmarking sequence analysis tools
- Development and debugging of NGS data processing applications
- Educational purposes in bioinformatics

## Features

- Generates valid FASTQ format files
- Command-line configurable sequence length and file size
- Random DNA sequence generation using standard nucleotides (A, T, C, G)
- Phred quality score simulation
- Automatic timestamp-based file naming

## Usage

The program requires two command-line arguments:
```bash
java Main <sequenceLength> <fileSizeInMB>
```

Parameters:
- `sequenceLength`: Length of each DNA sequence in base pairs (e.g., 100)
- `fileSizeInMB`: Desired output file size in megabytes (e.g., 10)

Example:
```bash
java Main 150 20    # Generates sequences of 150bp with a total file size of 20MB
```

The output filename is automatically generated with format: `simulated_YYYYMMDDHHMMSS.fastq`

### Output Format

Each entry in the generated FASTQ file consists of four lines:

1. Sequence identifier (starts with '@')
2. Raw sequence letters
3. Separator line (starts with '+')
4. Quality scores (encoded in ASCII)

Example:
```
@SEQ1
ATCGATCG...
+
IIIIIII...
```

## Building and Running

### Prerequisites

- Java JDK 8 or higher
- Maven (for building)

### Building the Project

```bash
mvn clean package
```

### Running the Application

After building, you can run the application using:

```bash
java -jar fastqgen.jar <sequenceLength> <fileSizeInMB>
```

Example:
```bash
java -jar fastqgen.jar 100 10    # Generates 100bp sequences in a 10MB file
```

## Technical Details

- Quality scores are generated using Phred-like quality values
- Sequences are randomly generated using equal probabilities for A, T, C, G
- Each generated file includes a unique timestamp in its name for easy identification

## License

MIT License

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. 