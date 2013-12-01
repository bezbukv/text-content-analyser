package business.originator_frequency_index;

import business.originator_frequency_index.IIndexCursor;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

public class IndexCursorFactory {

  private PyObject buildingClass;

  /**
   * Create a new PythonInterpreter object, then use it to
   * execute some python code. In this case, we want to
   * import the python module that we will coerce.
   *
   * Once the module is imported than we obtain a reference to
   * it and assign the reference to a Java variable
   */

  public IndexCursorFactory() {
    PythonInterpreter interpreter = new PythonInterpreter();
    interpreter.exec("import sys");
    interpreter.exec("sys.path.append('.');");
    interpreter.exec("from jysrc.business.originator_frequency_index.IndexCursor import IndexCursor");
    buildingClass = interpreter.get("IndexCursor");
  }

  /**
   * The create method is responsible for performing the actual
   * coercion of the referenced python module into Java bytecode
   */

  public IIndexCursor create(String indexRoot) {

    PyObject buildingObject = buildingClass.__call__(new PyString(indexRoot));
    return (IIndexCursor)buildingObject.__tojava__(IIndexCursor.class);
  }
}