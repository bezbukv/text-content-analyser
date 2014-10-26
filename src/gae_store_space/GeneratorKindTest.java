package gae_store_space;

import core.math.DistributionElement;
import core.math.GeneratorDistributionException;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static gae_store_space.OfyService.ofy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class GeneratorKindTest {
  private static final LocalServiceTestHelper helper =
    new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() { helper.setUp(); }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  GeneratorKind build(ArrayList<Integer> distribution) {

    ArrayList<DistributionElement> real = new ArrayList<DistributionElement>();

    for (Integer value: distribution) {
      real.add(new DistributionElement(value, true));
    }
    
    return GeneratorKind.create(real);
  }

  @Test
  public void testCreate() throws GeneratorDistributionException {
    ArrayList<Integer> distribution = new ArrayList<Integer>(Arrays.asList(1, 6, 0, 14, 5, 7));

    GeneratorKind g = build(distribution);
    
    assertNotNull(g);  // иногда падает

    List<Integer> experiment = new ArrayList<Integer>();
    for (int i = 0; i < 10000; ++i) {
      experiment.add(g.getPosition());
    }

    assertFalse(experiment.contains(new Integer(distribution.indexOf(0))));
  }

  @Test
  public void testPersist() {
    {
      ArrayList<Integer> distribution = new ArrayList<Integer>(Arrays.asList(1, 6, 0, 14, 5, 7));
      GeneratorKind d = build(distribution);

      ofy().save().entity(d).now();

      d.disablePoint(0);

      ofy().save().entity(d).now();
    }

    // try load
    {
      GeneratorKind page = ofy().load().type(GeneratorKind.class).id(1).now();
      Integer action = page.codeAction;
    }
  }
}
