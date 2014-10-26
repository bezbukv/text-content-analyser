import third_party_tests.jython_callers.jsr.PyCaller;

import  java.util.ArrayList;
public class TestCallJy {

    public static void main(String[] args) {
	// write your code here

        PyCaller caller = new PyCaller();
        String fname = "scripts/test_caller.py";
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
