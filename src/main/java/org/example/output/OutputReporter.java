package org.example.output;

import java.util.List;
import org.example.entities.PageFrame;
import org.example.simulator.SimulationResult;

/**
 * OutputReporter is responsible for printing the final summary of a simulation in the format requested:
 * Algorithm
 * Number of frames
 * Total page references
 * Number of page faults
 * Fault rate (%)
 * Evictions
 * Final state of frames (frame_ids / page_ids)
 */

public class OutputReporter {

  public void printReport(String algorithm, SimulationResult result) {
    System.out.println("=== Simulation Summary ===");
    System.out.printf("Algorithm: %s%n", algorithm);
    System.out.printf("Number of frames: %d%n", result.getFramesCapacity());
    System.out.printf("Total page references: %d%n", result.getTotalReferences());
    System.out.printf("Number of page faults: %d%n", result.getPageFaults());
    System.out.printf("Fault rate: %.2f%%%n", result.getPageFaultRate());
    System.out.printf("Evictions: %d%n", result.getReplacements());
    System.out.print("Final state of frames (frame_id/page_id): ");
    
    List<PageFrame> frames = result.getFinalFrames();

    // Print frame ids
    System.out.print("frame_ids: ");
    for (PageFrame f : frames) {
        System.out.printf("%4d ", f.getIndex());
    }
    System.out.println();

    // Print page ids
    System.out.print("page_ids:  ");
    for (PageFrame f : frames) {
        System.out.printf("%4d" , f.getPageId());
    }
    System.out.println();
  }
}