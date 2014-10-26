package frozen.dal.accessors_text_file_storage;

import com.google.common.collect.ImmutableList;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 01.09.13
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
public interface ContentHolder {
  ImmutableList<String> getContentItem(String word);
}
