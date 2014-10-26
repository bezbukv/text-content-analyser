package frozen.old;


import com.google.common.collect.Multiset;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 10.05.13
 * Time: 18:57
 * To change this template use File | Settings | File Templates.
 */
final public class NewReducer {

  public static final int IDX_SORTED_IDX = 5;

  public static List reduce_word_level_base(List task) {
    List one = new ArrayList();
    // Сортируем индекс частот
    Multiset<String> unsorted_index = (Multiset<String>)task.get(NewMapper.IDX_FREQ_INDEX);


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
}
