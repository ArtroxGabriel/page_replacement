package org.example.cli;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "pager",
    mixinStandardHelpOptions = true,
    version = "pager 1.0",
    description = "Page replacement algorithm simulator"
)
public class Pager implements Callable<Integer> {

  @Option(
      names = {"--algo"},
      required = true,
      description = "Replacement algorithm: FIFO, LRU, OPTIMAL, SECOND_CHANCE, CLOCK, NRU, LFU, MFU"
  )
  private String algorithm;

  @Option(
      names = {"--frames"},
      required = true,
      description = "Number of page frames available in physical memory"
  )
  private int frames;

  @Option(
      names = {"--trace"},
      required = true,
      description = "Path to the file containing the access sequence"
  )
  private File traceFile;

  @Option(
      names = {"--verbose"},
      required = false,
      description = "Display detailed execution information"
  )
  private boolean verbose;

  @Override
  public Integer call() throws Exception {
    if (frames <= 0) {
      System.err.println("Error: The number of frames must be greater than zero.");
      return 1;
    }

    if (!traceFile.exists() || !traceFile.isFile()) {
      System.err.println("Error: Trace file not found: " + traceFile.getPath());
      return 1;
    }

    List<Integer> pageReferences;
    try {
      pageReferences = Files.readAllLines(traceFile.toPath())
          .stream()
          .map(String::trim)
          .filter(line -> !line.isEmpty())
          .map(Integer::parseInt)
          .toList();
    } catch (NumberFormatException e) {
      System.err.println("Error: The trace file contains invalid values.");
      return 1;
    }

    if (pageReferences.isEmpty()) {
      System.err.println("Error: The trace file is empty.");
      return 1;
    }

    if (verbose) {
      System.out.println("=== Configuration ===");
      System.out.println("Algorithm: " + algorithm.toUpperCase());
      System.out.println("Frames: " + frames);
      System.out.println("File: " + traceFile.getPath());
      System.out.println("References loaded: " + pageReferences.size());
      System.out.println();
    }

    // TODO: Execute the selected algorithm with the provided parameters
    System.out.println("Algorithm: " + algorithm.toUpperCase());
    System.out.println("Frames: " + frames);
    System.out.println("References: " + pageReferences.size());

    return 0;
  }
}