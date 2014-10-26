package core.text_extractors;

import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SubtitlesParser implements org.apache.tika.parser.Parser {
  @Override
  public Set<MediaType> getSupportedTypes(ParseContext parseContext) {
    return null;
  }

  // TODO: it's weak. 00:31:19,764 --> 00:31:22,823. Усилить бы регулярным выражением
  private static final String TIME_TICKET_SIGN_ = "-->";
  private static final String CONST_0_ = "- ";

  // Передать любой handler нельзя.
  @Override
  public void parse(
      InputStream stream, ContentHandler handler,
      Metadata metadata, ParseContext context) throws IOException, SAXException, TikaException
    {
    SpecialSymbols symbols = new SpecialSymbols();
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream
        ,
        //Charsets.US_ASCII
        Charsets.UTF_8
      ));

    List<String> items = new ArrayList<String>();
    handler.startDocument();
    while (true) {
      String buffer;
      if ((buffer = reader.readLine()) == null)
        break;

      // TODO: not effective
      // TODO: Делим пробелами, разделить не только пробелами
      ImmutableList<String> list = ImmutableList.copyOf(
        Splitter.on(CharMatcher.WHITESPACE)
          .trimResults()
          .omitEmptyStrings()
          .split(buffer));

      // Удаляем просто числа
      String line = Joiner.on(symbols.EMPTY_STRING).join(list);

      if (!list.isEmpty()
          && !(list.size() == 1 && StringUtils.isNumeric(line))
          && !buffer.contains(TIME_TICKET_SIGN_))
        {
        // Некоторые замены исходя из статистики
        // TODO: Убирать бы знаки прямой речи, но можно потерять информацию.
        buffer = CharMatcher.is(symbols.LEFT_ANGLE_BRACKET).replaceFrom(buffer, symbols.WHITESPACE);
        buffer = CharMatcher.is(symbols.RIGHT_ANGLE_BRACKET).replaceFrom(buffer, symbols.DOT);
        buffer = CharMatcher.is(symbols.APOSTROPHE).replaceFrom(buffer, symbols.ONE_QUOTE);
        buffer = buffer.replace(CONST_0_, symbols.WHITESPACE_STRING);

        handler.characters(buffer.toCharArray(), 0, buffer.length());
      }
    }
    // Summary
    handler.endDocument();
  }
}