package org.example.strategy.algorithms;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.PageReplacementStrategy;

@Log4j2
public class LRUStrategy implements PageReplacementStrategy {

  @Override
  public ReplacementResult referencePage(Page page, List<PageFrame> frames, int currentTime,
      List<Integer> pageReferences, boolean hasFault) {
    log.info("starting LRU referencePage");

    if (!hasFault) {
      // Page hit: update last access time
      var frameIndex = findPageInFrames(frames, page);
      var pageFrame = frames.get(frameIndex);
      pageFrame.accessPage(page, currentTime); // update load time to current time

      log.info("page hit - updated last access time for page {}", page.getId());
      return new ReplacementResult(false, frameIndex, -1);
    }

    // page fault: find LRU page to evict or empty frame

    // case 1: check for empty frame
    int emptyFrameIndex = findEmptyFrame(frames);
    if (emptyFrameIndex != -1) {
      // load page into empty frame
      var emptyFrame = frames.get(emptyFrameIndex);
      emptyFrame.accessPage(page, currentTime);

      log.info("page fault - loaded page {} into empty frame {}", page.getId(), emptyFrameIndex);
      return new ReplacementResult(true, emptyFrameIndex, -1);
    }

    // case 2: no empty frame, apply the LRU logic, get the frame with the oldest load time
    var victimPage = getVictimPage(frames, page, pageReferences, currentTime);
    log.info("page fault - evicting page {} from frame {}", victimPage.getPageId(),
        victimPage.getIndex());

    var evictedPageId = victimPage.getPageId();
    var victimPageIndex = victimPage.getIndex();

    // replace the page
    victimPage.accessPage(page, currentTime);

    return new ReplacementResult(true, victimPageIndex, evictedPageId);
  }

  @Override
  public int getVictimFrameIndex(List<PageFrame> frames, Page page,
      List<Integer> pageReferences, int currentIndex) {
    PageFrame victim = getVictimPage(frames, page, pageReferences, currentIndex);
    return victim != null ? victim.getIndex() : -1;
  }

  @Override
  public PageFrame getVictimPage(List<PageFrame> frames, Page page, List<Integer> _pageReferences,
      int _currentIndex) {
    PageFrame lruFrame = null;
    int minTime = Integer.MAX_VALUE;

    for (PageFrame frame : frames) {
      if (frame.getLoadTime() < minTime) {
        minTime = frame.getLoadTime();
        lruFrame = frame;
      }
    }

    return lruFrame;
  }

  @Override
  public String getAlgorithmName() {
    return "LRU(Least Recently Used)";
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

  @Override
  public int findEmptyFrame(List<PageFrame> frames) {
    log.debug("checking if has empty frame");

    for (var index = 0; index < frames.size(); index++) {
      if (frames.get(index).isEmpty()) {
        log.debug("found empty frame at index {}", index);
        return index;
      }

    }
    return -1;
  }
}
