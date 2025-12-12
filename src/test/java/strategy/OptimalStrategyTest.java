package strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.algorithms.OptimalStrategy;
import org.junit.jupiter.api.Test;

public class OptimalStrategyTest {

  @Test
  void pageFaultLoadsIntoEmptyFrame() {
    var algo = new OptimalStrategy();
    Page incoming = new Page(10);

    PageFrame f0 = new PageFrame(0);
    PageFrame f1 = new PageFrame(1);
    List<PageFrame> frames = List.of(f0, f1);

    List<Integer> refs = List.of(incoming.getId());

    ReplacementResult res = algo.referencePage(incoming, frames, 7, refs, true);

    assertTrue(res.pageFault());
    assertEquals(0, res.frameIndex());
    assertEquals(-1, res.evictedPageId());

    PageFrame loaded = frames.get(res.frameIndex());
    assertEquals(incoming.getId(), loaded.getPageId());
    assertEquals(7, loaded.getLoadTime());
  }

  @Test
  void evictsPageNotUsedAgain() {
    var algo = new OptimalStrategy();
    Page p1 = new Page(1);
    Page p2 = new Page(2);
    Page incoming = new Page(3);

    PageFrame f0 = new PageFrame(0);
    f0.accessPage(p1, 2);
    PageFrame f1 = new PageFrame(1);
    f1.accessPage(p2, 5);

    List<PageFrame> frames = List.of(f0, f1);

    // references: current index 0 is incoming, later only p1 appears -> p2 is never used again
    List<Integer> refs = List.of(incoming.getId(), p1.getId());

    ReplacementResult res = algo.referencePage(incoming, frames, 20, refs, true);

    assertTrue(res.pageFault());
    assertEquals(1, res.frameIndex(),
        "Should evict frame containing page 2 because it's never used again");
    assertEquals(2, res.evictedPageId());

    PageFrame replaced = frames.get(res.frameIndex());
    assertEquals(3, replaced.getPageId());
    assertEquals(20, replaced.getLoadTime());
  }

  @Test
  void evictsPageWithFarthestNextUse() {
    var algo = new OptimalStrategy();
    Page p1 = new Page(1);
    Page p2 = new Page(2);
    Page incoming = new Page(3);

    PageFrame f0 = new PageFrame(0);
    f0.accessPage(p1, 2); // p1 used sooner
    PageFrame f1 = new PageFrame(1);
    f1.accessPage(p2, 5); // p2 used later

    List<PageFrame> frames = List.of(f0, f1);

    // references after current: p1 appears at index 1, p2 at index 2 -> should evict p2
    List<Integer> refs = List.of(incoming.getId(), p1.getId(), p2.getId());

    ReplacementResult res = algo.referencePage(incoming, frames, 30, refs, true);

    assertTrue(res.pageFault());
    assertEquals(1, res.frameIndex(),
        "Should evict frame 1 because its page is used farthest in the future");
    assertEquals(2, res.evictedPageId());

    PageFrame replaced = frames.get(res.frameIndex());
    assertEquals(3, replaced.getPageId());
    assertEquals(30, replaced.getLoadTime());
  }
}
