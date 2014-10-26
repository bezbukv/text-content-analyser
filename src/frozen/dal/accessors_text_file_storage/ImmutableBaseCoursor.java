/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 09.05.13
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */

package frozen.dal.accessors_text_file_storage;



import com.google.common.base.Joiner;
import frozen.jobs_processors.ProcessorTargets;
import frozen.crosscuttings.AppConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Список получаем по размеченным папкам в временной директории индекса
final public class ImmutableBaseCoursor {
  public static List<String> getListNodes() {
    // Получаем список узлов по папкам, а на по заданиям
    String pathToTmpFolder =
      Joiner.on(AppConstants.PATH_SPLITTER).join(
        ProcessorTargets.getPathToIndex(),
        AppConstants.TMP_FOLDER);

    File rootTmp = new File(pathToTmpFolder);

    // Итоговый список
    List<String> listNodes = new ArrayList<String>();
    listNodes.addAll(Arrays.asList(rootTmp.list()));
    return listNodes;
  }
}
