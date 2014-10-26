package sand.third_party_tests.jython_callers.jsr;

/**
 *
 */



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
public class Trash {

    public void printListEngines() {
        ScriptEngineManager manager = new ScriptEngineManager();
        List<ScriptEngineFactory> engines = manager.getEngineFactories();
        if (engines.isEmpty()) {
            System.out.println("No scripting engines were found");
            return;
        }
        System.out.println("The following " + engines.size() +
                " scripting engines were found");
        System.out.println();
        for (ScriptEngineFactory engine : engines) {
            System.out.println("Engine name: " + engine.getEngineName());
            System.out.println("\tVersion: " + engine.getEngineVersion());
            System.out.println("\tLanguage: " + engine.getLanguageName());
            List<String> extensions = engine.getExtensions();
            if (extensions.size() > 0) {
                System.out.println("\tEngine supports the following extensions:");
                for (String e : extensions) {
                    System.out.println("\t\t" + e);
                }
            }
            List<String> shortNames = engine.getNames();
            if (shortNames.size() > 0) {
                System.out.println("\tEngine has the following short names:");
                for (String n : engine.getNames()) {
                    System.out.println("\t\t" + n);
                }
            }
            System.out.println("=========================");
        }

    }

    public void runTRK() {
        ScriptEngineManager manager = new ScriptEngineManager();
        File scriptsDir = new File("scripts");
        File[] scripts = scriptsDir.listFiles(new TPKFilter());
        for (File f : scripts) {
            String fileName = f.getName();
            String ext = fileName.substring(fileName.lastIndexOf(".")+1);

            //
            ScriptEngine engine = manager.getEngineByExtension(ext);
            if (engine != null) {
                try {
                    ScriptEngineFactory factory = engine.getFactory();
                    System.out.println(
                            "Running " + fileName + " using engine " +
                                    factory.getEngineName() + " Version " +
                                    factory.getEngineVersion() + " for language " +
                                    factory.getLanguageName());

                    engine.eval(new FileReader(f));
                }
                catch (FileNotFoundException ex) {
                    Logger.getLogger(PyCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (ScriptException ex) {
                    Logger.getLogger(PyCaller.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally {
                    System.out.println("============================");
                }
            }
            else {
                System.err.println("Could not find scripting engine for " + f.getName());
            }
        }
    }

    public void runFunctionFromPyScriptNoArgs() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("jython");
        try {

            engine.eval(new FileReader("scripts/TPK.py"));

            Invocable inv = (Invocable)engine;
            try {
                System.out.println("Invoking hello");
                inv.invokeFunction("hello");
                System.out.println("Invoking goodbye");
                inv.invokeFunction("goodbye");
            }
            catch (NoSuchMethodException ex) {
                Logger.getLogger(PyCaller.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(PyCaller.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ScriptException ex) {
            Logger.getLogger(PyCaller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
class TPKFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String name) {
        return name.startsWith("TPK.");
    }
}


