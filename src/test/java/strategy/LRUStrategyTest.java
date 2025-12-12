package strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.example.entities.Page;
import org.example.entities.PageFrame;
import org.example.entities.ReplacementResult;
import org.example.strategy.algorithms.LRUStrategy;
import org.junit.jupiter.api.Test;

public class LRUStrategyTest {

  @Test
  void pageHitUpdatesLoadTime() {
    LRUStrategy lru = new LRUStrategy();
    Page page = new Page(1);

    PageFrame frame0 = new PageFrame(0);
    frame0.accessPage(page, 5);
    PageFrame frame1 = new PageFrame(1);

    List<PageFrame> frames = Arrays.asList(frame0, frame1);

    ReplacementResult res = lru.referencePage(page, frames, 10, false);

    assertFalse(res.pageFault(), "Should be a page hit (no fault)");
    assertEquals(0, res.frameIndex(), "Hit should be in frame 0");
    assertEquals(-1, res.evictedPageId(), "No eviction on hit");

    assertEquals(10, frame0.getLoadTime(), "Load time should be updated to current time");
    assertEquals(1, frame0.getPageId(), "Page id should remain the same after hit");
  }

  @Test
  void pageFaultLoadsIntoEmptyFrame() {
    LRUStrategy lru = new LRUStrategy();
    Page page = new Page(2);

    PageFrame frame0 = new PageFrame(0);
    PageFrame frame1 = new PageFrame(1);

    List<PageFrame> frames = Arrays.asList(frame0, frame1);

    ReplacementResult res = lru.referencePage(page, frames, 7, true);

    assertTrue(res.pageFault(), "Should be a page fault when hasFault=true");
    assertEquals(0, res.frameIndex(), "First empty frame (0) should be used");
    assertEquals(-1, res.evictedPageId(), "No eviction when loading into empty frame");

    PageFrame loaded = frames.get(res.frameIndex());
    assertEquals(2, loaded.getPageId(), "Loaded page id should match");
    assertEquals(7, loaded.getLoadTime(), "Loaded frame should have the provided time");
  }

  @Test
  void pageFaultEvictsLRU() {
    LRUStrategy lru = new LRUStrategy();
    Page p1 = new Page(1);
    Page p2 = new Page(2);
    Page incoming = new Page(3);

    PageFrame frame0 = new PageFrame(0);
    frame0.accessPage(p1, 2); // older
    PageFrame frame1 = new PageFrame(1);
    frame1.accessPage(p2, 5); // newer

    List<PageFrame> frames = Arrays.asList(frame0, frame1);

    ReplacementResult res = lru.referencePage(incoming, frames, 20, true);

    assertTrue(res.pageFault(), "Should be a page fault");
    assertEquals(0, res.frameIndex(), "Frame 0 (oldest) should be evicted");
    assertEquals(1, res.evictedPageId(), "Evicted page id should be the id of the older page (1)");

    PageFrame replaced = frames.get(0);
    assertEquals(3, replaced.getPageId(), "Incoming page should now occupy the evicted frame");
    assertEquals(20, replaced.getLoadTime(), "Load time should be updated to current time after replacement");
  }

  @Test
  void findPageInFramesNotFound() {
    LRUStrategy lru = new LRUStrategy();
    Page p1 = new Page(1);
    PageFrame frame0 = new PageFrame(0);
    frame0.accessPage(p1, 4);

    Page missing = new Page(99);
    List<PageFrame> frames = List.of(frame0);

    int idx = lru.findPageInFrames(frames, missing);
    assertEquals(-1, idx, "Page not present should return -1");
  }
}
