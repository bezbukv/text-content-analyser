package frozen.controllers.web_wrapper;

import java.util.List;
import java.util.Map;

// Пусть пока только геттеры, не знаю как назвать нормально
public interface GetterOneWordData {
  public Map<String, List<String>> getPerWordData();
}