package org.example.simulator;

import lombok.Getter;

@Getter
public class SimulationResult {

  private final int totalReferences;
  private final int pageFaults;
  private final int pageHits;
  private final int replacements;
  private final int framesCapacity;

  public SimulationResult(int totalReferences, int pageFaults, int pageHits,
      int replacements, int framesCapacity) {
    this.totalReferences = totalReferences;
    this.pageFaults = pageFaults;
    this.pageHits = pageHits;
    this.replacements = replacements;
    this.framesCapacity = framesCapacity;
  }

  public double getPageFaultRate() {
    return totalReferences > 0 ? (double) pageFaults / totalReferences * 100 : 0;
  }

  public double getPageHitRate() {
    return totalReferences > 0 ? (double) pageHits / totalReferences * 100 : 0;
  }

  public void printSummary(String algorithmName) {
    System.out.println("=== Simulation Results ===");
    System.out.println("Algorithm: " + algorithmName);
    System.out.println("Frames: " + framesCapacity);
    System.out.println("Total References: " + totalReferences);
    System.out.println("Page Faults: " + pageFaults);
    System.out.println("Page Hits: " + pageHits);
    System.out.println("Replacements: " + replacements);
    System.out.printf("Page Fault Rate: %.2f%%\n", getPageFaultRate());
    System.out.printf("Page Hit Rate: %.2f%%\n", getPageHitRate());
  }
}
