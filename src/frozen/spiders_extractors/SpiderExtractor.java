package frozen.spiders_extractors;

import core.adapters_3rdparty.TikaWrapper;
import common.Tools;
import frozen.crosscuttings.AppConstants;
import frozen.crosscuttings.ThroughLevelBoundaryError;
import frozen.jobs_processors.ProcessorTargets;

import java.util.List;


public class SpiderExtractor {
  //private static Logger log = Logger.getLogger(SpiderExtractor.class);
  // Main()
  public static void main(String [ ] args) {
    //BasicConfigurator.configure();
    try {
      // Получаем цели
      List<List<String>> targets = ProcessorTargets.runParser(AppConstants.SPIDER_TARGETS_FILENAME);
      for (List<String> target : targets) {
        try {
          // TODO(zaqwes): если файл существует, то будет перезаписан. Нужно хотя бы предупр.
          TikaWrapper.extractAndSaveText(target);
          TikaWrapper.extractAndSaveMetadata(target);
          //break;  // DEVELOP
        } catch (ExtractorException e) {
          // Ошибка может произойти на каждой итерации, но пусть обработка предолжается
          Tools.print(e.getMessage());
        }
      }
    } catch (ThroughLevelBoundaryError e) {
      Tools.print(e.getMessage());
    }
    Tools.print("Done. Spider extractor.\n");
  }
}
