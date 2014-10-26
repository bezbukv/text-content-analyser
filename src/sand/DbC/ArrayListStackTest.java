package sand.DbC;

import org.junit.Test;

// On cofoja in IDEA http://java.dzone.com/articles/using-google-contracts-java
// http://habrahabr.ru/post/139206/ - нужно почитать, не запустил сходу
//
// TODO: узнать про annotation processing!
//
// еще реализация DbC
// http://docs.oracle.com/javase/6/docs/technotes/guides/language/assert.html#usage-conditions
//
// http://youtrack.jetbrains.com/issue/IDEA-117888

public class ArrayListStackTest {
  @Test
  public void testPop() throws Exception {
    Stack<Integer> stack = new ArrayListStack<Integer>();
    stack.pop();
  }
}
