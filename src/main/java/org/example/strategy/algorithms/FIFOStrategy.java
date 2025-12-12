package org.example.strategy.algorithms;

import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.PageReplacementStrategy;
import java.util.List;


public class FIFOStrategy implements PageReplacementStrategy {

  private int nextFrameToReplace = 0;

  @Override
  public ReplacementResult referencePage(Page page, List<PageFrame> frames, int currentTime, boolean hasFault) {
    System.out.println("Hello World from FIFO Strategy!");
    return null;
  }

  @Override
  public int getVictimFrameIndex(List<PageFrame> frames, Page page, int currentIndex) {
    return 0;
  }

  @Override
  public PageFrame getVictimPage(List<PageFrame> frames, Page page, int currentIndex) {
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