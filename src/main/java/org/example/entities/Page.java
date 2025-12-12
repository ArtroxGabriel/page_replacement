package org.example.entities;


import lombok.Getter;

@Getter
public class Page {

  private final int id;

  public Page(int id) {
    this.id = id;
  }
}
