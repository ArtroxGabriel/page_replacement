package org.example.simulator;

import java.util.List;
import lombok.Getter;
import org.example.entities.PageFrame;

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

  public void printSummary(String algorithmName, List<PageFrame> frames) {
    System.out.println("=== Simulation Results ===");
    System.out.println("Algorithm: " + algorithmName.toUpperCase());
    System.out.println("Frames: " + framesCapacity);
    System.out.println("References: " + totalReferences);
    System.out.println("Page Faults: " + pageFaults);
    System.out.printf("Page Fault Rate: %.2f%%\n", getPageFaultRate());
    System.out.println("Page Hits: " + pageHits);
    System.out.printf("Page Hit Rate: %.2f%%\n", getPageHitRate());
    System.out.println("Replacements: " + replacements);
    System.out.println("Final set:");
    System.out.print("frame_ids:");
    for (PageFrame frame : frames) {
      System.out.print("\t" + frame.getIndex());
    }
    System.out.println();
    System.out.print("page_ids: ");
    for (PageFrame frame : frames) {
      int pageId = frame.getPage() != null ? frame.getPage().getId() : -1;
      System.out.print("\t" + pageId);
    }
  }
}
