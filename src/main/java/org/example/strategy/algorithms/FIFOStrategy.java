package org.example.strategy.algorithms;

import java.util.List;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.PageReplacementStrategy;


public class FIFOStrategy implements PageReplacementStrategy {

  private int nextFrameToReplace = 0;

  @Override
  public ReplacementResult referencePage(Page page, List<PageFrame> frames, int currentTime,
      List<Integer> pageReferences, boolean hasFault) {
    System.out.println("Hello World from FIFO Strategy!");
    return null;
  }

  @Override
  public int getVictimFrameIndex(List<PageFrame> frames, Page page,List<Integer> pageReferences, int currentIndex) {
    return 0;
  }

  @Override
  public PageFrame getVictimPage(List<PageFrame> frames, Page page, List<Integer> pageReferences,
      int currentIndex) {
    return new PageFrame(-1);
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