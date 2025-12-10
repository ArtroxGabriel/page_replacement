package org.example.entities;

import lombok.Getter;

@Getter
public class PageFrame {
  private int pageId;
  private int loadTime;
  private boolean empty;

  public PageFrame() {
    this.empty = true;
    this.pageId = -1;
    this.loadTime = -1;
  }

  public void loadPage(int pageId, int time) {
    this.pageId = pageId;
    this.loadTime = time;
    this.empty = false;
  }
}