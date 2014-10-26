package frozen.crosscuttings.configurator;

import org.junit.Test;

public class GlobalConfiguratorTest {
  @Test(expected = NoFoundConfigurationFile.class)
  public void testGetPathToAppFolder() throws Exception {
    String path = "noExist-app.yaml";
    new GlobalConfigurator(path).getPathToAppFolder().get();
  }

  @Test
  public void testFileExist() throws Exception {
    String path = "./my.yaml";
    new GlobalConfigurator(path).getPathToAppFolder().get();
  }
}
