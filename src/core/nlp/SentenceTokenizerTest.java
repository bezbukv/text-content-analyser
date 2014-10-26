package core.nlp;

import com.google.common.base.Splitter;
import common.Tools;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 10.05.13
 * Time: 20:28
 * To change this template use File | Settings | File Templates.
 */
public class SentenceTokenizerTest {
  @Test
  public void testDevelop() {
    String sent = "Прочие, связанные с аудиторской деятельностью услуги представляют собой:  "+
      "- постановка, восстановление и ведение бухгалтерского учета;  "+
      "- составление отчетности  "+
      "- консультирование (в том числе налоговое, управленческое и правовое   консультирование);  "+
      "- анализ финансово-хозяйственной деятельности;  "+
      "9) автоматизация бухгалтерского учета, внедрение информационных   технологий;  "+
      "- разработка и анализ инвестиционных проектов;  - оценка стоимости имущества;     4      "+
      "- проведение научно-исследовательских работ;  "+
      "- и другие услуги, связанные с аудиторской деятельностью.  ";
    Iterable<String> result = Splitter.onPattern("(-|[0-9])?[:;]")
      .trimResults()
      .omitEmptyStrings()
      .split(sent);
    for (String item: result) {
      Tools.print(item);
    }

  }
}
