package core.text_extractors;

import org.junit.Test;
import org.xml.sax.ContentHandler;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class SubtitlesContentHandlerTest {
  @Test
  public void testCharacters() throws Exception {
    List<String> sink = new ArrayList<String>();
    ContentHandler handler = new SubtitlesContentHandler(sink);

    handler.startDocument();

    String step0 = "hello";
    char [] bytes = step0.toCharArray();

    handler.characters(bytes, 0, bytes.length);

    assert sink.size() == 1;

    handler.endDocument();
  }
}
