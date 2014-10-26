package sand.third_party_tests.gson;

import com.google.gson.Gson;
import org.junit.Test;

import java.util.*;

public class UseGsonTest {

  @Test
	public void testMain() {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		Collection<Integer> ints = new ArrayList<Integer>();
		ints.add(9);
		ints.add(92);
		String json = gson.toJson(ints);
		System.out.println(json);
	}
}
