package strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.algorithms.LFUStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LFUStrategyTest {

  private LFUStrategy lfu;
  private static final List<Integer> REFERENCE_STRING =
      List.of(7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1);

  @BeforeEach
  void setUp() {
    lfu = new LFUStrategy();
  }

  @Test
  void testWithThreeFrames() {
    List<PageFrame> frames = createFrames(3);
    int pageFaults = simulateReferences(frames);
    assertEquals(14, pageFaults, "LFU with 3 frames should produce 14 page faults");
  }

  @Test
  void testWithFourFrames() {
    List<PageFrame> frames = createFrames(4);
    int pageFaults = simulateReferences(frames);
    assertEquals(8, pageFaults, "LFU with 4 frames should produce 8 page faults");
  }

  private int simulateReferences(List<PageFrame> frames) {
    int pageFaults = 0;
    HashMap<Integer, Integer> pageFaultsSoFar = new HashMap<>();

    for (int i = 0; i < REFERENCE_STRING.size(); i++) {
      Page page = new Page(REFERENCE_STRING.get(i));
      boolean hasFault = lfu.findPageInFrames(frames, page) == -1;
      ReplacementResult result = lfu.referencePage(page, frames, i, REFERENCE_STRING, hasFault);

      if (result.pageFault()) {
        pageFaultsSoFar.remove(result.evictedPageId());
        pageFaultsSoFar.put(REFERENCE_STRING.get(i), pageFaultsSoFar.getOrDefault(REFERENCE_STRING.get(i), 0) + 1);
        pageFaults++;
        System.out.println("Step " + i + " - Page Fault! Total: " + pageFaults + " | Frequencies: " + pageFaultsSoFar);
      } else {
        pageFaultsSoFar.put(REFERENCE_STRING.get(i), pageFaultsSoFar.getOrDefault(REFERENCE_STRING.get(i), 0) + 1);
        System.out.println("Step " + i + " - Page Hit | Frequencies: " + pageFaultsSoFar);
      }
    }

    System.out.println("\nFinal Page Faults: " + pageFaults);
    System.out.println("Final Frequencies: " + pageFaultsSoFar);

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