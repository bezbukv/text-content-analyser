package servlets;

import gae_store_space.AppInstance;
import gae_store_space.high_perf.OnePageProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jcip.annotations.NotThreadSafe;

import org.javatuples.Pair;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;

import core.text_extractors.ParserPOSTFile;

@NotThreadSafe
public class FileAccepter extends HttpServlet {
  private static final long serialVersionUID = -8709394805924640800L;
  
	private AppInstance app = AppInstance.getInstance(); 
	
	// FIXME: нужно выделить имя файла, иначе похоже файл не идентифицировать.
	private ImmutableList<String> process(ArrayList<String> in) {
		if (in.size() < 3)
			throw new IllegalArgumentException();
		
		// http://stackoverflow.com/questions/24769832/uploaded-file-only-contains-webkitformboundary
		List<String> workSpace = in.subList(1, in.size()-1);

		String contentDisposition = workSpace.get(0);
		
		Pair<String, String> pair = new ParserPOSTFile().getNameAndFilename(contentDisposition);
				
		// выделяем заголовок
		String name = pair.getValue0();
		String filename = pair.getValue1();
		
		if (name.isEmpty() || filename.isEmpty())
			throw new IllegalArgumentException();
		
		// Где-то тут нужно перейти на нижний уроветь - спрятать его будет нужно
		// FIXME: ошибки парсинга
		OnePageProcessor processor = new OnePageProcessor();
		
		ArrayList<String> sentences = new ArrayList<String>(workSpace.subList(2, workSpace.size()-1));
		String text = processor.convertToPlainText(sentences);
		app.createPageIfNotExist(filename, text);
		
		return ImmutableList.copyOf(in);
	}

	// http://commons.apache.org/proper/commons-fileupload/using.html
	@Override
  public void doPost(
    HttpServletRequest request,
    HttpServletResponse response) throws ServletException, IOException
  {

		try {
	  	BufferedReader reader = 
	  			new BufferedReader(new InputStreamReader(request.getInputStream()));
	
	  	ArrayList<String> lines = new ArrayList<String>();
	  	String line;
	    while ((line = reader.readLine()) != null) {
	    	lines.add(line);
	    }
	    
	    // purge from headers and bottoms
	    process(lines);
	  	
	  	// response
	  	response.setContentType("text/html");
	    response.setStatus(HttpServletResponse.SC_OK); 
	    
	    String jsonResponse = new Gson().toJson(9);

	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().println(jsonResponse);
  	} catch (IOException e) {
  		throw new IllegalStateException(e);
  	}
  }
}
