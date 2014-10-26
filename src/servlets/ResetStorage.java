package servlets;

import gae_store_space.AppInstance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResetStorage extends HttpServlet {
  private static final long serialVersionUID = -658795213490961099L;
  private AppInstance app = AppInstance.getInstance();
  
  @Override
  public void doGet(
    HttpServletRequest request,
    HttpServletResponse response) throws ServletException, IOException
  {

    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);
    
    app.resetFullStore();
    
    //List<PageSummaryValue> v = app.getUserInformation(OnePageProcessor.defaultUserId);
    //String json = new Gson().toJson(v);

    //response.setCharacterEncoding("UTF-8");
    //response.getWriter().println(json);
  }
}
