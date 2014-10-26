package frozen.dal.accessors_text_file_storage;

import com.google.common.base.Joiner;
import common.Tools;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 02.06.13
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
public class ImmutableOntoAssistant {


  static public void main(String [] args) {
    StringBuilder result = new StringBuilder();
      // Получаем сортированных индекс
    List<String> nodes = ImmutableBaseCoursor.getListNodes();
    for (String node: nodes) {
      Tools.print(node);
      // сортированных индекс
      List<String> sorted_idx = ImmutableIdxGetters.get_sorted_idx(node);
      Map<String, String> rest_idx = ImmutableIdxGetters.get_rest_idx(node);
      Map<String, Integer> freq_idx = ImmutableIdxGetters.get_freq_idx(node);

      int i = 0;
      for (String stem: sorted_idx) {
        if (i%7  != 0) {
        Tools.print(
          Joiner.on(" * ")
            .join(freq_idx.get(stem), stem, rest_idx.get(stem)));
        } else {
          Tools.print(
            Joiner.on(" * ")
              .join("\n" + freq_idx.get(stem), stem, rest_idx.get(stem)));
        }
          i++;
      }
    }


      // Частота, стем, остатки

  }
}
