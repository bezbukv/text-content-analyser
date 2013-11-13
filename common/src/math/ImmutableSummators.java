package math;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 10.05.13
 * Time: 19:33
 * To change this template use File | Settings | File Templates.
 */
final public class ImmutableSummators {

  public static int sumIntList(List<Integer> list) {
    int sum = 0;
    for (Integer value : list) {
      sum += value;
    }
    return sum;
  }

  public static double meanList(List<Integer> list) {
    return sumIntList(list)*1.0/list.size();
  }
}
