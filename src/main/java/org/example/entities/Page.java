package org.example.entities;


import lombok.Getter;

@Getter
public class Page {

  private static int nextID = 0;
  private final int id;

  public Page() {
    this.id = nextID++;
  }
}
