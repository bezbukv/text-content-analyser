package jsr;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import jsr.EvalFile;

import org.junit.Test;

public class EvalFileTest {
	@Test
	public void testRunScriptNamed() {
		//fail("Not yet implemented");
		String scriptName = "TestScript.py";
		EvalFile evalFile = new EvalFile();
		evalFile.runScriptNamed( scriptName );
	}
	// Write a test method (annotated with @Test) 
	// that asserts expected results on the object under test:
	@Test
	public void testEmptyCollection() {
		Collection<?> collection = new ArrayList<Object>();
		assertTrue(collection.isEmpty());
        //assertTrue(false);
	}

	/// Runner()
	// If you are running your JUnit 4 tests with a JUnit 3.x runner, 
	// write a suite() method that uses the JUnit4TestAdapter 
	// class to create a suite containing all of your test methods:
	/*public static junit.framework.Test suite() {
		return new junit.framework.JUnit4TestAdapter(EvalFileTest.class);
	}       */
	// Although writing a main() method to run the test is much less important 
	// with the advent of IDE runners, it's still possible:
	/*public static void main(String args[]) {
		org.junit.runner.JUnitCore.runClasses(PyCallerTest.class);//.main("jsr.*");
        org.junit.runner.JUnitCore.runClasses(EvalFileTest.class);//.main("jsr.*");
	}  */

}
