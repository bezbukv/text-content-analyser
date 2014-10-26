package frozen;

import com.google.gson.Gson;
import common.Tools;
import frozen.dal.accessors_text_file_storage.ImmutableBaseCoursor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ServletWebRelay extends HttpServlet {
  /* Блок классов приложения? Должны быть потокозащищенными
  private  */
  //private  IndexCursor ptr;

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
    List<String> listNodes = ImmutableBaseCoursor.getListNodes();

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
      json_response = "get_axis";
      String node = request.getParameter("node_name");
    } else if (name_requester.equals("get_nodes")) {
      Gson gson = new Gson();
      json_response = gson.toJson(listNodes);
      Tools.print(json_response);
    } else {
      // No implemented
    }

    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(json_response);
  }
}