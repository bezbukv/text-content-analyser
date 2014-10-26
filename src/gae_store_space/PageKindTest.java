package gae_store_space;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.base.Optional;
import com.google.common.collect.Ordering;

import core.math.DistributionElement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import servlets.protocols.WordDataValue;
import gae_store_space.high_perf.OnePageProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static gae_store_space.OfyService.ofy;
import static org.junit.Assert.*;


// Это таки юнитест, т.к. работает с фейковой базой данных
public class PageKindTest {
  private static final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  public PageKind buildContentPage(String pageName) {
  	OnePageProcessor processor = new OnePageProcessor();
    return processor.buildPageKind(pageName, processor.getTestFileName());
  }

  @Before
  public void setUp() { helper.setUp(); }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testCreateAndPersist() throws Exception {
    PageKind page = buildContentPage(OnePageProcessor.defaultPageName);
    GeneratorKind gen = GeneratorKind.create(page.getRawDistribution());
    ofy().save().entity(gen).now();
    page.addGenerator(gen);
    ofy().save().entity(page).now();
  }

  @Test
  public void testGetDistribution() throws IOException {
    ofy().save().entity(buildContentPage(OnePageProcessor.defaultPageName)).now();
    
    // Очень медленно!!
    PageKind page =
    		ofy().load().type(PageKind.class)
    			.filter("name =", OnePageProcessor.defaultPageName)
    			.limit(1).first().now();

    /// Queries
    ArrayList<DistributionElement> distribution = page.getRawDistribution();
    assertFalse(distribution.isEmpty());

    // TODO: how do that?
    boolean sorted = Ordering.natural().reverse().isOrdered(distribution);
    assertTrue(sorted);
  }

  @Test
  public void testDeletePage() {
    // TODO: Delete full page
  }
  
  private PageKind putPagesInStore() {
  	// Check store
    String activePageName = OnePageProcessor.defaultPageName;
    PageKind loadedPage =
      ofy().load().type(PageKind.class).filter("name = ", activePageName).first().now();
    assertNull(loadedPage);  // с одним именем могуть быть, id будут разными

    // Create new page
    PageKind page = buildContentPage(activePageName);
    GeneratorKind gen = GeneratorKind.create(page.getRawDistribution());
    ofy().save().entity(gen).now();
    page.addGenerator(gen);
    ofy().save().entity(page).now();
    
    // создаем две страницы, важно для проверки разделения запросов.
    // Add noise page
    ofy().save().entity(buildContentPage(activePageName+"_noise")).now();
    
    return page;
  }

  @Deprecated
  @Test
  public void testGetWordData() {
    // Получаем все сразу, но это никчему. Можно передать подсписок, но это не то что хотелось бы.
    // Хотелось бы выбирать по некоторому критерию.
    // https://groups.google.com/forum/#!topic/objectify-appengine/scb3xNPFszE
    // http://stackoverflow.com/questions/9867401/objectify-query-filter-by-list-in-entity-contains-search-parameter
    // http://bighow.net/3869301-Objectify___how_to__Load_a_List_lt_Ref_lt___gt__gt__.html
    //
    // http://stackoverflow.com/questions/11924572/using-in-query-in-objectify
    //
    // https://www.mail-archive.com/google-appengine-java@googlegroups.com/msg09389.html
    //
    // TODO: troubles. Может добавить метод выкалывания точек?
    // TODO: Может лучше сделать ссылкой-ключом?
    // TODO: может лучше внешний, а данные получать из страницы. Но будут доп. обращ. к базе.
    //   можно использовать кэши, но как быть с обновлением данных?
  	PageKind page = putPagesInStore();

    // queries
    Integer pointPosition = page.getGenerator(OnePageProcessor.defaultGenName).getPosition();

    /*
    // слово одно, но если страниц много, то получим для всех
    List<WordKind> words = ofy().load()
        .type(WordKind.class)
        //.ancestor(page)  // don't work
        //.parent(page)  // don't work
        .filterKey("in", page.getWordKeys()).filter("pointPos =", pointPosition)
        .list();
    assertEquals(words.size(), 1);  // не прошли не свои страницы
    
    WordKind word = words.get(0);
    
    List<SentenceKind> content = word.getContendKinds();

    for (SentenceKind e: content) {
      String v = e.getSentence();

      // TODO: пока будет работать. Сейчас используется только стеммер
      // http://stackoverflow.com/questions/2275004/in-java-how-to-check-if-a-string-contains-a-substring-ignoring-the-case
      boolean in = v.toLowerCase().contains(word.getWord().toLowerCase());
      assertTrue(in);
    }
    // запрещаем точку
    */
  }
  
  @Test 
  public void testGetPackedWordData() {
  	PageKind page = putPagesInStore();
  	Optional<WordDataValue> v = page.getWordData(OnePageProcessor.defaultGenName);
  	assertTrue(v.isPresent());
  } 
}
