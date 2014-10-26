package frozen.dal.accessors_text_file_storage;

import common.Tools;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 08.06.13
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public final class ImmutableBaseFilter {
  private static final Set<String> BASE_FILTER;

  static {
    String pathToFilterFile = "C:/Users/кей/Dropbox/rpts/onto/key-words-base-filter.txt";
    BASE_FILTER = new HashSet<String>(Tools.file2list(pathToFilterFile));
  }

  static boolean isContentStem(String stem) {
    return BASE_FILTER.contains(stem);
  }

  static public void main(String[] args) {

  }
}
