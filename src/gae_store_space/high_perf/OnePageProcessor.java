package gae_store_space.high_perf;

import gae_store_space.PageKind;
import gae_store_space.SentenceKind;
import gae_store_space.WordKind;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import common.Tools;

import core.mapreduce.CountReducer;
import core.mapreduce.CounterMapper;
import core.nlp.PlainTextTokenizer;

public class OnePageProcessor {
	public static final String defaultPageName = "Korra";
	public static final String defaultGenName = "Default";
	public static final String defaultUserId = "User";
	
	private SubtitlesToPlainText convertor = new SubtitlesToPlainText();
	
	public String getTestFileName() {
    return "./test_data/korra/etalon.srt";
  }
	
  public String getGetPlainTextFromFile(String filename) {
  	try {
  		ArrayList<String> lines = new ArrayList<String>(Tools.fileToList(filename).asList());
  		return convertToPlainText(lines);
  	} catch(IOException e) {
  		throw new RuntimeException(e);
  	}
  }

  public String convertToPlainText(ArrayList<String> lines) {
  	String rawText = Joiner.on('\n').join(lines);
  	return convertor.convert(rawText);  	
  }
 
  public PageKind buildPageKind(String pageName, String filename) {  
    String plainText = getGetPlainTextFromFile(filename);
    return build(pageName, plainText);
  }

  private ArrayList<SentenceKind> getContentElements(String text) {
    ImmutableList<String> sentences = new PlainTextTokenizer().getSentences(text);

    // Пакуем
    ArrayList<SentenceKind> contentElements = new ArrayList<SentenceKind>();
    for (String sentence: sentences)
      contentElements.add(new SentenceKind(sentence));

    return contentElements;
  }
  
  // FIXME: DevDanger: operation must be idempotent!!!
  // Now no store operations
  public PageKind build(String name, String plainText) {
  	// FIXME: убрать отсюда весь доступ к хранилищу
  	ArrayList<SentenceKind> contentElements = getContentElements(plainText);
  	
    // TODO: BAD! В страницу собрана обработка
    Multimap<String, SentenceKind> wordHistogramSink = HashMultimap.create();
    CountReducer reducer = new CountReducer(wordHistogramSink);
    CounterMapper mapper = new CounterMapper(reducer);

    // Split
    mapper.map(contentElements);  // TODO: implicit, but be so

    ArrayList<WordKind.WordValue> value = new ArrayList<WordKind.WordValue>();
    for (String word: wordHistogramSink.keySet()) {
      Collection<SentenceKind> content = wordHistogramSink.get(word);
      int rawFrequency = content.size();
      value.add(new WordKind.WordValue(word, rawFrequency, content));
    }

    // Sort words by frequency and assign idx
    Collections.sort(value, new WordKind.WordValueFrequencyComparator());
    Collections.reverse(value);

    // Элементы отсортированы - это важно!!
    ArrayList<WordKind> words = new ArrayList<WordKind>();
    for (int i = 0; i < value.size(); i++) {
      WordKind.WordValue v = value.get(i);
      words.add(WordKind.create(v.word, v.sentences, v.frequency));
      words.get(i).setPointPos(i);
    }

    // Слова сортированы
    return new PageKind(name, contentElements, words, plainText);
  }
}
