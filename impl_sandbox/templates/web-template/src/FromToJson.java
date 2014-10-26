import com.google.gson.Gson;
import java.util.*;

public class FromToJson {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		Collection<Integer> ints = new ArrayList<Integer>();
		ints.add(9);
		ints.add(92);
		String json = gson.toJson(ints);
		
		System.out.println(json);
	}

}
