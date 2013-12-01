/**
 * Trobles:
 *    - Перенос строки (можно по \r\n определить и схлопнуть слово, но могут быть консяки со словами с дефисом)
 *    - Заголовки не имеют окончания (на выходе Tika, например)
 *
 * */
import java.text.BreakIterator;
import java.util.Locale;

public class TestSentSplitter {

  public static void main(String[] args) {
    // write your code here

    
    String text = "\n" +
      "22.2 Сроки и порядок предоставления налоговой декларации \n" +
      "В соответствии с п. 5 статьи 204 НК РФ налогоплательщики обязаны представлять в \n" +
      "налоговые органы по месту своего нахождения, а также по месту нахождения каждого сво-" +
      "его \n" +
      "обособленного подразделения, в которых они состоят на учете, если иное не предусмотрено \n" +
      "настоящим пунктом, налоговую декларацию за налоговый период в части осуществляемых ими \n" +
      "операций, признаваемых объектом налогообложения в соответствии с главой 22 НК РФ, в срок \n" +
      "не позднее 25-го числа месяца, следующего за истекшим налоговым периодом, если иное не \n" +
      "предусмотрено настоящим пунктом, а налогоплательщики, имеющие свидетельство о \n" +
      "регистрации лица, совершающего операции с прямогонным бензином, и (или) свидетельство о \n" +
      "регистрации организации, совершающей операции с денатурированным этиловым спиртом, - не \n" +
      "позднее 25-го числа третьего месяца, следующего за отчетным. ";
    
    BreakIterator bi = BreakIterator.getSentenceInstance();
    bi.setText(text);
    int index = 0;
    while (bi.next() != BreakIterator.DONE) {
      String sentence = text.substring(index, bi.current());
      System.out.println("Sentence: " + sentence);
      index = bi.current();
    }


    Locale locale = Locale.UK;
    BreakIterator breakIterator =
      BreakIterator.getLineInstance(locale);

    breakIterator.setText(
      "Mary had a little Android device.\n " +
        "It had small batteries too.");

    //int boundaryIndex = breakIterator.first();
    /*while(boundaryIndex != BreakIterator.DONE) {
      System.out.println(boundaryIndex) ;
      boundaryIndex = breakIterator.next();

      //String sentence = text.substring(index, bi.current());
      // System.out.println("Sentence: " + sentence);
      //index = bi.current();
    }*/
  }
}

