package sand.third_party_tests.guava;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class SetGuavaTest {

  @Test
  public void testSplit() {
      Iterable<String> result =
        Splitter.on(',')
          .trimResults()
          .split("a,  l b,   c asfddf, d, d, d");
      Multiset<String> wordsMultiset = HashMultiset.create(result);
      System.out.print(wordsMultiset);
  }

  @Test
  public void testTrimSpacesString() {
    Iterable<String> result =
      Splitter
        .on(CharMatcher.WHITESPACE)
        .trimResults()
        .omitEmptyStrings()
        .split("   ");
    assert ImmutableList.copyOf(result).isEmpty();
  }

  @Test
  public void testStringIsDigit() {
    Iterable<String> result =
      Splitter
        .on(CharMatcher.WHITESPACE)
        .trimResults()
        .omitEmptyStrings()
        .split("88  ");

    assert ImmutableList.copyOf(result).size() == 1;

    Joiner joiner = Joiner.on("").skipNulls();
    String line = joiner.join(ImmutableList.copyOf(result));

    assert StringUtils.isNumeric(line);
  }
}
