package core.mapreduce;

import core.nlp.SentenceTokenizer;

import org.checkthread.annotations.NotThreadSafe;

import gae_store_space.SentenceKind;

import java.util.List;

/**
 https://hadoop.apache.org/docs/r1.2.1/mapred_tutorial.html

 Fake MapReduce - try make similar
 */
@NotThreadSafe
public class CounterMapper {
  private final CountReducer reducer_;
  public CounterMapper(CountReducer reducer) {
    reducer_ = reducer;
  }

  private void emit(String key, SentenceKind value) {
    reducer_.reduce(key, value);
  }

  public void map(List<SentenceKind> contentItemKinds) {
    SentenceTokenizer tokenizer = new SentenceTokenizer();
    for (SentenceKind item : contentItemKinds) {
      List<String> words = tokenizer.getWords(item.getSentence());
      for (String word: words)
      	// FIXME: нужна компрессия. Пока что все перевел в нижний регистр.
      	
        emit(word.toLowerCase(), item);
    }
  }
}
