import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
 
import java.io.IOException;

//import business.originator_frequency_index.IIndexCursor;
//import business.originator_frequency_index.IndexCursorFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.ServletHandler;

//import java.util.
// App
//import business.IndexContainer;

//import com.google.gson.Gson;
import java.util.*;
import java.util.Random;


public class AppContainer {
  public static void main(String[] args) throws Exception {
    // TODO(zaqwes): Сделать через конфигурационные файлы. Можно ли и нужно ли?
    System.out.println("Working Directory = " +
        System.getProperty("user.dir"));

    Server server = new Server();
    SelectChannelConnector connector = new SelectChannelConnector();
    connector.setPort(8080);
    server.addConnector(connector);
 
    // Подключаем корень?
    ResourceHandler resource_handler = new ResourceHandler();
    resource_handler.setDirectoriesListed(true);
    resource_handler.setWelcomeFiles(new String[]{ "index.html" });
    resource_handler.setResourceBase("./src/web_view");
 
    // Список обработчиков?
    HandlerList handlers = new HandlerList();
    ServletHandler handler = new ServletHandler();
    handlers.setHandlers(new Handler[] { resource_handler/*, new DefaultHandler()*/, handler });
    // ! если не находи index.html Открывает вид папки!!
    
    // Подключаем к серверу
    server.setHandler(handlers);

    // Регистрируем сервлет?
    handler.addServletWithMapping("AppContainer$App", "/app");
 
    server.start();
    server.join();
  }
  
  // Servlets
  public static class App extends HttpServlet {
    /* Блок классов приложения? Должны быть потокозащищенными
    private  */
    //private  IIndexCursor ptr;

    public void init() throws ServletException {
      //IndexCursorFactory factory = new IndexCursorFactory();

      //String indexRoot = "src/indexes";
      //ptr = factory.create(indexRoot);



      // Получаем индекс
      //System.out.print();

    }

    //IndexContainer indexContainer = new IndexContainer();

    /*private String getIndex() {
      Gson gson = new Gson();
      //System.out.print(ptr.getListNodes());

      // Подключаемся к ветке
      String contentItemName = "Iron Man AA";

      ptr.assignBranch(contentItemName);

      String result = gson.toJson(ptr.getSortedForwardIdx());
      return  result;
    }

    private String getListNodes() {
      Gson gson = new Gson();
      List<String> noPackedResponse = ptr.getListNodes();
      return gson.toJson(noPackedResponse);
    }

    private String _getOxOy() {
      Gson gson = new Gson();

      ArrayList<ArrayList<Integer>> ints = new ArrayList<ArrayList<Integer>>(10000);

      Random randomGenerator = new Random();
      for (Integer idx = 0; idx < 700; idx += 1){
        ArrayList<Integer> tmp = new  ArrayList<Integer>();
        int randomInt = randomGenerator.nextInt(100);
        tmp.add(idx);  // Ox sample
        tmp.add(randomInt);  // Oy sample
        ints.add(tmp);
      }

      // Jython test
      //indexContainer.testCall();

      // Сереализуем
      String json_response = gson.toJson(ints);
      return  json_response;
    } */

    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
      
      //

      // Проверить бы на наличие
      String name_requester = request.getParameter("name");

      response.setContentType("text/html");
      response.setStatus(HttpServletResponse.SC_OK);

      // Можно сделат table call map   map<string, (?)аналог_указателя_на_функции_в_си>
      //
      // Вариант 1:
      // в java 6 нет лямбд и замыканий(?)
      // кажется нужна будет обертка для класса и интерфейс - как соединить все вместе?
      //
      // Вариант 2:
      // reflection - slow
      //
      // Вариант N:
      // есть еще какие-то альтарнативы
      //
      // Плохой вариант - линейный поиск - свич по именам
      //
      // И как быть с потокозащитой хэша? Кстати доступ только на чтение
      String json_response = "";
      if (name_requester.equals("get_axis")) {
        json_response = "get_axis";//getIndex();
      } else if (name_requester.equals("get_nodes")) {
        json_response = "get_nodes";//getListNodes();
      } else {
        // No implemented
      }

  		
      response.getWriter().println(json_response);
    }
  }
}