package org.example.entities;

import lombok.Getter;

@Getter
public class PageFrame {

  private final int index;
  private Page page;
  private int loadTime;
  private boolean empty;

  public PageFrame(int index) {
    this.index = index;
    this.page = null;
    this.empty = true;
    this.loadTime = -1;
  }

  public int getPageId() {
    if (empty || page == null) {
      return -1;
    }

    return page.getId();
  }

  /**
   * Loads a page into the frame at the given time.
   *
   * @param page the page to be accessed
   * @param time the current time
   */
  public void accessPage(Page page, int time) {
    this.page = page;
    this.loadTime = time;
    this.empty = false;
  }
}