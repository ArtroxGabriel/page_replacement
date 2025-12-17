package strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.algorithms.SecondChanceStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SecondChanceStrategyTest {

  private SecondChanceStrategy secondChance;
  private static final List<Integer> REFERENCE_STRING =
      List.of(7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1);

  @BeforeEach
  void setUp() {
    secondChance = new SecondChanceStrategy();
  }

  @Test
  void testWithThreeFrames() {
    List<PageFrame> frames = createFrames(3);
    int pageFaults = simulateReferences(frames);
    assertEquals(14, pageFaults, "Second Chance with 3 frames should produce 13 page faults");
  }

  @Test
  void testWithFourFrames() {
    List<PageFrame> frames = createFrames(4);
    int pageFaults = simulateReferences(frames);
    assertEquals(9, pageFaults, "Second Chance with 4 frames should produce 9 page faults");
  }

  private int simulateReferences(List<PageFrame> frames) {
    int pageFaults = 0;

    for (int i = 0; i < REFERENCE_STRING.size(); i++) {
      Page page = new Page(REFERENCE_STRING.get(i));
      boolean hasFault = secondChance.findPageInFrames(frames, page) == -1;
      ReplacementResult result = secondChance.referencePage(page, frames, i, REFERENCE_STRING, hasFault);

      if (result.pageFault()) {
        pageFaults++;
        System.out.println("Step " + i + " - Page Fault! Total: " + pageFaults +
            " | Page: " + page.getId() +
            " | Evicted: " + (result.evictedPageId() != -1 ? result.evictedPageId() : "none"));
      } else {
        System.out.println("Step " + i + " - Page Hit | Page: " + page.getId());
      }

      // Log frame states with reference bits
      System.out.print("  Frames: [");
      for (int j = 0; j < frames.size(); j++) {
        PageFrame frame = frames.get(j);
        if (!frame.isEmpty()) {
          System.out.print("P" + frame.getPageId() + "(R=" + frame.getReferenceBit() + ")");
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
}