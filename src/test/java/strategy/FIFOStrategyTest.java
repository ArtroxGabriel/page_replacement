package strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.algorithms.FIFOStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FIFOStrategyTest {

  private FIFOStrategy fifo;
  private static final List<Integer> REFERENCE_STRING =
      List.of(7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1);

  @BeforeEach
  void setUp() {
    fifo = new FIFOStrategy();
  }

  @Test
  void testWithThreeFrames() {
    List<PageFrame> frames = createFrames(3);
    int pageFaults = simulateReferences(frames);
    assertEquals(15, pageFaults, "FIFO with 3 frames should produce 15 page faults");
  }

  @Test
  void testWithFourFrames() {
    List<PageFrame> frames = createFrames(4);
    int pageFaults = simulateReferences(frames);
    assertEquals(10, pageFaults, "FIFO with 4 frames should produce 10 page faults");
  }

  private int simulateReferences(List<PageFrame> frames) {
    int pageFaults = 0;
    for (int i = 0; i < REFERENCE_STRING.size(); i++) {
      Page page = new Page(REFERENCE_STRING.get(i));
      boolean hasFault = findPageInFrames(frames, page) == -1;
      ReplacementResult result = fifo.referencePage(page, frames, i, REFERENCE_STRING, hasFault);
      if (result.pageFault()) {
        pageFaults++;
      }
    }
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