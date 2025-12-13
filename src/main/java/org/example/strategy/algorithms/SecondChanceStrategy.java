package org.example.strategy.algorithms;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.PageReplacementStrategy;

@Log4j2
public class SecondChanceStrategy implements PageReplacementStrategy {

  private int clockPointer = 0;

  @Override
  public String getAlgorithmName() {
    return "Second Chance (Clock)";
  }

  @Override
  public ReplacementResult referencePage(Page page, List<PageFrame> frames, int framesCapacity,
      List<Integer> referenceString, boolean isFault) {

    // If it's a hit (not a fault), just set the reference bit
    if (!isFault) {
      for (PageFrame frame : frames) {
        if (frame.getPageId() == page.getId()) {
          frame.setReferenceBit(1);
          return new ReplacementResult(false, frame.getIndex(), -1);
        }
      }
    }

    // If there's an empty frame, load there
    for (PageFrame frame : frames) {
      if (frame.isEmpty()) {
        frame.accessPage(page, 0);
        frame.setReferenceBit(1);
        return new ReplacementResult(false, frame.getIndex(), -1);
      }
    }

    // No empty frame, need to replace
    PageFrame victim = getVictimPage(frames, page, referenceString, 0);
    int victimId = victim.getPageId();

    victim.accessPage(page, 0);
    victim.setReferenceBit(1);

    return new ReplacementResult(true, victim.getIndex(), victimId);
  }

  @Override
  public PageFrame getVictimPage(List<PageFrame> frames, Page page, List<Integer> referenceString,
      int currentIndex) {

    // rotate until finding a page with R=0
    while (true) {
      PageFrame current = frames.get(clockPointer);

      if (current.getReferenceBit() == 0) {
        // R=0: found victim, advance pointer to next position
        int victimIndex = clockPointer;
        clockPointer = (clockPointer + 1) % frames.size();
        return frames.get(victimIndex);
      } else {
        // R=1: give second chance, clear bit and advance
        current.setReferenceBit(0);
        clockPointer = (clockPointer + 1) % frames.size();
      }
    }
  }
}