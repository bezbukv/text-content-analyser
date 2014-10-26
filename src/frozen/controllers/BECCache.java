package frozen.controllers;

//import frozen.dal.accessors_text_file_storage.IdxAccessor;


// @Immutable
//
// TODO(zaqwes) TOTH: Вот словарь это индекс или фильтр?
@Deprecated
public class BECCache {
  /*
  public static Optional<BECCache> create() {
    Optional<BECCache> instance = Optional.absent();
    try {
      instance = Optional.of(new BECCache());
    } catch (CrosscuttingsException e) {
      Tools.log(e.getMessage());
    }
    return instance;
  }

  // Да, лучше передать, тогда будет Стратегией?
  private BECCache() throws CrosscuttingsException {
    String nodeName = "bec-node";
    final String splitter = GlobalConstants.PATH_SPLITTER;
    final String pathToAppFolder = GlobalConfigurator.getPathToAppFolder();

    final String pathToNode = Joiner.on(GlobalConstants.PATH_SPLITTER)
      .join(
        pathToAppFolder,
        nodeName);

    SENTENCES_PTRS = IdxAccessor.getSentencesKeys(
        Joiner.on(splitter).join(pathToNode, GlobalConstants.FILENAME_SENTENCES_IDX));
    IDX_SORTED = IdxAccessor.getSortedIdx(
        Joiner.on(splitter).join(pathToNode, GlobalConstants.SORTED_IDX_FILENAME));
    COUNT_WORDS = IDX_SORTED.get().size();
  }

  // Похоже тут violate не нужно.
  private final Optional<ImmutableList<String>> IDX_SORTED;
  private final Optional<ImmutableMap<String, List<Integer>>> SENTENCES_PTRS;
  private final Integer COUNT_WORDS;

  // Получить слово по индексу. Нужно для генератора случайных чисел.
  // 0 - CountWords
  //
  // Если использовать Optional, то нужно где-то все равно переходить к исключениям.
  //
  // Наверное нужно кинуть непроверяемое!! Выход за границы - нарушение контракта
  //   и это стерильная зона данных (числа идут от генератора ПСП),
  //   а вот при записи... и если данные идут из Web
  //
  public String getWordByIdx(Integer idx) throws VParserException {
    // Проверяем границы.
    // Вычитать 1 нужно, так как нумерация с нуля.
    if (!(idx < 0 || idx > COUNT_WORDS-1)) {
      return IDX_SORTED.get().get(idx);
    } else {
      throw new VParserException("Out of range.");
    }
  }

  //private List<Integer>
  //public List<String> getAssociatedContent(String key) {

  //}

  public Integer getSizeIndexes() {
    return COUNT_WORDS;
  }  */
}
