package sand.DbC;

import com.google.java.contract.Ensures;
import com.google.java.contract.Invariant;

import java.util.ArrayList;

@Invariant("elements != null")
public class ArrayListStack<T> implements Stack<T> {
  protected ArrayList<T> elements;

  public ArrayListStack() {
    elements = new ArrayList<T>();
  }

  public int size() {
    return elements.size();
  }

  public T peek() {
    return elements.get(elements.size() - 1);
  }

  public T pop() {
    return elements.remove(elements.size() - 1);
  }

  @Ensures("elements.contains(old(obj))")
  public void push(T obj) {
    elements.add(obj);
  }
}