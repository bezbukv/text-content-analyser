package crosscuttings;

import com.google.common.io.Closer;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 29.04.13
 * Time: 20:51
 * To change this template use File | Settings | File Templates.
 *
 * Кофигуратор приложения
 */
final public class ImmutableAppConfigurator {
  private static final String ROOT_NAME = "App";
  public static String getPathToAppFolder() throws CrosscuttingsException {
    String fullCfgFilename = AppConstants.APP_CFG_FILENAME;
    Yaml yaml = new Yaml();
    try {
      Closer closer = Closer.create();
      try {
        InputStream input = closer.register(new FileInputStream(new File(fullCfgFilename)));
        Map<String, Object> object = (Map<String, Object>) yaml.load(input);
        Map topCfg = (Map)object;
        Map scriberConfiguration = (Map)((Map)topCfg.get(ROOT_NAME)).get("Scriber");

        String appFolder =(String)scriberConfiguration.get("app_folder");
        return appFolder;
      } catch (Throwable e) { // must catch Throwable
        throw closer.rethrow(e);
      } finally {
        closer.close();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw new CrosscuttingsException("File with cfg, no found. File name - "+fullCfgFilename);
    } catch (IOException e) {
      e.printStackTrace();
      throw new CrosscuttingsException("Error on read file - "+fullCfgFilename);
    }
  }
}
