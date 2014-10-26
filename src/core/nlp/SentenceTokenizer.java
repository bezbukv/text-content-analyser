package core.nlp;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class SentenceTokenizer {
  public List<String> getWords(String contentItem) {
    BreakIterator iterator = BreakIterator.getWordInstance();
    iterator.setText(contentItem);
    int start = iterator.first();
    int end = iterator.next();

    List<String> words = new ArrayList<String>();
    while (end != BreakIterator.DONE) {
      String word = contentItem.substring(start,end);
      if (Character.isLetterOrDigit(word.charAt(0))) {
        words.add(word);
      }
      start = end;
      end = iterator.next();
    }
    return words;
  }
}
