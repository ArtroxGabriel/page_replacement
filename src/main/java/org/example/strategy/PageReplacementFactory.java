package org.example.strategy;

import org.example.strategy.algorithms.*;

public class PageReplacementFactory {

  public static PageReplacementStrategy createStrategy(String algorithmName) {
    if (algorithmName == null || algorithmName.isBlank()) {
      throw new IllegalArgumentException("Algorithm name cannot be null or empty");
    }

    return switch (algorithmName.toUpperCase().trim()) {
      case "FIFO" -> new FIFOStrategy();
//      case "LRU" -> new LRUStrategy();
//      case "OPTIMAL", "OPT" -> new OptimalStrategy();
//      case "SECOND_CHANCE", "SC" -> new SecondChanceStrategy();
//      case "CLOCK" -> new ClockStrategy();
//      case "NRU" -> new NRUStrategy();
//      case "LFU" -> new LFUStrategy();
//      case "MFU" -> new MFUStrategy();
      default -> throw new IllegalArgumentException(
          String.format(
              "Unknown algorithm: '%s'. Supported algorithms: FIFO, LRU, OPTIMAL, SECOND_CHANCE, CLOCK, NRU, LFU, MFU",
              algorithmName
          )
      );
    };
  }
}