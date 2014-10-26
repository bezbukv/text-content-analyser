package gae_store_space.high_perf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.parser.Parser;
import org.xml.sax.ContentHandler;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Closer;

import core.text_extractors.SpecialSymbols;
import core.text_extractors.SubtitlesContentHandler;
import core.text_extractors.SubtitlesParser;

public class SubtitlesToPlainText {
	//FIXME: make strategy
	public String convert(String content) {
		try {
			// Пока файл строго юникод - UTF-8
			Closer closer = Closer.create();
			try {
			  // http://stackoverflow.com/questions/247161/how-do-i-turn-a-string-into-a-stream-in-java
			  InputStream in = closer.register(new ByteArrayInputStream(content.getBytes(Charsets.UTF_8)));
			  Parser parser = new SubtitlesParser();
			  List<String> sink = new ArrayList<String>();
			  ContentHandler handler = new SubtitlesContentHandler(sink);
			  parser.parse(in, handler, null, null);
			
			  // Получили список строк.
	      SpecialSymbols symbols = new SpecialSymbols();
	      return Joiner.on(symbols.WHITESPACE_STRING).join(sink);
	    } catch (Throwable e) {
	      throw closer.rethrow(e);
	    } finally {
	      closer.close();
	    }
   	} catch(IOException e) {
   		throw new RuntimeException(e);
   	}
	}
}
