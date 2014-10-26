// Tasks:
//   Сперва подключить кеш,
//   Затем думать о распределении
//   Затем думать об удалении.

// http://www.oracle.com/technetwork/java/javase/documentation/index-137868.html#styleguide
// TODO: http://www.oracle.com/technetwork/articles/marx-jpa-087268.html
// TODO: скрыть персистентность в этом классе, пусть сам себя сохраняет и удаляет.
// TODO: Функция очистки данных связанных со страницей, себя не удаляет.
// TODO: Добавить оценки текста
// не хочется выносить ofy()... выше. Но может быть, если использовать класс пользователя, то он может.
/**
 * About:
 *   Отражает один элемент данный пользователя, например, один файл субтитров.
 */

package gae_store_space;

import static gae_store_space.OfyService.ofy;
import gae_store_space.high_perf.OnePageProcessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import servlets.protocols.WordDataValue;
import net.jcip.annotations.NotThreadSafe;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

import core.math.DistributionElement;

@NotThreadSafe
@Entity
public class PageKind {
  private PageKind() { }

  @Id Long id;

  @Index String name;
  
  String rawSource;  // для обновленной версии

  // Формированием не управляет, но остальным управляет.
  // Обязательно отсортировано
  @Ignore private ArrayList<WordKind> wordKinds = new ArrayList<WordKind>();
  @Ignore private List<SentenceKind> sentencesKinds = new ArrayList<SentenceKind>();

  // FIXME: почему отношение не работает?
  // Попытка сделать так чтобы g не стал нулевым указателем
  // все равно может упасть. с единичным ключем фигня какая-то
  @Load  
  private List<Key<GeneratorKind>> generators = new ArrayList<Key<GeneratorKind>>();  // FIXME: вообще это проблема!!
  
  public String getName() { return name; }
  
  // FIXME: если появится пользователи, то одного имени будет мало
  public static Optional<PageKind> restore(String pageName) {
  	List<PageKind> pages = 
    		ofy().load().type(PageKind.class).filter("name = ", pageName).list();
    
    if (pages.size() != 1)
  		throw new IllegalStateException();
    
    PageKind barePage = pages.get(0);
    
    String rawSource = barePage.rawSource;
    
    // обрабатываем
    OnePageProcessor p = new OnePageProcessor();
    PageKind page = p.build(barePage.name, rawSource);
    
    // теперь нужно запустить процесс обработки,
    barePage.assign(page);
    
    return Optional.fromNullable(barePage);  // 1 item
  }
  
  private void assign(PageKind rhs) {
  	wordKinds = rhs.wordKinds;
  	sentencesKinds = rhs.sentencesKinds;
  }
  
  public void persist() {
  	ofy().save().entity(this).now();
  }

  // TODO: перенести бы в класс генератора, но!! это затрудняет выборку, т.к. имя не уникально 
  //
  // throws: 
  //   IllegalStateException - генератор не найден. Система замкнута, если 
  //     по имение не нашли генератора - это нарушение консистентности. Имена генереторов
  //     вводится только при создании, потом они только читаются.
  public GeneratorKind getGenerator(String name) {  
  	List<GeneratorKind> gen = 
  			ofy().load().type(GeneratorKind.class)
	  			.filterKey("in", generators)
	  			.filter("name = ", name)
	  			.list();
  	
  	if (gen.isEmpty() || gen.size() != 1)
  		throw new IllegalStateException();
  	
  	gen.get(0).reset();
  	
  	return gen.get(0);
  }
  
  public List<String> getGenNames() {
  	List<GeneratorKind> gen = 
  			ofy().load().type(GeneratorKind.class)
	  			.filterKey("in", generators)
	  			.list();
  	
  	List<String> r = new ArrayList<String>();
  	for (GeneratorKind g: gen) {
  		r.add(g.getName()); 
  	}	
  	return r;
  }

  public void addGenerator(GeneratorKind gen) {
  	Key<GeneratorKind> k = Key.create(gen);
    generators.add(k);
  }

  // Это при создании с нуля
  public PageKind(
  		String name, ArrayList<SentenceKind> items, ArrayList<WordKind> words, String rawSource) 
  	{
    this.name = name;
   	this.wordKinds = words;
   	this.sentencesKinds = items;    
    this.rawSource = rawSource;
  }

  // About: Возвращать частоты, сортированные по убыванию.
  public ArrayList<DistributionElement> getRawDistribution() {
    // Сортируем - элементы могут прийти в случайном порядке
    Collections.sort(wordKinds, WordKind.createFrequencyComparator());
    Collections.reverse(wordKinds);

    // Form result
    ArrayList<DistributionElement> distribution = new ArrayList<DistributionElement>();
    for (WordKind word : wordKinds)
      distribution.add(new DistributionElement(word.getRawFrequency()));

    return distribution;
  }
   
  public Optional<WordDataValue> getWordData(String genName) {
  	GeneratorKind go = getGenerator(genName);
    
		Integer pointPosition = go.getPosition();
		WordKind wordKind =  getWordKind(pointPosition);
		String word = wordKind.getWord();
		ImmutableList<SentenceKind> contentKinds = wordKind.getContendKinds();

		ArrayList<String> content = new ArrayList<String>(); 
		for (SentenceKind e: contentKinds)
		  content.add(e.getSentence());
		
		return Optional.of(new WordDataValue(word, content, pointPosition));
  }
  
  // FIXME: а логика разрешает Отсутствующее значение?
  // http://stackoverflow.com/questions/2758224/assertion-in-java
  // генераторы могут быть разными, но набор слов один.
  private WordKind getWordKind(Integer pos) {
  	if (! (pos < this.wordKinds.size()))
  		throw new IllegalArgumentException();
  	
		return wordKinds.get(pos);
  }
}
