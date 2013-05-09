import com.github.zaqwes8811.processor_word_frequency_index.AppConstants;
import com.github.zaqwes8811.processor_word_frequency_index.crosscuttings.ImmutableAppConfigurator;
import com.github.zaqwes8811.processor_word_frequency_index.crosscuttings.ImmutableProcessorTargets;
import com.github.zaqwes8811.processor_word_frequency_index.spiders_extractors.ImmutableTikaWrapper;
import org.junit.Test;

import com.github.zaqwes8811.processor_word_frequency_index.crosscuttings.CrosscuttingsException;

import java.util.List;

public class SpiderExtractorTest {
  static void print(Object msg) {
     System.out.println(msg);

  }

  @Test
  public void testDevelopSpider() {
      try {
        // Получаем цели
        String spiderTargetsFilename = AppConstants.SPIDER_TARGETS_FILENAME;
        List<List<String>> targets = ImmutableProcessorTargets.runParser(spiderTargetsFilename);
        for (List<String> target : targets) {
          String nodeName = target.get(ImmutableProcessorTargets.RESULT_NODE_NAME);
          String pathToFile = target.get(ImmutableProcessorTargets.RESULT_PATH);
          String fileName = target.get(ImmutableProcessorTargets.RESULT_FILENAME);

          // TODO(zaqwes): если файл существует, то будет перезаписан. Нужно хотя бы предупр.

          // Выделяем текст
          // Нужно передать имя исходного файла, и путь к итоговому(без расширения)
          ImmutableTikaWrapper tikaWrapper = new ImmutableTikaWrapper();
          tikaWrapper.extractAndSaveText(fileName, pathToFile, nodeName);

          // Формируем метаданные для каждой задачи
          //break;
        }

      } catch (CrosscuttingsException e) {
        System.out.println(e.getMessage());
      }
    }
}
