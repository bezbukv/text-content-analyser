package core.mapreduce;

import gae_store_space.SentenceKind;

import com.google.common.collect.Multimap;

import org.checkthread.annotations.NotThreadSafe;

@NotThreadSafe
public class CountReducer {
  private final Multimap<String, SentenceKind> wordHistogram_;
  public CountReducer(Multimap<String, SentenceKind> wordHistogram) {
    wordHistogram_ = wordHistogram;
  }

  // @param value inv. index key - index sentence - или лучше хеш.
  public void reduce(String key, SentenceKind value) {
    wordHistogram_.put(key, value);
  }
}
