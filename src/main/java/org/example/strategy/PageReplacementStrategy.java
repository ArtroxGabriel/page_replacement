package org.example.strategy;

import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import java.awt.*;
import java.util.List;

public interface PageReplacementStrategy {

  ReplacementResult referencePage(int pageId, List<PageFrame> frames, int currentTime);

  int getVictimFrameIndex(List<PageFrame> frames, int pageId, List<Integer> referenceString, int currentIndex);

  PageFrame getVictimPage(List<PageFrame> frames, int pageId, List<Integer> referenceString, int currentIndex);

  String getAlgorithmName();

  default void reset() {
  }

  default int findPageInFrames(List<PageFrame> frames, int pageId) {
    return -1;
  }

  default int findEmptyFrame(List<PageFrame> frames) {
    return -1;
  }
}
