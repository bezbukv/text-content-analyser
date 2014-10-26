package sand.third_party_tests.guava;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 11.03.14
 * Time: 20:24
 * To change this template use File | Settings | File Templates.
 */
public class GuavaHashingTest {

  @Test
  public void testBase() {
    HashFunction hf = Hashing.md5();
    String name = "hallo";
    HashCode hc = hf.newHasher()
      .putString(name, Charsets.UTF_8)
      .hash();
    Integer hash = hc.asInt();

    HashCode hc2 = hf.newHasher()
      .putString(name, Charsets.UTF_8)
      .hash();
    Integer hash2 = hc.asInt();
    assert hash.equals(hash2);
  }
}
