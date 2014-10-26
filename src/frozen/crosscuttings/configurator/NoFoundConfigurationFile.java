package frozen.crosscuttings.configurator;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 23.07.13
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class NoFoundConfigurationFile extends Exception {
  public NoFoundConfigurationFile(String msg) {
    super(msg);
  }
  public NoFoundConfigurationFile(Exception e) {
    super(e);
  }

  // Если просто getM.. то открывает детали реализации!
  private String fileName;
  public void setFileName(String name) {
    fileName = name;
  }
  public String getFileName() {return fileName;}
}
