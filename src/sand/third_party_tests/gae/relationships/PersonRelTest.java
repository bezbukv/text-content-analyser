package sand.third_party_tests.gae.relationships;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static sand.third_party_tests.gae.relationships.OfyService.ofy;

/**
 * Created by zaqwes on 5/9/14.
 */
public class PersonRelTest {
  private static final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testOneToOne() {
    PersonRel wife = new PersonRel();
    wife.name = "July";
    ofy().save().entity(wife).now();
    assertNotNull(wife.id);

    PersonRel me = new PersonRel();
    me.name = "bob";
    ofy().save().entity(me).now();
    assertNotNull(me.id);

    me.significantOther = Key.create(wife);
    ofy().save().entity(me).now();

    // Read
    PersonRel bob = ofy().load().type(PersonRel.class).filter("name", "bob").first().now();
    PersonRel bobswife = ofy().load().key(bob.significantOther).now();

  }
}
