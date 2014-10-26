package sand.third_party_tests.gae;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.cmd.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static sand.third_party_tests.gae.OfyService.ofy;

/**
 * Created by zaqwes on 5/7/14.
 */
public class CarOneToOneTest {
  private static final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  static int RED = 90;
  static int BLUE = 89;

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testWithParent() {
    // https://code.google.com/p/objectify-appengine/wiki/Introduction
    // Persons
    Person activeUser = Person.create("zaqwes");
    Person defaultUser = Person.create("default");
    ofy().save().entities(activeUser, defaultUser).now();

    // Edit
    Engine engine = new Engine("Honda");
    ofy().save().entity(engine).now();

    // Obj graph
    CarOneToOne porsche = CarOneToOne.create("2FAST", RED, defaultUser, engine);
    CarOneToOne porsche2 = CarOneToOne.create("2FAST", RED, defaultUser, engine);
    ofy().save().entities(porsche, porsche2).now();    // async without the now()

    // Read
    Person person = ofy().load().type(Person.class).filter("name", "default").first().now();
    assertEquals(person.name, "default");

    // With parent
    Query<CarOneToOne> carsQuery = ofy().load().type(CarOneToOne.class).ancestor(person);
    assertEquals(2, carsQuery.count());
  }

  @Test
  public void testCoupling() {
    // https://code.google.com/p/objectify-appengine/wiki/Introduction
    // Fill
    CarOneToOne porsche = new CarOneToOne("2FAST", RED);
    ofy().save().entity(porsche).now();    // async without the now()

    Engine engine = new Engine("Honda");
    // TODO: Need to save? Yes. Throw exception.
    ofy().save().entity(engine).now();

    // Edit
    porsche.setEngine(engine);
    ofy().save().entity(porsche).now();

    // Read
    //Iterable<Key<CarOneToOne>> allKeys = ofy().load().type(CarOneToOne.class).keys();
    //Query<CarOneToOne>
    CarOneToOne car = ofy().load().type(CarOneToOne.class).filter("vin", "2FAST").first().now();
    assertEquals(car.getEngine().name, "Honda");
  }
}
