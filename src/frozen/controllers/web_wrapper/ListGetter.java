package frozen.controllers.web_wrapper;

import com.google.common.collect.ImmutableList;

@Deprecated
public interface ListGetter {
  // Index: нужно для маркеровки
  // WordItemKind: само слово
  // Translates:
  // Context:
  ImmutableList<ImmutableList<ImmutableList<String>>> getPerWordData();
}
