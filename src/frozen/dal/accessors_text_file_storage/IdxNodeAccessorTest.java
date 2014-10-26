package frozen.dal.accessors_text_file_storage;

import com.google.common.base.Optional;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IdxNodeAccessorTest {
  @Test
  public void testNodeRight() throws NodeNoFound, NodeAlreadyExist, NodeIsCorrupted {
    String pathToNode = "./app-folder/bec-node";
    Optional<ImmutableNodeAccessor> accessor = 
    		Optional.of(IdxNodeAccessor.createImmutableConnection(pathToNode));
	  assertEquals(false, accessor.equals(Optional.absent()));
  }
}
