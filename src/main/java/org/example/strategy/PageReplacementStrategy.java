package org.example.strategy;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;

public interface PageReplacementStrategy {

  /**
   * Handle a reference to a page within the provided frames. Implementations should update any
   * internal bookkeeping (timestamps, counters, etc.) and return a {@link ReplacementResult}
   * describing whether a page fault occurred and which frame was loaded/evicted if applicable.
   *
   * @param page           the page being referenced
   * @param frames         the current list of page frames
   * @param currentTime    the current time or step (used by algorithms that record access time)
   * @param pageReferences the full list of page references
   * @param hasFault       indicates if a page fault has already been detected for this reference
   * @return a {@link ReplacementResult} containing the outcome of the reference
   */
  ReplacementResult referencePage(Page page, List<PageFrame> frames, int currentTime,
      List<Integer> pageReferences, boolean hasFault);


  /**
   * Return the actual {@link PageFrame} chosen as victim for eviction.
   *
   * @param frames         the current list of page frames
   * @param page           the page being referenced
   * @param pageReferences the full list of page references
   * @param currentIndex   the current index in the reference string
   * @return the victim {@link PageFrame}, or null if no victim is chosen
   */
  PageFrame getVictimPage(List<PageFrame> frames, Page page, List<Integer> pageReferences,
      int currentIndex);

  /**
   * Human-readable name of the algorithm (e.g. "LRU", "FIFO", "OPT").
   *
   * @return the algorithm's name
   */
  String getAlgorithmName();

  /**
   * Helper to create a {@link ReplacementResult} for loading a page into an empty frame.
   *
   * @param emptyFrameIndex the index of the empty frame
   * @param page            the page to load
   * @param time            the current time
   * @param frames          the list of page frames
   * @return a {@link ReplacementResult} for loading a page into an empty frame
   */
  default ReplacementResult emptyFrameResult(int emptyFrameIndex, Page page, int time,
      List<PageFrame> frames) {
    var emptyFrame = frames.get(emptyFrameIndex);
    emptyFrame.accessPage(page, time);
    emptyFrame.setReferenceBit(1);

    return new ReplacementResult(true, emptyFrameIndex, -1);
  }

  /**
   * Helper to create a {@link ReplacementResult} for loading a page into an empty frame.
   *
   * @param emptyFrameIndex the index of the empty frame
   * @param page            the page to load
   * @param frames          the list of page frames
   * @return a {@link ReplacementResult} for loading a page into an empty frame
   */
  default ReplacementResult emptyFrameResult(int emptyFrameIndex, Page page, List<PageFrame> frames) {
    var emptyFrame = frames.get(emptyFrameIndex);
    emptyFrame.accessPage(page);
    emptyFrame.setReferenceBit(1);

    return new ReplacementResult(true, emptyFrameIndex, -1);
  }


  /**
   * Determine the index of the victim frame to evict for the given page reference.
   *
   * @param frames         the current list of page frames
   * @param page           the page being referenced (may be used by some algorithms)
   * @param pageReferences the full list of page references
   * @param currentIndex   the current index in the reference string for which a victim is chosen
   * @return the index of the frame to evict, or -1 if no victim is chosen
   */
  default int getVictimFrameIndex(List<PageFrame> frames, Page page, List<Integer> pageReferences,
      int currentIndex) {
    PageFrame victim = getVictimPage(frames, page, pageReferences, currentIndex);
    return victim != null ? victim.getIndex() : -1;
  }

  /**
   * Reset any internal state used by the strategy (timestamps, counters, etc.). Default
   * implementation does nothing.
   */
  default void reset() {
  }

  /**
   * Helper to find a page id in the current frames.
   *
   * @param frames the current list of page frames
   * @param page   the page to search for
   * @return the index of the frame containing the page, or -1 if not present
   */
  default int findPageInFrames(List<PageFrame> frames, Page page) {
    for (var index = 0; index < frames.size(); index++) {
      if (frames.get(index).isEmpty()) {
        return index;
      }

    }
    return -1;
  }

  /**
   * Helper to find an empty frame (one that can be used without eviction).
   *
   * @param frames the current list of page frames
   * @return the index of an empty frame, or -1 if none are empty
   */
  default int findEmptyFrame(List<PageFrame> frames) {
    return -1;
  }
}