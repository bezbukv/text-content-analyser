package gae_store_space;

import static gae_store_space.OfyService.ofy;
import gae_store_space.high_perf.OnePageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import servlets.protocols.PageSummaryValue;
import servlets.protocols.PathValue;

public class AppInstance {
	private static final Integer CACHE_SIZE = 5;
	
	private final OnePageProcessor processor = new OnePageProcessor();	
	
	Integer fakeState = new Integer(0);
	
	LoadingCache<String, Optional<PageKind>> pagesCache = CacheBuilder.newBuilder()
			.maximumSize(CACHE_SIZE)
			.build(
					new CacheLoader<String, Optional<PageKind>>() {
						public Optional<PageKind> load(String key) {
							return PageKind.restore(key);
						}
					});
	
	public void resetFullStore() {
		// пока создаем один раз и удаляем. классы могут менятся, лучше так, чтобы не было 
		//   конфликтов.
		//
  	ofy().delete().keys(ofy().load().type(PageKind.class).keys()).now();
  	ofy().delete().keys(ofy().load().type(GeneratorKind.class).keys()).now();
  	ofy().delete().keys(ofy().load().type(WordKind.class).keys()).now();
  	
  	// clear cache
  	pagesCache.cleanUp();
  	
  	createDefaultPages();  // нельзя это в конструктор
	}
	
	public PageKind createPageIfNotExist(String name, String text) {
		// FIXME: add user info
		List<PageKind> pages = 
				ofy().load().type(PageKind.class).filter("name = ", name).list();
		
		if (pages.isEmpty()) {
			OnePageProcessor processor = new OnePageProcessor();
	  	PageKind page = processor.build(name, text);
	  	
	  	GeneratorKind defaultGenerator = GeneratorKind.create(page.getRawDistribution(), fakeState.toString());
	  	fakeState++;
	  	defaultGenerator.persist();  	
	  	
	  	page.addGenerator(defaultGenerator);
	  	page.persist();
			return page;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	private void createDefaultPages() {
		{
	  	// Own tables
	  	// FIXME: GAE can't read file.
  		String name = OnePageProcessor.defaultPageName;
  		
  		String text = processor.getGetPlainTextFromFile(processor.getTestFileName());
  		createPageIfNotExist(name, text);
	 	}
  	
  	{
  		String name = OnePageProcessor.defaultPageName+"Copy";
  		String text = processor.getGetPlainTextFromFile(processor.getTestFileName());
  		createPageIfNotExist(name, text);
  	}
  	
  	{
	  	// Try read
	  	// Скрыл, так как падало, но должно работать!!
	  	List<PageKind> pages = 
	  			ofy().load().type(PageKind.class).filter("name = ", OnePageProcessor.defaultPageName).list();
	  	
	  	// FIXME: иногда страбатывает - почему - не ясно - список пуст, все вроде бы синхронно
	  	if (pages.isEmpty()) 
	  		throw new IllegalStateException();
	  	
	  	if (pages.size() > 1) 
	  		throw new IllegalStateException();
  	}
	}
	
	public AppInstance() {
		
	}
	
	// FIXME: may be non thread safe. Да вроде бы должно быть база то потокобезопасная?
	public PageKind getPage(String namePage) {
		try {
			return pagesCache.get(namePage).get();
    } catch (ExecutionException e) {
    	throw new RuntimeException(e);
    }
	}

	public static class Holder {
		static final AppInstance w = new AppInstance();
	}
	
	public static AppInstance getInstance() {
		return Holder.w;
	} 
	
	// пока не ясно, что за идентификация будет для пользователя
	// данных может и не быть, так что 
	public List<PageSummaryValue> getUserInformation(String userId) {
		// FIXME: add user info
		List<PageKind> pages = ofy().load().type(PageKind.class).list();
		
		List<PageSummaryValue> r = new ArrayList<PageSummaryValue>();
		for (PageKind page: pages) {
			r.add(PageSummaryValue.create(page.getName(), page.getGenNames()));
		}
		
		return r;
	}
	
	public void disablePoint(PathValue p) {
		PageKind page = getPage(p.pageName);
		GeneratorKind g = page.getGenerator(p.genName);
		g.disablePoint(p.pointPos);
		
		ofy().save().entity(g).now();
	} 
}
