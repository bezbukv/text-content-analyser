package sand.third_party_tests.servlets;


import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

  public class MinimalServlets
  {
      public static void main(String[] args)
          throws Exception
      {
          Server server = new Server();
          Connector connector = new SocketConnector();
          //connector.setPort(8080);   // Not work.
          server.setConnectors(new Connector[]{connector});
          
          ServletHandler handler = new ServletHandler();
          server.setHandler(handler);
          
          handler.addServletWithMapping("MinimalServlets$HelloServlet", "/");
          handler.addServletWithMapping("MinimalServlets$HelloServletAjax", "/ajax");
          
          server.start();
          server.join();
      }
  
      public static class HelloServlet extends HttpServlet
      {
          protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
          {
              response.setContentType("text/html");
              response.setStatus(HttpServletResponse.SC_OK);
              response.getWriter().println("<h1>Hello SimpleServlet</h1>");
          }
     }
      
      public static class HelloServletAjax extends HttpServlet
      {
          protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
          {
              response.setContentType("text/html");
              response.setStatus(HttpServletResponse.SC_OK);
              response.getWriter().println("<h1>Ajax</h1>");
          }
     }
  }