package sand.third_party_tests.jython_callers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 16.03.13
 * Time: 12:50
 * To change this template use File | Settings | File Templates.
 */
public class JavaImpl implements IJythonTestInterface {
    public int getIdx() {
        return 42;
    }

    public  List<String> getList() {
        List<String> result = new ArrayList<String>();
        result.add("Hello");
        return result;
    }

    public void setList(List<String> stringList) {
        stringList.add("Hello form java");
        System.out.println(stringList);
    }
}
