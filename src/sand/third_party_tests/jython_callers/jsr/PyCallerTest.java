/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sand.third_party_tests.jython_callers.jsr;

import org.junit.Test;

/**
 *
 * @author zaqwes
 */
public class PyCallerTest {
    /**
     * Test of run_py_function method, of class PyCaller.
     */
    @Test
    public void testPy_function() {
        System.out.println("py_function");

        // Исходные данные
        /*String fname = "scripts/for_test_caller.py";
        String function_name = "string_string";
        String arg = "10";
        PyCaller instance = new PyCaller();
        String expResult = "10";
        
        // Все нормально
        String result = "";
        try {
            result = instance.run_py_function(fname, function_name, arg);
            assertEquals(expResult, result);
        } catch (JSRException ex) {
            assertTrue(false);
        }

        // Не найден файл
        fname = "scripts_tmp/for_test_caller.py";
        try {
            result = instance.run_py_function(fname, function_name, arg);
        } catch (JSRException ex) {
            assertEquals(ex.getErrCode(), 2);
        }

        // Не нашлось имени функции
        fname = "scripts/for_test_caller.py";
        function_name = "unknown";
        try {
            result = instance.run_py_function(fname, function_name, arg);
        } catch (JSRException ex) {
            assertEquals(ex.getErrCode(), 1);
        }

        fname = "scripts/for_test_caller.py";
        function_name = "err_in_script";
        try {
            result = instance.run_py_function(fname, function_name, arg);
        } catch (JSRException ex) {
            ex.getStackTrace();
            assertEquals(ex.getErrCode(), 3);
        }     */
    }

    private void print(String msg) {
        System.out.println(msg);

    }
    
    /*@Test(expected = ArithmeticException.class)  
	public void divisionWithException() {  
	  int i = 1/0;
	}*/
}