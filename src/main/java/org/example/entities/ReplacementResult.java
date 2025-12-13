package org.example.entities;

/**
 * @param pageFault     indicates if a page fault occurred
 * @param frameIndex    the index of the frame that was loaded or accessed
 * @param evictedPageId the ID of the evicted page, or -1 if no eviction occurred
 */
public record ReplacementResult(boolean pageFault, int frameIndex, int evictedPageId) {

  public boolean hadEviction() {
    return evictedPageId != -1;
  }
}