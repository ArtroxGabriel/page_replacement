package org.example.entities;

import lombok.Getter;

@Getter
public class Memory {

  private final Page[] frames;

  public Memory(int numberOfFrames) {
    if (numberOfFrames <= 0) {
      throw new IllegalArgumentException("the number of frames must be grater than 0");
    }

    frames = new Page[numberOfFrames];
  }
}
