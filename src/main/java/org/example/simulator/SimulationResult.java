package org.example.simulator;

import lombok.Getter;

@Getter
public class SimulationResult {

  private final int totalReferences;
  private final int pageFaults;
  private final int pageHits;
  private final int replacements;
  private final int framesCapacity;
  private final List<PageFrame> finalFrames;

  public SimulationResult(int totalReferences, int pageFaults, int pageHits,
      int replacements, int framesCapacity, List<PageFrame> finalFrames) {
    this.totalReferences = totalReferences;
    this.pageFaults = pageFaults;
    this.pageHits = pageHits;
    this.replacements = replacements;
    this.framesCapacity = framesCapacity;
    this.finalFrames = finalFrames;
  }

  public double getPageFaultRate() {
    return totalReferences > 0 ? (double) pageFaults / totalReferences * 100 : 0;
  }

  public double getPageHitRate() {
    return totalReferences > 0 ? (double) pageHits / totalReferences * 100 : 0;
  }
}
