package org.example.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.PageReplacementFactory;
import org.example.strategy.PageReplacementStrategy;

@Getter
public class MemorySimulator {

  private final PageReplacementStrategy strategy;
  private final int framesCapacity;
  private final HashMap<Integer, Page> pages;
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
    for (int i = 0; i < framesCapacity; i++) {
      frames.add(new PageFrame(i));
    }

    this.pages = new HashMap<>();
  }

  public SimulationResult simulate(List<Integer> pageReferences) {
    if (verbose) {
      System.out.println("=== Starting Simulation ===");
      System.out.println();
    }

    pageReferences.forEach(reference -> pages.putIfAbsent(reference, new Page(reference)));

    for (int currentTime = 0; currentTime < pageReferences.size(); currentTime++) {
      int pageId = pageReferences.get(currentTime);
      var page = pages.get(pageId);
      processPageReference(page, currentTime, pageReferences);
    }

    return new SimulationResult(
        pageReferences.size(),
        pageFaults,
        pageHits,
        replacements,
        framesCapacity
    );
  }

  private void processPageReference(Page page, int currentTime, List<Integer> pageReferences) {
    if (verbose) {
      System.out.printf("Reference #%d: Page %d\n", currentTime + 1, page.getId());
    }

    // verificar se a página já está na memória (page hit)
    boolean pageInMemory = frames.stream()
        .anyMatch(frame -> frame.getPageId() == page.getId());

    if (pageInMemory) {
      notifyStrategyPageAccess(page, pageReferences);
    } else {
      pageFaultHandler(page, currentTime, pageReferences);
    }

    if (verbose) {
      printMemoryState();
      System.out.println();
    }
  }

  private void pageFaultHandler(Page page, int currentTime, List<Integer> pageReferences) {
    pageFaults++;
    if (verbose) {
      System.out.println("  → FAULT");
    }

    // se houver espaço livre, carregar a página sem substituição
    if (this.frames.stream().anyMatch(PageFrame::isEmpty)) {
      ReplacementResult result = strategy.referencePage(page, frames, framesCapacity,
          pageReferences, true);
      if (verbose) {
        System.out.printf("  → Loaded into frame %d\n", frames.size() - 1);
      }
      return;
    }

    // precisa substituir uma página
    PageFrame victimPage = strategy.getVictimPage(frames, page, pageReferences, currentTime);

    if (victimPage != null) {
      replacements++;
      if (verbose) {
        System.out.printf("  → Replacing page %d\n", victimPage.getPageId());
      }
    }

    // executar a substituição através da estratégia
    strategy.referencePage(page, frames, framesCapacity, pageReferences, true);

  }

  private void notifyStrategyPageAccess(Page page, List<Integer> pageReferences) {
    pageHits++;
    if (verbose) {
      System.out.println("  → HIT");
    }
    // notificar a estratégia sobre o acesso (importante para LRU, LFU, etc)
    strategy.referencePage(page, frames, framesCapacity, pageReferences, false);
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
