package coursors;

import com.google.common.collect.Multiset;
import common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Делает комплексные выборки данных
public final class ImmutableDataExtractor {
  static List<String> get_ww80_list(String node) {
    Map<String, String> base_node_notes = NotesProcessor.get_notes_for_node(node);
    List<String> sorted_base_idx = ImmutableIdxGetters.get_sorted_idx(node);
    int WW80 =  Integer.valueOf(base_node_notes.get(NotesProcessor.NOTE_N80_CAPACITY), 10);
    List<String> WW80List = sorted_base_idx.subList(0, WW80);
    List<String> WW20List = sorted_base_idx.subList(WW80, sorted_base_idx.size());
    return WW80List;
  }

  static List<String> get_ww20_list(String node) {
    Map<String, String> base_node_notes = NotesProcessor.get_notes_for_node(node);
    List<String> sorted_base_idx = ImmutableIdxGetters.get_sorted_idx(node);
    int WW80 =  Integer.valueOf(base_node_notes.get(NotesProcessor.NOTE_N80_CAPACITY), 10);
    List<String> WW80List = sorted_base_idx.subList(0, WW80);
    List<String> WW20List = sorted_base_idx.subList(WW80, sorted_base_idx.size());
    return WW20List;
  }

  ///
  static Multiset<String> get_follow_data(String base_node, List<String> rest_nodes) {
    // получаем оценки для базового индекса
    List<String> WW20ListBase = get_ww20_list(base_node);
    utils.print("Document name: " + base_node);

    // обрабатываем по узлу
    for (String node: rest_nodes) {
      Map<String, Integer> freq_idx = ImmutableIdxGetters.get_freq_idx(node);
      // Получаем оценки для одного узла
      List<String> WW80List = get_ww80_list(node);
      utils.print("\n"+node+"; WW80="+WW80List.size()+"; Число уникальных слов="+freq_idx.keySet().size());
      List<String> cross_words = new ArrayList<String>();
      for (String word: WW20ListBase) {
        if (WW80List.contains(word)) {
          cross_words.add(word+"/"+freq_idx.get(word));
        }
      }
      utils.print(cross_words.size()+" "+cross_words);
    }
    return null;
  }


}
