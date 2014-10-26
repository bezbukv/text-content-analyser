package frozen.controllers.web_wrapper;

import com.google.common.collect.ImmutableList;
import frozen.dal.accessors_text_file_storage.*;


// TODO: перейти на получение map, а не списков. Идея реаширения была избыточной.
public class ContainerNodeControllers {
  private final ImmutableList<ImmutableNodeAccessor> NODES;
  public ImmutableList<ImmutableList<ImmutableList<String>>> getPerWordData(Integer node) {
    ImmutableNodeAccessor accessor = NODES.asList().get(node);
    ListGetter getter = new ListGetterImpl(accessor);
    return getter.getPerWordData();
  }


  public ContainerNodeControllers(ImmutableList<ImmutableNodeAccessor> nodes) {
    // В try сделать нельзя - компилятор будет ругаться не неинициализованность
    NODES = nodes;  // Должна упасть если при ошибке дошла до сюда
  }
}

