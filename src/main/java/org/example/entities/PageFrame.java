package org.example.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageFrame {

  private final int index;
  private Page page;
  private int loadTime;
  private boolean empty;
  private int referenceBit;
  private int modifyBit;

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

  /**
   * Loads a page into the frame.
   *
   * @param page the page to be accessed
   */
  public void accessPage(Page page) {
    this.page = page;
    this.empty = false;
  }
}