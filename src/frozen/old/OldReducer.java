package frozen.old;


import com.google.common.collect.Multiset;

import java.util.*;

/**  */
public class OldReducer {

  public static final int IDX_SORTED_IDX = 5;

  public static List reduce_word_level_base(List task) {
    List one = new ArrayList();
    // Сортируем индекс частот
    Multiset<String> unsorted_index = (Multiset<String>)task.get(OldMapper.IDX_FREQ_INDEX);


    Set<String> elems = unsorted_index.elementSet();
    Map<String, Integer> unsorted_map = new HashMap<String, Integer>();
    for (String elem: elems) {
      unsorted_map.put(elem, unsorted_index.count(elem));
    }
    List<Map.Entry<String, Integer>> list = new LinkedList(unsorted_map.entrySet());

    // sort list based on comparator
    Collections.sort(list, new Comparator() {
      public int compare(Object o1, Object o2) {
        return ((Comparable) ((Map.Entry) (o2)).getValue())
          .compareTo(((Map.Entry) (o1)).getValue());
      }
    });

    // Сортированный список
    List<String> sorted_result_list = new ArrayList<String>();
    for (Map.Entry<String, Integer> entry : list) {
      sorted_result_list.add(entry.getKey());
    }

    // Добаляем к результатам
    one.addAll(task);
    one.add(sorted_result_list);
    return one;
  }

  public static final int IDX_NODE_NAME = 0;
  public static final int IDX_SENT_LENGTH_MEAN = 1;
  public static final int IDX_RE = 2;
  public static final int IDX_LANG = 3;

  public static final String NOTE_MEAN_LEN_SENT = "mean_length_sentence";
  public static final String NOTE_RE = "RE";
  public static final String NOTE_MEAN_TIME_FOR_READ = "mean_time_for_read";
  public static final String NOTE_MEAN_LANG = "mean_language";

  public static final double RU_MEAN_SPEED_READ = 250.0;  // word/min

  /*
  public static Map<String, String> reduce_sentences_level(List task) {
    // Средняя длина предложения
    List<Integer> s = (List<Integer>)task.get(
        MapperSentencesLevel.IDX_SENTENCES_LENS);
    Double meanLengthSentence = SummatorLists.meanList(s);
    Double countWords = SummatorLists.sumIntList(s)*1.0;

    // Средняя длина слога
    s = (List<Integer>)task.get(MapperSentencesLevel.IDX_COUNT_SYLLABLES);
    Double meanLengthSyllable = SummatorLists.sumIntList(s)/countWords;

    Double RE = new Double(-1);
    Double timeForRead = new Double(-1);
    String lang = (String)task.get(MapperSentencesLevel.IDX_LANG);
    if (lang.equals("ru")) {
      RE = (206.835 - 60.1*meanLengthSyllable - 1.015*meanLengthSentence);

      timeForRead = countWords/RU_MEAN_SPEED_READ/60;  // часов
    } else if (lang.equals("en")) {
      RE = (206.835 - 84.6*meanLengthSyllable - 1.015*meanLengthSentence);
      timeForRead = countWords/RU_MEAN_SPEED_READ/60;  // часов
    } else {
      String nodeName = (String)task.get(OldMapper.IDX_NODE_NAME);
      Tools.print("Warning: Lang no used - "+lang+". Node - "+nodeName);
    }

    // Make results
    Map<String, String> result_reduce_stage = new TreeMap<String, String>();
    result_reduce_stage.put("mean_length_sentence", meanLengthSentence.toString());
    result_reduce_stage.put("RE", RE.toString());
    result_reduce_stage.put("mean_time_for_read", timeForRead.toString());
    result_reduce_stage.put("mean_language", lang);
    return result_reduce_stage;
  } */
}
