package frozen.dal.accessors_text_file_storage;

public class FabricImmutableNodeControllersImpl implements FabricImmutableNodeControllers {
  @Override
  public ImmutableNodeAccessor create(String pathToNode) throws NodeNoFound, NodeIsCorrupted {
    return IdxNodeAccessor.createImmutableConnection(pathToNode);
  }
}
