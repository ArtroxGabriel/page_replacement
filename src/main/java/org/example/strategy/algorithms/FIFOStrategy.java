package org.example.strategy.algorithms;

import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.PageReplacementStrategy;
import java.util.List;


public class FIFOStrategy implements PageReplacementStrategy {

  private int nextFrameToReplace = 0;

  @Override
  public ReplacementResult referencePage(int pageId, List<PageFrame> frames, int currentTime) {
    System.out.println("Hello World from FIFO Strategy!");
    return null;
  }

  @Override
  public int getVictimFrameIndex(List<PageFrame> frames, int pageId, List<Integer> referenceString, int currentIndex) {
    return 0;
  }

  @Override
  public PageFrame getVictimPage(List<PageFrame> frames, int pageId, List<Integer> referenceString, int currentIndex) {
    return new PageFrame();
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