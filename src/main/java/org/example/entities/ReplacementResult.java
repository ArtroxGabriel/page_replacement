package org.example.entities;

import lombok.Getter;

@Getter
public class ReplacementResult {
  private final boolean pageFault;
  private final int frameIndex;
  private final int evictedPageId;

  public ReplacementResult(boolean pageFault, int frameIndex, int evictedPageId) {
    this.pageFault = pageFault;
    this.frameIndex = frameIndex;
    this.evictedPageId = evictedPageId;
  }

  public boolean hadEviction() {
    return evictedPageId != -1;
  }
}