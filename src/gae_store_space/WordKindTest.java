package gae_store_space;

import org.junit.Test;

/**
 * Created by zaqwes on 5/12/14.
 */
public class WordKindTest {
  @Test
  public void testCompare() throws Exception {
    WordKind o1 = WordKind.create("hello", null, 1);
    WordKind o2 = WordKind.create("dfasdf", null, 1);

    assert 0 == WordKind.createFrequencyComparator().compare(o1, o2);
  }
}
