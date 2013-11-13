package nlp;

import common.ImmutableAppUtils;
import com.google.common.base.Splitter;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 10.05.13
 * Time: 15:04
 * To change this template use File | Settings | File Templates.
 */
final public class BaseTokenizer {
  public static Iterable<String> splitDocFormat(String line) {
    return Splitter.onPattern("(-|[0-9])?[:;]")
      .trimResults()
      .omitEmptyStrings()
      .split(line);
  }

  // Встроенный в SDK дробитель на предложения - похоже безсловарный, поэтому грубоватый
  public static StringBuilder splitToSentences(StringBuilder buffer, String lang) {
    // разбиваем не единицы и пишем
    // TODO(zaqwes) TOTH: maybi slow!
    // Убираем переносы строк - могут быть ошибки! Некоторые слова с дефисом могут быть перенес. по дуфису
    String dataForSplitting = buffer.toString().replace("-\n", "\n").replace('\n', ' ');

    BreakIterator bi = BreakIterator.getSentenceInstance();
    bi.setText(dataForSplitting);
    int index = 0;
    StringBuilder summaryContent = new StringBuilder();
    while (bi.next() != BreakIterator.DONE) {
      String sentence = dataForSplitting.substring(index, bi.current());
      // Разбираем предложение еще не части
      // Для документов характерно наличие 1), 2)...
      Iterable<String> additionSplit = splitDocFormat(sentence);
      for (String item : additionSplit) {
        String oneRecord = lang+' '+item+'\n';
        summaryContent.append(oneRecord);
      }

      // добавляем
      index = bi.current();
    }
    return summaryContent;
  }

  // Split to words
  public static List<String> extractWords(String target/*, BreakIterator wordIterator*/) {
    BreakIterator wordIterator =
      BreakIterator.getWordInstance();
    wordIterator.setText(target);
    int start = wordIterator.first();
    int end = wordIterator.next();

    List<String> resultWordList = new ArrayList<String>();
    while (end != BreakIterator.DONE) {
      String word = target.substring(start,end);
      if (Character.isLetterOrDigit(word.charAt(0))) {
        resultWordList.add(word);
      }
      start = end;
      end = wordIterator.next();
    }
    return resultWordList;
  }
}
