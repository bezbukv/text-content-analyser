package jsr;

import javax.script.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
public class EvalFile {
    /* */
    public int runScriptNamed( String scriptName ) {
    	// create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create JavaScript engine
        ScriptEngine engine = factory.getEngineByName("jython");
        // evaluate JavaScript code from given file - specified by first argument
        try {
        	//System.out.println(System.getProperty("user.dir"));
        	//System.out.println( scriptName );
        	
        	// from ant no work
        	//engine.eval( "print \"Hello World from Jython\\n\"");
			engine.eval(new FileReader( scriptName ));
        }
        catch (ScriptException e) {
        	//System.out.println("eval() : an error occurrs in script.");
        	return 1;
        }
        catch (NullPointerException e) {
        	//System.out.println("eval() : the argument is null.");
        	return 2;
        }
        catch (FileNotFoundException e) {
        	//System.out.println("FileReader : script file no found");
        	return 3;
		}
        return 0;
    	
    }
}