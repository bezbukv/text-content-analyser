package frozen.old;

import core.nlp.SyllableCounterImpl;
import frozen.jobs_processors.JobsFormer;
import core.nlp.SentenceTokenizer;
import com.google.common.io.Closer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 11.05.13
 * Time: 20:31
 * To change this template use File | Settings | File Templates.
 */
final public class SentencesMapper {
  public final static int IDX_NODE_NAME = 0;
  public final static int IDX_SENTENCES_LENS = 1;
  public final static int IDX_COUNT_SYLLABLES = 2;
  public final static int IDX_LANG = 3;
  /*
  * @param job = [node_name, url_with_text_content_splitted_to_sentences]
  *
  * @return [node_name, [len0, len1, ...], [syllable0, syllable1, ...], mean_lang]
  *
  * No use generic!!
  *
  * Ограничения:
  *   - Расчет число слогов призводится упрощенно - по числу гласных букв и усредненному языку
  *      а значит, встрачается не соотв. языку слов, то число слогов в нем будет 0.
  *      Возможно оценка из-за этого будет искажаться. Определять язык по одному слову? Будут промахи!
  * */
  public static List map(List<String> job) {
    List response = new ArrayList();
    String node = job.get(JobsFormer.IDX_NODE_NAME);
    String filename = job.get(JobsFormer.IDX_FILENAME);
    List<Integer> sentencesLengths = new ArrayList<Integer>();
    List<Integer> syllablesLengths = new ArrayList<Integer>();
    String meanLang = "unknown";
    try {
      Closer closer = Closer.create();
      try {
        BufferedReader reader = closer.register(new BufferedReader(new FileReader(filename)));
        String s;
        while ((s = reader.readLine()) != null) {
          int langPtr = s.indexOf(' ');
          List<String> words = new SentenceTokenizer().getWords(s.substring(langPtr, s.length()));
          sentencesLengths.add(words.size());

          // Получаем язык, нужно для деления на слоги
          meanLang = s.substring(0, langPtr);  // язык средний по документу
          int countSyllable = 0;
          for (String word : words) {
            countSyllable += SyllableCounterImpl.calc(word, meanLang);
          }
          syllablesLengths.add(countSyllable);
        }
      } catch (Throwable e) {
        closer.rethrow(e);
      } finally {
        closer.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Pack result
    response.addAll(Arrays.asList(
        node,
        sentencesLengths,
        syllablesLengths,
        meanLang));
    return response;
  }

}
