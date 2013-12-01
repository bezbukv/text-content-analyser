package common;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 09.05.13
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
final public class ImmutableAppUtils {
  public static List<String> getListNamesMetaFiles(String pathToNode, String regex) {
    File nodeContainer = new File(pathToNode);
    List<String> result = Arrays.asList(nodeContainer.list(new DirFilter(regex)));
    return result;
  }

  public static void print(Object msg) {
    if (System.console() == null) {
      System.out.println(msg);
    } else {
      System.console().writer().println(msg);
    }
  }

  public static List<String> getListFilenamesByExtention(String path, String regex) {
    List<String> result = Arrays.asList(new File(path).list(new DirFilter(regex)));
    return result;
  }
}

final class DirFilter implements FilenameFilter {
  private Pattern pattern;
  public DirFilter(String regex) {
    pattern = Pattern.compile(regex);
  }
  public boolean accept(File dir, String name) {
    return  pattern.matcher(name).matches();
  }
}
