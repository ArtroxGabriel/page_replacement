package org.example.cli;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import org.example.io.TraceReader;
import org.example.simulator.MemorySimulator;
import org.example.simulator.SimulationResult;
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
      description = "Name of the trace file in resources folder (e.g., silberschatz2001.trace)"
  )
  private String traceFile;

  @Option(
      names = {"--verbose"},
      description = "Display detailed execution information"
  )
  private boolean verbose;

  private final TraceReader traceReader = new TraceReader();

  @Override
  public Integer call() {
    if (frames <= 0) {
      System.err.println("Error: The number of frames must be greater than zero.");
      return 1;
    }

    List<Integer> pageReferences;
    try {
      pageReferences = traceReader.readTrace(traceFile);
    } catch (IOException | IllegalArgumentException e) {
      System.err.println("Error: " + e.getMessage());
      return 1;
    }

    List<Integer> pageReferences;
    try {
      pageReferences = traceReader.readTrace(traceFile);
    } catch (IOException | IllegalArgumentException e) {
      System.err.println("Error: " + e.getMessage());
      return 1;
    }

    if (verbose) {
      System.out.println("=== Configuration ===");
      System.out.println("Algorithm: " + algorithm);
      System.out.println("Frames: " + frames);
      System.out.println("File: src/main/resources/" + traceFile);
      System.out.println("References loaded: " + pageReferences.size());
      System.out.println();
    }

    // Executar simulação
    MemorySimulator simulator = new MemorySimulator(algorithm, frames, verbose);
    SimulationResult result = simulator.simulate(pageReferences);

    // Exibir resultados
    OutputReporter reporter = new OutputReporter();
    reporter.printReport(algorithm, result);

    return 0;
  }
}