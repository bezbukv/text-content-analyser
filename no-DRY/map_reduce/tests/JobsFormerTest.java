
import common.ImmutableAppUtils;
import jobs_processors.ImmutableJobsFormer;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 10.05.13
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public class JobsFormerTest {

  @Test
  public void testDevelop() {
    ImmutableAppUtils.print(ImmutableJobsFormer.getJobs());

  }
}
