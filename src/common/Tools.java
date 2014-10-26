package common;

import com.google.appengine.repackaged.org.apache.http.annotation.Immutable;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Closer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Immutable
final public class Tools {
  private Tools() {}

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
        StringBuilder buffer = new StringBuilder();
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

  static public void listToFile(List<String> list, String filename) throws IOException {
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

  static public Optional<String> fileToString(String filename) throws IOException {
    Closer closer = Closer.create();
    try {
      BufferedReader in = closer.register(new BufferedReader(new FileReader(filename)));
      String s;
      StringBuilder buffer = new StringBuilder();
      while ((s = in.readLine()) != null) buffer.append(s);
      return Optional.of(buffer.toString());
    } catch (Throwable e) {
      closer.rethrow(e);
    } finally {
      closer.close();
    }
    return Optional.absent();
  }

  static public ImmutableList<String> fileToList(String filename) throws IOException {
    Closer closer = Closer.create();
    List<String> result = new ArrayList<String>();
    try {
      // TODO(zaqwes): Может лучше считать разом, а потом разбить на части?
      BufferedReader in = closer.register(new BufferedReader(new FileReader(filename)));
      String s;
      while ((s = in.readLine()) != null) result.add(s);
    } catch (Throwable e) {
      closer.rethrow(e);
    } finally {
      closer.close();
    }
    return ImmutableList.copyOf(result);
  }

  static public void print(Object msg) {
    if (System.console() == null) {
      System.out.println(msg);
    } else {
      System.console().writer().println(msg);
    }
  }

  private final static  class DirFilter implements FilenameFilter {
    private Pattern pattern;
    public DirFilter(String regex) {
      pattern = Pattern.compile(regex);
    }
    public boolean accept(File dir, String name) {
      return  pattern.matcher(name).matches();
    }
  }
  public static List<String> getListNamesMetaFiles(String pathToNode, String regex) {
    File nodeContainer = new File(pathToNode);
    return Arrays.asList(nodeContainer.list(new DirFilter(regex)));
  }

  public static List<String> getListFilenamesByExtention(String path, String regex) {
    return Arrays.asList(new File(path).list(new DirFilter(regex)));
  }
}
