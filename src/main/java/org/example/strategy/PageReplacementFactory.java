package org.example.strategy;

import org.example.strategy.algorithms.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PageReplacementFactory {

  private static final Map<String, Supplier<PageReplacementStrategy>> strategies = new HashMap<>();

  static {
    register("FIFO", FIFOStrategy::new);
     register("LRU", LRUStrategy::new);
     register("OPTIMAL", OptimalStrategy::new);
     register("OPT", OptimalStrategy::new); // Alias
     register("SECOND_CHANCE", SecondChanceStrategy::new);
     register("SC", SecondChanceStrategy::new); // Alias
     register("CLOCK", SecondChanceStrategy::new);
     register("NRU", NRUStrategy::new);
     register("LFU", LFUStrategy::new);
     register("MFU", MFUStrategy::new);
  }

  public static void register(String name, Supplier<PageReplacementStrategy> strategySupplier) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Algorithm name cannot be null or empty");
    }
    if (strategySupplier == null) {
      throw new IllegalArgumentException("Strategy supplier cannot be null");
    }
    strategies.put(name.toUpperCase().trim(), strategySupplier);
  }

  public static PageReplacementStrategy createStrategy(String algorithmName) {
    if (algorithmName == null || algorithmName.isBlank()) {
      throw new IllegalArgumentException("Algorithm name cannot be null or empty");
    }

    String normalizedName = algorithmName.toUpperCase().trim();
    Supplier<PageReplacementStrategy> strategySupplier = strategies.get(normalizedName);

    if (strategySupplier == null) {
      throw new IllegalArgumentException(String.format("Unknown algorithm: '%s'", algorithmName));
    }

    return strategySupplier.get();
  }
}