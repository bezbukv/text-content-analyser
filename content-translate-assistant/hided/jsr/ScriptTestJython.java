package jsr;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptTestJython {
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
      //fail("Not yet implemented");
      		String scriptName = "TestScript.py";
      		EvalFile evalFile = new EvalFile();
      		evalFile.runScriptNamed( scriptName );
    }
}