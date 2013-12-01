package business;

import jsr.PyCaller;

import  java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 30.03.13
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
public class IndexContainer {
  public void testCall() {
    PyCaller caller = new PyCaller();
    String fname = "src/scripts/test_caller.py";
    String function_name = "get_string";
    String arg = "None";

    String result = (String)caller.py_call_with_return(fname, function_name, arg);
    System.out.println(result);

    function_name = "get_list";
    ArrayList resultlist = (ArrayList)caller.py_call_with_return(fname, function_name, arg);
    resultlist.add("Java string");
    System.out.println(resultlist);
  }
}
