package strategy;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.algorithms.NRUStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NRUStrategyTest {

  private NRUStrategy nru;
  private static final List<Integer> REFERENCE_STRING =
      List.of(7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1);

  @BeforeEach
  void setUp() {
    nru = new NRUStrategy();
  }

  @Test
  void testWithThreeFrames() {
    List<PageFrame> frames = createFrames(3);
    int pageFaults = simulateReferences(frames);
    assertTrue(pageFaults >= 10 && pageFaults <= 17,
        "NRU with 3 frames should produce between 9-15 page faults (varies due to randomness)");
  }

  @Test
  void testWithFourFrames() {
    List<PageFrame> frames = createFrames(4);
    int pageFaults = simulateReferences(frames);
    assertTrue(pageFaults >= 6 && pageFaults <= 12,
        "NRU with 4 frames should produce between 6-12 page faults (varies due to randomness)");
  }

  private int simulateReferences(List<PageFrame> frames) {
    int pageFaults = 0;

    for (int i = 0; i < REFERENCE_STRING.size(); i++) {
      Page page = new Page(REFERENCE_STRING.get(i));
      boolean hasFault = findPageInFrames(frames, page) == -1;
      ReplacementResult result = nru.referencePage(page, frames, i, REFERENCE_STRING, hasFault);

      if (result.pageFault()) {
        pageFaults++;
        System.out.println("Step " + i + " - Page Fault! Total: " + pageFaults +
            " | Page: " + page.getId() +
            " | Evicted: " + (result.evictedPageId() != -1 ? result.evictedPageId() : "none"));
      } else {
        System.out.println("Step " + i + " - Page Hit | Page: " + page.getId());
      }

      // Log frame states with R and M bits
      System.out.print("  Frames: [");
      for (int j = 0; j < frames.size(); j++) {
        PageFrame frame = frames.get(j);
        if (!frame.isEmpty()) {
          System.out.print("P" + frame.getPageId() +
              "(R=" + frame.getReferenceBit() +
              ",M=" + frame.getModifyBit() + ")");
        } else {
          System.out.print("empty");
        }
        if (j < frames.size() - 1) System.out.print(", ");
      }
      System.out.println("]");
    }

    System.out.println("\nFinal Page Faults: " + pageFaults);

    return pageFaults;
  }

  private List<PageFrame> createFrames(int count) {
    List<PageFrame> frames = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      frames.add(new PageFrame(i));
    }
    return frames;
  }

  private int findPageInFrames(List<PageFrame> frames, Page page) {
    for (int i = 0; i < frames.size(); i++) {
      if (frames.get(i).getPageId() == page.getId()) {
        return i;
      }
    }
    return -1;
  }
}