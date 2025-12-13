package org.example.strategy.algorithms;

import java.util.Comparator;
import java.util.List;
import org.example.entities.Page;
import org.example.entities.PageFrame;

public class MFUStrategy extends FrequencyBasedStrategy {

  @Override
  public String getAlgorithmName() {
    return "Most Frequently Used (MFU)";
  }

  @Override
  public PageFrame getVictimPage(List<PageFrame> frames, Page page,
      List<Integer> referenceString, int currentIndex) {
    return frames.stream()
        .max(Comparator.comparingInt(PageFrame::getFrequency))
        .orElse(frames.getFirst());
  }
}