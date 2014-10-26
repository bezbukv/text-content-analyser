package frozen.controllers.web_wrapper;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ListGetterFake implements ListGetter {
  @Override
  public ImmutableList<ImmutableList<ImmutableList<String>>> getPerWordData() {
    List<String> rawKeys = new ArrayList<String>();
    List<ImmutableList<String>> values = new ArrayList<ImmutableList<String>>();

    // Добавляем, только если что-то есть
    rawKeys.add("content");
    values.add(ImmutableList.of("word of", "words things"));

    rawKeys.add("translate");
    values.add(ImmutableList.of("слово"));

    // Обазятельно!
    rawKeys.add("word");
    values.add(ImmutableList.of("word"));

    List<ImmutableList<String>> keys = new ArrayList<ImmutableList<String>>();
    keys.add(ImmutableList.copyOf(rawKeys));

    return ImmutableList.of(ImmutableList.copyOf(keys), ImmutableList.copyOf(values));
  }
}
