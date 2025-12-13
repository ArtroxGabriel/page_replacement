package org.example.strategy.algorithms;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.PageReplacementStrategy;

@Log4j2
public class OptimalStrategy implements PageReplacementStrategy {

  @Override
  public ReplacementResult referencePage(Page page, List<PageFrame> frames, int currentTime,
      List<Integer> pageReferences, boolean hasFault) {
    log.info("starting Optimal referencePage");

    if (!hasFault) {
      // Page hit: no action needed
      log.info("page hit - no action needed for page {}", page.getId());
      return new ReplacementResult(false, findPageInFrames(frames, page), -1);
    }

    // page fault: find Optimal page to evict or empty frame

    // case 1: check for empty frame
    int emptyFrameIndex = findEmptyFrame(frames);
    if (emptyFrameIndex != -1) {
      return emptyFrameResult(emptyFrameIndex, page, currentTime, frames);
    }

    // case 2: no empty frame, apply the Optimal logic
    // compute the current index within the provided reference list. If not found, assume 0
    int currentIndexInRefs = 0;
    if (pageReferences != null) {
      int idx = pageReferences.indexOf(page.getId());
      if (idx != -1) {
        currentIndexInRefs = idx;
      }
    }

    var victimPage = getVictimPage(frames, page, pageReferences, currentIndexInRefs);
    log.info("page fault - evicting page {} from frame {}", victimPage.getPageId(),
        victimPage.getIndex());
    var evictedPageId = victimPage.getPageId();
    var victimPageIndex = victimPage.getIndex();

    // replace the page
    victimPage.accessPage(page, currentTime);

    return new ReplacementResult(true, victimPageIndex, evictedPageId);
  }

  @Override
  public PageFrame getVictimPage(List<PageFrame> frames, Page page, List<Integer> pageReferences,
      int currentIndex) {
    log.info("starting Optimal getVictimPage");

    PageFrame victimFrame = null;
    int farthestUse = -1;
    for (PageFrame frame : frames) {
      int nextUse = Integer.MAX_VALUE;
      for (int lookahead = currentIndex + 1; lookahead < pageReferences.size(); lookahead++) {
        if (pageReferences.get(lookahead) == frame.getPageId()) {
          nextUse = lookahead;
          break;
        }
      }

      if (nextUse > farthestUse) {
        farthestUse = nextUse;
        victimFrame = frame;
      }
    }

    if (victimFrame == null) {
      throw new IllegalStateException("No victim frame found, this should not happen.");
    }

    log.info("Optimal victim page selected: {}", victimFrame.getPageId());
    return victimFrame;
  }

  @Override
  public String getAlgorithmName() {
    return "Optimal";
  }
}
