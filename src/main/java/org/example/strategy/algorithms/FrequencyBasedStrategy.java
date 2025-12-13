package org.example.strategy.algorithms;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.PageReplacementStrategy;

@Log4j2
public abstract class FrequencyBasedStrategy implements PageReplacementStrategy {

  @Override
  public ReplacementResult referencePage(Page page, List<PageFrame> frames, int currentTime,
      List<Integer> pageReferences, boolean hasFault) {
    log.info("starting {} referencePage", getAlgorithmName());

    if (!hasFault) {
      // Page hit: increment frequency
      var frameIndex = findPageInFrames(frames, page);
      var pageFrame = frames.get(frameIndex);
      pageFrame.incrementFrequency();

      log.info("page hit - incremented frequency for page {} (frequency: {})",
          page.getId(), pageFrame.getFrequency());
      return new ReplacementResult(false, frameIndex, -1);
    }

    // Page fault: find victim or empty frame

    // Case 1: check for empty frame
    int emptyFrameIndex = findEmptyFrame(frames);
    if (emptyFrameIndex != -1) {
      log.info("page fault - loading page {} into empty frame {}", page.getId(), emptyFrameIndex);
      var frame = frames.get(emptyFrameIndex);
      frame.accessPage(page, currentTime);
      frame.resetFrequency();
      frame.incrementFrequency(); // First access
      return new ReplacementResult(false, emptyFrameIndex, -1);
    }

    // Case 2: no empty frame, apply frequency-based logic
    var victimPage = getVictimPage(frames, page, pageReferences, -1);
    log.info("page fault - evicting page {} (frequency: {}) from frame {}",
        victimPage.getPageId(), victimPage.getFrequency(), victimPage.getIndex());

    var evictedPageId = victimPage.getPageId();
    var victimPageIndex = victimPage.getIndex();

    // Replace the page
    victimPage.accessPage(page, currentTime);
    victimPage.resetFrequency();
    victimPage.incrementFrequency(); // First access

    return new ReplacementResult(true, victimPageIndex, evictedPageId);
  }

  @Override
  public abstract PageFrame getVictimPage(List<PageFrame> frames, Page page,
      List<Integer> referenceString, int currentIndex);

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