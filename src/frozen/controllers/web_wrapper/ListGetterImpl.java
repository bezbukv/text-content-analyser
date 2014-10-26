package frozen.controllers.web_wrapper;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import frozen.dal.accessors_text_file_storage.ImmutableNodeAccessor;
import gae_store_space.GeneratorKind;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ListGetterImpl implements ListGetter {
  private final ImmutableNodeAccessor ACCESSOR_;
  private final Optional<GeneratorKind> GENERATOR_;
  public ListGetterImpl(ImmutableNodeAccessor accessor) {
    ACCESSOR_ = accessor;
    ArrayList<Integer> distribution = ACCESSOR_.getDistribution();
    GENERATOR_ = Optional.absent();//ActiveDistributionGenKind.create(distribution);
  }

  // Index: нужно для маркеровки
  // WordItemKind: само слово
  // Translates:
  // Context:
  @Override
  public ImmutableList<ImmutableList<ImmutableList<String>>> getPerWordData() {
    List<String> rawKeys = new ArrayList<String>();
    List<ImmutableList<String>> values = new ArrayList<ImmutableList<String>>();

    // Отправляем только если есть контекст
    while (true) {
      boolean finded = false;
      Integer currentKey = GENERATOR_.get().getPosition();

      // Добавляем, только если что-то есть
      ImmutableList<String> content = ACCESSOR_.getContent(currentKey);
      if (!content.isEmpty()) {
        rawKeys.add("content");
        values.add(content);
        finded = true;
      }

      if (finded) {
        ImmutableList<String> translate = ImmutableList.of();
        if (!translate.isEmpty()) {
          rawKeys.add("translate");
          values.add(translate);
        }

        // Обязательно!
        rawKeys.add("word");
        values.add(ImmutableList.of(ACCESSOR_.getWord(currentKey)));
        break;
      }
    }
    List<ImmutableList<String>> keys = new ArrayList<ImmutableList<String>>();
    keys.add(ImmutableList.copyOf(rawKeys));
    return ImmutableList.of(ImmutableList.copyOf(keys), ImmutableList.copyOf(values));
  }
}