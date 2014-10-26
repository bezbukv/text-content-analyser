package frozen.old;

import core.math.SummatorLists;
import common.Tools;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 11.05.13
 * Time: 20:38
 * To change this template use File | Settings | File Templates.
 */
final public class SentencesReduce {
  public static final int IDX_NODE_NAME = 0;
  public static final int IDX_SENT_LENGTH_MEAN = 1;
  public static final int IDX_RE = 2;
  public static final int IDX_LANG = 3;

  public static final String NOTE_MEAN_LEN_SENT = "mean_length_sentence";
  public static final String NOTE_RE = "RE";
  public static final String NOTE_MEAN_TIME_FOR_READ = "mean_time_for_read";
  public static final String NOTE_MEAN_LANG = "mean_language";

  public static final double RU_MEAN_SPEED_READ = 250.0;  // word/min

  public static Map<String, String> reduce_sentences_level(List task) {
    // Средняя длина предложения
    List<Integer> s = (List<Integer>)task.get(
        SentencesMapper.IDX_SENTENCES_LENS);
    Double meanLengthSentence = SummatorLists.meanList(s);
    Double countWords = SummatorLists.sumIntList(s)*1.0;

    // Средняя длина слога
    s = (List<Integer>)task.get(SentencesMapper.IDX_COUNT_SYLLABLES);
    Double meanLengthSyllable = SummatorLists.sumIntList(s)/countWords;

    Double RE = new Double(-1);
    Double timeForRead = new Double(-1);
    String lang = (String)task.get(SentencesMapper.IDX_LANG);
    if (lang.equals("ru")) {
      RE = (206.835 - 60.1*meanLengthSyllable - 1.015*meanLengthSentence);

      timeForRead = countWords/RU_MEAN_SPEED_READ/60;  // часов
    } else if (lang.equals("en")) {
      RE = (206.835 - 84.6*meanLengthSyllable - 1.015*meanLengthSentence);
      timeForRead = countWords/RU_MEAN_SPEED_READ/60;  // часов
    } else {
      String nodeName = (String)task.get(NewMapper.IDX_NODE_NAME);
      Tools.print("Warning: Lang no used - " + lang + ". Node - " + nodeName);
    }

    // Make results
    Map<String, String> result_reduce_stage = new TreeMap<String, String>();
    result_reduce_stage.put("mean_length_sentence", meanLengthSentence.toString());
    result_reduce_stage.put("RE", RE.toString());
    result_reduce_stage.put("mean_time_for_read", timeForRead.toString());
    result_reduce_stage.put("mean_language", lang);
    return result_reduce_stage;
  }
}
