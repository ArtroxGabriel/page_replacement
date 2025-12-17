package org.example.strategy.algorithms;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.PageReplacementStrategy;

@Log4j2
public class NRUStrategy implements PageReplacementStrategy {

  private int referenceCount = 0;
  private static final int RESET_INTERVAL = 5; // Reset R bit every 5 references

  @Override
  public String getAlgorithmName() {
    return "Not Recently Used (NRU)";
  }

  @Override
  public ReplacementResult referencePage(Page page, List<PageFrame> frames, int framesCapacity,
      List<Integer> referenceString, boolean isFault) {

    // Periodically reset R bits (simulating clock interrupt)
    referenceCount++;
    if (referenceCount >= RESET_INTERVAL) {
      resetReferenceBits(frames);
      referenceCount = 0;
    }

    // If it's a hit, set R bit (and M bit if it's a write)
    if (!isFault) {
      for (PageFrame frame : frames) {
        if (frame.getPageId() == page.getId()) {
          frame.setReferenceBit(1);

          // If page is modified, set M bit (assuming writes are marked in Page)
          boolean wasModified = Math.random() < 0.4;
          if (wasModified) {
            page.setModified(true);
            frame.setModifyBit(1);
          }

          return new ReplacementResult(false, frame.getIndex(), -1);
        }
      }
    }

    boolean isModified = Math.random() < 0.4;
    page.setModified(isModified);

    // If there's an empty frame, load there
    for (PageFrame frame : frames) {
      if (frame.isEmpty()) {
        frame.accessPage(page, 0);
        frame.setReferenceBit(1);
        frame.setModifyBit(page.isModified() ? 1 : 0);
        return emptyFrameResult(frame.getIndex(), page, frames);
      }
    }

    // No empty frame, need to replace using NRU algorithm
    PageFrame victim = getVictimPage(frames, page, referenceString, 0);
    int victimId = victim.getPageId();

    victim.accessPage(page, 0);
    victim.setReferenceBit(1);
    victim.setModifyBit(page.isModified() ? 1 : 0);

    return new ReplacementResult(true, victim.getIndex(), victimId);
  }

  @Override
  public PageFrame getVictimPage(List<PageFrame> frames, Page page, List<Integer> referenceString,
      int currentIndex) {

    // Classify pages into 4 classes based on R and M bits
    List<PageFrame> class0 = new ArrayList<>(); // R=0, M=0 (not referenced, not modified)
    List<PageFrame> class1 = new ArrayList<>(); // R=0, M=1 (not referenced, modified)
    List<PageFrame> class2 = new ArrayList<>(); // R=1, M=0 (referenced, not modified)
    List<PageFrame> class3 = new ArrayList<>(); // R=1, M=1 (referenced, modified)

    for (PageFrame frame : frames) {
      if (!frame.isEmpty()) {
        int r = frame.getReferenceBit();
        int m = frame.getModifyBit();

        if (r == 0 && m == 0) {
          class0.add(frame);
        } else if (r == 0 && m == 1) {
          class1.add(frame);
        } else if (r == 1 && m == 0) {
          class2.add(frame);
        } else { // r == 1 && m == 1
          class3.add(frame);
        }
      }
    }

    // Select victim from lowest non-empty class
    if (!class0.isEmpty()) {
      return class0.getFirst(); // Class 0: lowest priority
    } else if (!class1.isEmpty()) {
      return class1.getFirst(); // Class 1
    } else if (!class2.isEmpty()) {
      return class2.getFirst(); // Class 2
    } else {
      return class3.getFirst(); // Class 3: highest priority
    }
  }

  /**
   * Resets all reference bits to 0 (simulating a clock interrupt)
   */
  private void resetReferenceBits(List<PageFrame> frames) {
    log.debug("Resetting all R bits (clock interrupt simulation)");
    for (PageFrame frame : frames) {
      if (!frame.isEmpty()) {
        frame.setReferenceBit(0);
      }
    }
  }
}