package frozen.crosscuttings.configurator;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closer;
import net.jcip.annotations.NotThreadSafe;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.scanner.ScannerException;
import frozen.crosscuttings.CrosscuttingsException;

import java.io.*;
import java.util.List;
import java.util.Map;


// Для тестиования удобно передать путь к конфигурационному файлу.
@NotThreadSafe
public class GlobalConfigurator {

  //private final String ROOT_NAME = "App";
  private final String APP_CFG_FULL_FILENAME;

  public GlobalConfigurator(String configurationFileName) {
    APP_CFG_FULL_FILENAME = configurationFileName;
  }

  private static abstract class Reader<V> {
    // Конечную точки заворачиваем в Optional
    public abstract Optional<V> extract(ImmutableList<String> pieces, Map object);
  }

  private static class ReaderDecorator<V> {
    private final String CFG_FULL_FILENAME;
    public ReaderDecorator(String filename) {
      CFG_FULL_FILENAME = filename;
    }

    public Optional<V> read(Reader<V> reader, String key)
        throws NoFoundConfigurationFile, ConfigurationFileIsCorrupted, NoFoundRecordInConfiguration {
      try {
        Closer closer = Closer.create();
        Optional<V> path = Optional.absent();
        try {
          InputStream input = closer.register(new FileInputStream(new File(CFG_FULL_FILENAME)));
          Map object = (Map)(Map<String, Object>) (new Yaml()).load(input);
          ImmutableList<String> pieces = ImmutableList.copyOf(Splitter.on("/").split(key));
          path = reader.extract(pieces, object);
        } catch (Throwable e) {
          throw closer.rethrow(e);
        } finally {
          closer.close();
        }
        return path;
      } catch (FileNotFoundException e) {
        NoFoundConfigurationFile c = new NoFoundConfigurationFile(e);
        c.setFileName(CFG_FULL_FILENAME);
        throw c;
      } catch (ScannerException e) {
        throw new ConfigurationFileIsCorrupted(e);
      } catch (NullPointerException e) {
        throw new NoFoundRecordInConfiguration(e, key);
      } catch (IOException e) {
        throw new CrosscuttingsException(e);
      }
    }
  }

  // Папка не обязательно в рабочей директории программы
  public Optional<String> getPathToAppFolder()
      throws NoFoundConfigurationFile, ConfigurationFileIsCorrupted {
    String requestedPath = "for-scribe/app-folder";
    try {
    return new ReaderDecorator<String>(this.APP_CFG_FULL_FILENAME).read(
        new Reader<String>() {
          @Override
          public Optional<String> extract(ImmutableList<String> pieces, Map object) {
              Map step = (Map)object.get(pieces.get(0));
              String result = (String)step.get(pieces.get(1));
              return Optional.of(result);
          }
        }, requestedPath);
    } catch (NoFoundRecordInConfiguration e) {
      // Путь задан жестко, и если его нет, то файл поврежден
      throw new ConfigurationFileIsCorrupted(e);
    }
  }

  // Получить список узлов в виде путей к ним.
  //
  // Пути уникальные.
  public Optional<ImmutableSet<String>> getRegisteredNodes()
    throws NoFoundConfigurationFile, ConfigurationFileIsCorrupted {
    String requestedPath = "registered-nodes";
    try {
      return new ReaderDecorator<ImmutableSet<String>>(this.APP_CFG_FULL_FILENAME).read(
          new Reader<ImmutableSet<String>>() {
            @Override
            public Optional<ImmutableSet<String>> extract(ImmutableList<String> pieces, Map object) {
              List<String> nodes = (List)object.get(pieces.get(0));
              return Optional.of(ImmutableSet.copyOf(nodes));
            }
          }, requestedPath);
    } catch (NoFoundRecordInConfiguration e) {
      // Путь задан жестко, и если его нет, то файл поврежден
      throw new ConfigurationFileIsCorrupted(e, "Path no found - "+requestedPath);
    }
  }
}
