package frozen.dal.accessors_text_file_storage;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 31.08.13
 * Time: 18:45
 * To change this template use File | Settings | File Templates.
 */
public interface FabricImmutableNodeControllers {
  public ImmutableNodeAccessor create(String pathToNode) throws NodeNoFound, NodeIsCorrupted;
}
