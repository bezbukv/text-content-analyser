/**
 * 
 */
package jsr;


import jsr.JSRException;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
 

/**
 *
 * @author ptremblett
 * 
 * @thinks 
 *   Пока python-объектов не касается
 */
public class PyCaller {
    /**
     *
     * @param engineName что за интерпр. используем
     *
     * */
    private ScriptEngine initScriptEngine(String engineName) { // throws JSRException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(TYPE_ENGINE);
        return  engine;
    }
    
    String TYPE_ENGINE = "jython";

    // Клиент сам знает что должен получить
    public Object py_call_with_return(String fname,
                                      String function_name,
                                      String arg) {

        // Загрузка движка
        ScriptEngine engine = initScriptEngine(TYPE_ENGINE);

        // Сам вызов
        Object result = "";
        try {
            engine.eval(new FileReader(fname));
            Invocable inv = (Invocable) engine;
            try {
                result = inv.invokeFunction(function_name, arg);
            }
            catch (NoSuchMethodException ex) {
                System.out.println("No such method in module");
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println("No found script file");
        }
        catch (ScriptException ex) {
            System.out.println("Error in script");
        }
        return result;
    }
    
    public String run_py_str_function_str(
        String fname,
        String function_name,
        String arg) throws JSRException {
    /**
     * Запуск python-функции формата
     * String function_name(String)
     * @param  fname имя файла скрипта
     * @param  function_name имя вызываемой функции python-модуля
     * @param  arg строковый аргумент
     * @return
     * Результат в формате строки
     */
      return (String)py_call_with_return(fname, function_name, arg);
    }
}


