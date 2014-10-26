package core.evaluators;

import com.google.common.base.Joiner;
import frozen.dal.accessors_text_file_storage.ImmutableBaseCoursor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zaqwes on 5/17/14.
 */
public class NotesBuilderTest {
  // Launcher
  @Test
  public void testCreating() {

    // Получаем адреса соответствующие узлам и оцененный язык
    List<String> nodes = ImmutableBaseCoursor.getListNodes();
    List<String> rpt = new ArrayList<String>(
      Arrays.asList(Joiner.on(";")
        .join(
          "Имя документа",
          "Флеш",
          "Время прочтения",
          "Ср. дл. предл.",
          "20% частых",
          "80% редких",
          "частые сост. 80% слов. состава",
          "редкие - 20% состава")));

    for (String node : nodes) {

      //Map<String, String> node_static_notes_info = get_notes_for_node(node);
      //rpt.add(get_one_record(node, node_static_notes_info));
    }

    // пишем результат
    /*try {
      Utils.list2file(rpt, Joiner.on(AppConstants.PATH_SPLITTER)
        .join(
          "rpts",
          "real_notes.csv"));
    } catch (IOException e) {
      e.printStackTrace();
    }*/
  }
}
