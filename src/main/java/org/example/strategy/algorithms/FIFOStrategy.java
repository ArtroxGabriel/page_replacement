package org.example.strategy.algorithms;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.PageReplacementStrategy;

@Log4j2
public class FIFOStrategy implements PageReplacementStrategy {

  private int nextFrameToReplace = 0;

  @Override
  public ReplacementResult referencePage(Page page, List<PageFrame> frames, int currentTime,
      List<Integer> pageReferences, boolean hasFault) {

    // Check if page is already in frames (page hit)
    if (!hasFault) {
      log.info("page hit - no action needed for page {}", page.getId());
      return new ReplacementResult(false, findPageInFrames(frames, page), -1);
    }

    // Page fault occurred

    // case 1: check for empty frame
    int emptyFrameIndex = findEmptyFrame(frames);
    if (emptyFrameIndex != -1) {
      return emptyFrameResult(emptyFrameIndex, page, currentTime, frames);
    }

    // case 2: no empty frames - need to evict using FIFO
    int victimIndex = nextFrameToReplace;
    int evictedPageId = frames.get(victimIndex).getPageId();

    // Replace the victim page
    frames.get(victimIndex).accessPage(page, currentTime);

    // Update pointer for next replacement (circular)
    nextFrameToReplace = (nextFrameToReplace + 1) % frames.size();

    return new ReplacementResult(true, victimIndex, evictedPageId);
  }

  @Override
  public int getVictimFrameIndex(List<PageFrame> frames, Page page, List<Integer> pageReferences, int currentIndex) {
    return nextFrameToReplace;
  }

  @Override
  public PageFrame getVictimPage(List<PageFrame> frames, Page page, List<Integer> pageReferences,
      int currentIndex) {
    return frames.get(nextFrameToReplace);
  }

  @Override
  public String getAlgorithmName() {
    return "FIFO";
  }

  @Override
  public void reset() {
    nextFrameToReplace = 0;
  }
}