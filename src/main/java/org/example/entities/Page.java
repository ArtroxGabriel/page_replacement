package org.example.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {

  private final int id;
  private boolean modified = false;

  public Page(int id) {
    this.id = id;
  }
}
