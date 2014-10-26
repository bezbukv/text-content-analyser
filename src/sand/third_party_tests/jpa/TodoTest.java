// Про базы данных
//  http://habrahabr.ru/post/193136/
package sand.third_party_tests.jpa;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

// Use singleton - local database
public class TodoTest {
  private static final String PERSISTENCE_UNIT_NAME = "todos";
  private static EntityManagerFactory factory;

  @Test
  public void testReadAll() {
    // http://stackoverflow.com/questions/17855882/is-the-eclipselink-entitymanagerfactory-thread-safe
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = factory.createEntityManager();

    // read the existing entries and write to console
    Query q = em.createQuery("select t from Todo t");
    List<Todo> todoList = q.getResultList();
    for (Todo todo : todoList) {
      System.out.println(todo);
    }
    System.out.println("Size: " + todoList.size());

    em.close();
  }

  @Test
  public void testAppend() {
    factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    EntityManager em = factory.createEntityManager();

    // create new todo
    em.getTransaction().begin();
    Todo todo = new Todo();
    todo.setSummary("This is a test");
    todo.setDescription("This is a test");
    em.persist(todo);
    em.getTransaction().commit();

    em.close();
  }
}
