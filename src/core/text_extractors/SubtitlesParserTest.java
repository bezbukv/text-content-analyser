package core.text_extractors;

import core.nlp.PlainTextTokenizer;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Closer;
import common.Tools;
import org.apache.tika.parser.Parser;
import org.junit.Test;
import org.xml.sax.ContentHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class SubtitlesParserTest {
  @Test
  public void testSrtChainOnGAE() throws IOException {
    String filename = "/home/zaqwes/work/statistic/the.legend.of.korra.a.new.spiritual.age.(2013).eng.1cd.(5474296)/" +
      "The Legend of Korra - 02x10 - A New Spiritual Age.WEB-DL.BS.English.HI.C.orig.Addic7ed.com.srt";
    String rawText = Joiner.on('\n').join(Tools.fileToList(filename));
    //InputStream in = closer.register(new FileInputStream(new File(filename)));  // No in GAE

    // Пока файл строго юникод - UTF-8
    Closer closer = Closer.create();
    try {
      // http://stackoverflow.com/questions/247161/how-do-i-turn-a-string-into-a-stream-in-java
      InputStream in = closer.register(new ByteArrayInputStream(rawText.getBytes(Charsets.UTF_8)));
      Parser parser = new SubtitlesParser();
      List<String> sink = new ArrayList<String>();
      ContentHandler handler = new SubtitlesContentHandler(sink);
      parser.parse(in, handler, null, null);

      // Получили список строк.
      SpecialSymbols symbols = new SpecialSymbols();
      String text = Joiner.on(symbols.WHITESPACE_STRING).join(sink);
      assertFalse(text.isEmpty());

      ImmutableList<String> sentences = new PlainTextTokenizer().getSentences(text);
      assertFalse(sentences.isEmpty());

      // TODO: WARNING! Нужно просматривать глазами. Могут попадатся артифакты

    } catch (Throwable e) { // must catch Throwable
      throw closer.rethrow(e);
    } finally {
      closer.close();
    }
  }
}
