package frozen.controllers.web_wrapper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import frozen.dal.accessors_text_file_storage.*;
import org.junit.Test;
import frozen.crosscuttings.configurator.GlobalConfigurator;

public class ContainerNodeControllersTest {
  @Test
  public void testGeneratePackage() throws Exception {
    BuilderControllers builder = new BuilderControllers();
    String pathToCfgFile = "./my.yaml";
    ImmutableSet<String> namesNodes = new GlobalConfigurator(pathToCfgFile).getRegisteredNodes().get();
    ImmutableList<ImmutableNodeAccessor> controllers = builder.createControllersForAllNodes(
        namesNodes, new FabricImmutableNodeControllersImpl());
    ContainerNodeControllers container = new ContainerNodeControllers(controllers);
    container.getPerWordData(0);
  }
}
