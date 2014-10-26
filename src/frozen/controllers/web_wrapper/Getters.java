package frozen.controllers.web_wrapper;


import java.util.*;

class Getters {
  private Getters() {}

  public static GetterOneWordData createFake() {
    return new FakeGetterOneWordData();
  }

  private static class FakeGetterOneWordData implements GetterOneWordData {
    @Override
    public Map<String, List<String>> getPerWordData() {
      List<String> word = new ArrayList<String>(Arrays.asList("jetty"));
      List<String> content = new ArrayList<String>(Arrays.asList("jetty here", "a jetty"));
      List<String> translate = new ArrayList<String>(Arrays.asList("Сервер", "Много используется"));

      Map<String, List<String>> oneCardContent = new HashMap<String, List<String>>();
      oneCardContent.put("word", word);
      oneCardContent.put("content", content);
      oneCardContent.put("translate", translate);
      return oneCardContent;
    }
  }
}
