package jsr;

import javax.script.*;
public class EvalScript {
    public static void main(String[] args) throws ScriptException {
		// create a script engine manager
        ScriptEngineManager manager =
            new ScriptEngineManager();

        //Printing all available scripting languages
        for (ScriptEngineFactory factory :
            manager.getEngineFactories()) {
            System.out.println("Available language: " +
                factory.getLanguageName());
        }

        // create a Jython engine
        ScriptEngine jythonEngine =
            manager.getEngineByName("python");
        // evaluate Jython code from String
        jythonEngine.eval(
            "print \"Hello World from Jython\\n\"");
        /*ScriptEngine engine = new ScriptEngineManager().getEngineByName("python");

        // Using the eval() method on the engine causes a direct
        // interpretataion and execution of the code string passed into it
        engine.eval("import sys");
        engine.eval("print sys");

        // Using the put() method allows one to place values into
        // specified variables within the engine
        engine.put("a", "42");

        // As you can see, once the variable has been set with
        // a value by using the put() method, we an issue eval statements
        // to use it.
        engine.eval("print a");
        engine.eval("x = 2 + 2");

        // Using the get() method allows one to obtain the value
        // of a specified variable from the engine instance
        Object x = engine.get("x");
        System.out.println("x: " + x);*/
    }
}