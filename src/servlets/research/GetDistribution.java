package servlets.research;

import gae_store_space.GeneratorKind;
import gae_store_space.PageKind;
import gae_store_space.AppInstance;
import gae_store_space.high_perf.OnePageProcessor;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sand.third_party_tests.gae.EasyKind;
import servlets.protocols.PathValue;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.googlecode.objectify.Key;

import static gae_store_space.OfyService.ofy;


// CASE: на вебе есть список генераторов страницы, берем имя генератора, и для него возврящаем 
//   распределение. Если генератор не найден - данные на вебе устарели.
public class GetDistribution extends HttpServlet {
  private static final long serialVersionUID = 4122657047047348423L;
  
  private AppInstance app = AppInstance.getInstance(); 
  
	@Override
  public void doGet(
    HttpServletRequest request,
    HttpServletResponse response) throws ServletException, IOException
  {
		
		String value = request.getParameter("arg0");
		
		if (value == null) 
			throw new IllegalArgumentException();
		
		PathValue path = new Gson().fromJson(value, PathValue.class);
		
		// Срабатывает только один раз
		// TODO: Генератора реально может и не быть, или не найтись. Тогда лучше вернуть не ноль, а что-то другое 
		// FIXME: страница тоже может быть не найдена
		PageKind page = app.getPage(path.pageName);  
  	GeneratorKind gen = page.getGenerator(path.genName);
  	
  	response.setContentType("text/html");
  	response.setStatus(HttpServletResponse.SC_OK);

  	String r = new Gson().toJson(gen.getDistribution());

    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(r);
  }
}
