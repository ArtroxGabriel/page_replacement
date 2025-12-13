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
  public ReplacementResult referencePage(Page page, List<PageFrame> frames, int currentTime,
      List<Integer> pageReferences, boolean hasFault) {
    log.info("starting {} referencePage", getAlgorithmName());

    if (!hasFault) {
      // Page hit: set reference bit
      var frameIndex = findPageInFrames(frames, page);
      var pageFrame = frames.get(frameIndex);
      pageFrame.setReferenceBit(1);

      log.info("page hit - set reference bit for page {} in frame {}", page.getId(), frameIndex);
      return new ReplacementResult(false, frameIndex, -1);
    }

    // Page fault: find victim or empty frame

    // Case 1: check for empty frame
    int emptyFrameIndex = findEmptyFrame(frames);
    if (emptyFrameIndex != -1) {
      log.info("page fault - loading page {} into empty frame {}", page.getId(), emptyFrameIndex);
      var frame = frames.get(emptyFrameIndex);
      frame.accessPage(page);
      frame.setReferenceBit(1);
      return emptyFrameResult(emptyFrameIndex, page, frames);
    }

    // Case 2: no empty frame, apply clock algorithm
    log.info("page fault - searching for victim using clock algorithm (pointer at {})", clockPointer);
    var victimPage = getVictimPage(frames, page, pageReferences, -1);
    log.info("page fault - evicting page {} from frame {}", victimPage.getPageId(), victimPage.getIndex());

    var evictedPageId = victimPage.getPageId();
    var victimPageIndex = victimPage.getIndex();

    victimPage.accessPage(page);
    victimPage.setReferenceBit(1);

    return new ReplacementResult(true, victimPageIndex, evictedPageId);
  }

  @Override
  public PageFrame getVictimPage(List<PageFrame> frames, Page page, List<Integer> referenceString, int currentIndex) {

    // Rotate until finding a page with R=0
    while (true) {
      PageFrame current = frames.get(clockPointer);
      log.debug("clock at position {}: page {} has R={}", clockPointer, current.getPageId(), current.getReferenceBit());

      if (current.getReferenceBit() == 0) {
        // R=0: found victim, advance pointer to next position
        int victimIndex = clockPointer;
        clockPointer = (clockPointer + 1) % frames.size();
        log.debug("victim found at position {}, advancing clock pointer to {}", victimIndex, clockPointer);
        return frames.get(victimIndex);
      } else {
        // R=1: give second chance, clear bit and advance
        log.debug("giving second chance to page {} at position {}", current.getPageId(), clockPointer);
        current.setReferenceBit(0);
        clockPointer = (clockPointer + 1) % frames.size();
      }
    }
  }

  @Override
  public int findPageInFrames(List<PageFrame> frames, Page page) {
    log.debug("checking for page {} in frames", page.getId());

    for (int i = 0; i < frames.size(); i++) {
      if (frames.get(i).getPageId() == page.getId()) {
        log.debug("page {} found in frame {}", page.getId(), i);
        return i;
      }
    }
    return -1;
  }
}