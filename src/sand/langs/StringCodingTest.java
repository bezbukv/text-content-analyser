package sand.langs;

/**
 *
 http://javatutor.net/articles/working-with-encodings
 */

import com.google.common.base.Charsets;
import org.junit.Test;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class StringCodingTest {

  @Test
  public void testCharBuffer() {
    char[] a = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'й'};
    String b = new String(a);
    assert b.equals("hello worlй");

    Charset charset = Charsets.US_ASCII;
    CharsetDecoder decoder = charset.newDecoder();
    CharBuffer bbuf = CharBuffer.wrap(a);
    //CharBuffer cbuf = decoder.decode(bbuf);

    //CharBuffer cbuf = new CharBuffer();
  }
}
