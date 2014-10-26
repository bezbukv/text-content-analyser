package core.nlp;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class PlainTextTokenizer {
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

  // About:
  //   split with some probability
  //
  // Troubles:
  /*
    разбитие прямой речи из субтитров. Недоразбивает:

    Yeah, I just wasn't looking where I was going. - But I'm great, actually. - Oh, thank goodness.
    Pattern -

    Sorry. ...Your  - Pattern -

  */
  public ImmutableList<String> getSentences(String text) {
    BreakIterator bi = BreakIterator.getSentenceInstance();
    bi.setText(text);
    List<String> result = new ArrayList<String>();
    int index = 0;
    while (bi.next() != BreakIterator.DONE) {
      String sentence = text.substring(index, bi.current());
      result.add(sentence);
      index = bi.current();
    }
    return ImmutableList.copyOf(result);
  }
}
