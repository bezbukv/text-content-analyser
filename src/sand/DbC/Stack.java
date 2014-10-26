package sand.DbC;

// https://code.google.com/p/cofoja/wiki/AddContracts

import com.google.java.contract.Ensures;
import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;

@Invariant("size() >= 0")  // step 2
interface Stack<T> {
  public int size();  // step 1

  @Requires("size() >= 1")  // step 5
  public T peek();  // step 5


  @Requires("size() >= 1")  // step 3
  //@Ensures("size() == old(size()) - 1")  // step 4
  @Ensures({  // step 6
    "size() == old(size()) - 1",
    "result == old(peek())"
  })
  public T pop();

  //@Ensures("size() == old(size()) + 1")  // step 4
  @Ensures({  // step 6
    "size() == old(size()) + 1",
    "peek() == old(obj)"
  })
  public void push(T obj);
}