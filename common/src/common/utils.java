package common;

import com.google.common.base.Joiner;
import com.google.common.collect.Multiset;
import com.google.common.io.Closer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 14.05.13
 * Time: 22:07
 * To change this template use File | Settings | File Templates.
 */
final public class utils {
  static public void list2file(List<String> list, String filename) throws IOException {
    Closer closer = Closer.create();
    try {
      closer.register(
        new BufferedWriter(
          new FileWriter(filename)))
        .write(Joiner.on("\n").join(list));
    } catch (Throwable e) {
      closer.rethrow(e);
    } finally {
      closer.close();
    }
  }

  static public String file2string(String filename) {
    try {
      Closer closer = Closer.create();
      try {
        BufferedReader in = closer.register(new BufferedReader(new FileReader(filename)));
        String s;
        StringBuffer buffer = new StringBuffer();
        while ((s = in.readLine()) != null) buffer.append(s);
        return buffer.toString();
      } catch (Throwable e) {
        closer.rethrow(e);
      } finally {
        closer.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;   // TODO(zaqwes): BAD!!
  }
  static public List<String> file2list(String filename) {
    List<String> result = new ArrayList<String>();
    try {
      Closer closer = Closer.create();
      try {
        BufferedReader in = closer.register(new BufferedReader(new FileReader(filename)));
        String s;
        while ((s = in.readLine()) != null) result.add(s);
        return result;
      } catch (Throwable e) {
        closer.rethrow(e);
      } finally {
        closer.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  static public void print(Object msg) {
    if (System.console() == null) {
      System.out.println(msg);
    } else {
      System.console().writer().println(msg);
    }
  }
}
