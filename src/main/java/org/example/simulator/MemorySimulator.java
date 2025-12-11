package org.example.simulator;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.PageReplacementFactory;
import org.example.strategy.PageReplacementStrategy;

@Getter
public class MemorySimulator {

  private final PageReplacementStrategy strategy;
  private final int framesCapacity;
  private final List<PageFrame> frames;
  private final boolean verbose;

  private int pageFaults = 0;
  private int pageHits = 0;
  private int replacements = 0;

  public MemorySimulator(String algorithm, int framesCapacity, boolean verbose) {
    this.strategy = PageReplacementFactory.createStrategy(algorithm);
    this.framesCapacity = framesCapacity;
    this.verbose = verbose;
    this.frames = new ArrayList<>(framesCapacity);
  }

  public SimulationResult simulate(List<Integer> pageReferences) {
    if (verbose) {
      System.out.println("=== Starting Simulation ===");
      System.out.println();
    }

    for (int i = 0; i < pageReferences.size(); i++) {
      int pageId = pageReferences.get(i);
      processPageReference(pageId, pageReferences, i);
    }

    return new SimulationResult(
        pageReferences.size(),
        pageFaults,
        pageHits,
        replacements,
        framesCapacity
    );
  }

  private void processPageReference(int pageId, List<Integer> pageReferences, int index) {
    if (verbose) {
      System.out.printf("Reference #%d: Page %d\n", index + 1, pageId);
    }

    // verificar se a página já está na memória (page hit)
    boolean pageInMemory = frames.stream()
        .anyMatch(frame -> frame.getPageId() == pageId);

    if (pageInMemory) {
      notifyStrategyPageAccess(pageId);
    } else {
      pageFaultHandler(pageId, pageReferences, index);
    }

    if (verbose) {
      printMemoryState();
      System.out.println();
    }
  }

  private void pageFaultHandler(int pageId, List<Integer> pageReferences, int index) {
    pageFaults++;
    if (verbose) {
      System.out.println("  → FAULT");
    }

    // se há espaço disponível, adicionar diretamente
    if (frames.size() < framesCapacity) {
      ReplacementResult result = strategy.referencePage(pageId, frames, framesCapacity);
      if (verbose) {
        System.out.printf("  → Loaded into frame %d\n", frames.size() - 1);
      }
    } else {
      // precisa substituir uma página
      PageFrame victimPage = strategy.getVictimPage(frames, pageId, pageReferences, index);

      if (victimPage != null) {
        replacements++;
        if (verbose) {
          System.out.printf("  → Replacing page %d\n", victimPage.getPageId());
        }
      }

      // executar a substituição através da estratégia
      strategy.referencePage(pageId, frames, framesCapacity);
    }
  }

  private void notifyStrategyPageAccess(int pageId) {
    pageHits++;
    if (verbose) {
      System.out.println("  → HIT");
    }
    // notificar a estratégia sobre o acesso (importante para LRU, LFU, etc)
    strategy.referencePage(pageId, frames, framesCapacity);
  }

  private void printMemoryState() {
    System.out.print("  Memory: [");
    for (int i = 0; i < frames.size(); i++) {
      if (i > 0) {
        System.out.print(", ");
      }
      System.out.print(frames.get(i).getPageId());
    }
    System.out.println("]");
  }
}
