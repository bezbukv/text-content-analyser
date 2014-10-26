package frozen.spiders_extractors;

//import frozen.dal.accessors_text_file_storage.VParserException;


// Наверное случайный выборщик будет оборачивать это класс, а не будет встроенным в него.
// Если его использовать статическим, то его нельзя будет передать!
//
// Option - что-то вроде альтернативы обработки ошибок, но она не гибкая, не дает никакой информации.
//   зато дает ответ - Да(результат)/Нет(ничего)
public final class ImmutableBECSave {
  /*
  // Thread-safe singleton. Trouble - how send data to constructor?
  public static final Optional<ImmutableBECParser> getDefaultCashInstance () {
    return BECParserHolder.INSTANCE;
  }

  static private class BECParserHolder {
    public static final Optional<ImmutableBECParser> INSTANCE;
    static {
      // Overhead! Объект создается всегда.
      final String PATH = "statistic-data";
      final String FILENAME = "vocabularity-folded.txt";
      final String FULL_FILENAME = Joiner.on(GlobalConstants.PATH_SPLITTER).join(PATH, FILENAME);
      // No lazily initializing!
      Optional<ImmutableBECParser> tmp;
      try {
        tmp = Optional.of(new ImmutableBECParser(FULL_FILENAME));
      } catch (IOException e) {
        // Не сконструирован. Использовать нельзя. Ноль, не ноль не важно.
        //   Не совсем ясно что с полями.
        Utils.print(e.getMessage());
        tmp = Optional.absent();
      }
      INSTANCE = tmp;
    }
  } */

  // TODO(zaqwes) TOTH: Кажется синхронизация не нужна.
  /*
  public static ImmutableBECSave create(ImmutableList<String> fileContent) {
    return new ImmutableBECSave(fileContent);
  }


  // Возвращает экземпляр парсера по заданному имени файла BEC. Если одного не достаточно.
  //
  // Излишняя потокозащита. Она нужна только для доступа к файлу. Хотя это общая защита при создании
  //   и возможно защиты только непосредственного доступа к файлу недостаточно.
  public static synchronized final Optional<ImmutableBECParser> getInstance(String fullFilename) {
    // No lazily initializing!
    Optional<ImmutableBECParser> tmp;
    try {
      tmp = Optional.of(new ImmutableBECParser(fullFilename));
    } catch (IOException e) {
      // Не сконструирован. Использовать нельзя. Ноль, не ноль не важно.
      //   Не совсем ясно что с полями.
      Utils.print(e.getMessage());
      tmp = Optional.absent();
    }
    return tmp;
  } */

  /*
  // No enough qualification. Too difficult.
  private ImmutableBECParser(String fullFilename) throws IOException {
    ImmutableList<String> content = Utils.file2list(fullFilename);

    CONTENT = ImmutableList.copyOf(content);

    List<String> cashWords = new ArrayList<String>();
    Multimap<String, String> cashTranslate = HashMultimap.create();
    Multimap<String, String> cashContent = HashMultimap.create();

    for (final String record: CONTENT) {
      List<String> parsedLine = Lists.newArrayList(Splitter.on(SPLITTER).split(record));
      if (!parsedLine.isEmpty()) {
        String word = parsedLine.get(KEY_POS);
        cashWords.add(word);
        cashTranslate.put(word, FAKE_TRANSLATE);
        cashContent.putAll(word, parsedLine.subList(KEY_POS, parsedLine.size()));
      }
    }

    WORDS_TRANSLATES = ImmutableMultimap.copyOf(cashTranslate);
    SORTED_WORDS_ALPH = ImmutableList.copyOf(cashWords);
    WORDS_CONTENT = ImmutableMultimap.copyOf(cashContent);
    COUNT_WORDS = SORTED_WORDS_ALPH.size();
  }   */

  // Данные для парсера передаем извне. Чтение внутри конструктора сомнительно очень.
  /*private ImmutableBECSave(ImmutableList<String> fileContent) {

    // Какие исключения могут генерировать списки, мапы,...
    CONTENT = ImmutableList.copyOf(fileContent);

    List<String> cashWords = new ArrayList<String>();
    Multimap<String, String> cashTranslate = HashMultimap.create();
    Multimap<String, String> cashContent = HashMultimap.create();

    for (final String record: CONTENT) {
      List<String> parsedLine = Lists.newArrayList(Splitter.on(SPLITTER).split(record));
      if (!parsedLine.isEmpty()) {
        String word = parsedLine.get(KEY_POS);
        cashWords.add(word);
        cashTranslate.put(word, FAKE_TRANSLATE);
        cashContent.putAll(word, parsedLine.subList(KEY_POS, parsedLine.size()));
      }
    }

    WORDS_TRANSLATES = ImmutableMultimap.copyOf(cashTranslate);
    SORTED_WORDS_ALPH = ImmutableList.copyOf(cashWords);
    WORDS_CONTENT = ImmutableMultimap.copyOf(cashContent);
    COUNT_WORDS = SORTED_WORDS_ALPH.size();
  }

  // Получить слово по индексу. Нужно для генератора случайных чисел.
  // 0 - CountWords
  //
  // Если использовать Optional, то нужно где-то все равно переходить к исключениям.
  public String getWordByIdx(Integer idx) throws VParserException {
    // Проверяем границы.
    // Вычитать 1 нужно, так как нумерация с нуля.
    if (!(idx < 0 || idx > COUNT_WORDS-1)) {
      return SORTED_WORDS_ALPH.get(idx);
    } else {
      throw new VParserException("Out of range.");
    }
  }

  // Похоже конструкторы параметры не передать. Поэтому путь жесткая константа.
  //private static Optional<ImmutableBECParser> instance = Optional.absent();

  private final Integer KEY_POS = 0;
  private final String SPLITTER = "@";
  private final String FAKE_TRANSLATE = "No";

  // Допустим испльзуем не исключение при чтении файла, возвращаем пустой список.
  // Если коллекция пустая, то возможно было пустой файл, а так сразу ясно, что что-то не так
  //   но появляние ошибки растянуто в времени.
  private final ImmutableList<String> CONTENT;
  private final Integer COUNT_WORDS;  // Нужно будет для генератора случайных числел
  //   но хранится будет здесь.
  // Индексты тоже сделать такими же
  private final ImmutableList<String> SORTED_WORDS_ALPH;
  private final ImmutableMultimap<String, String> WORDS_TRANSLATES;
  private final ImmutableMultimap<String, String> WORDS_CONTENT;

  public static void main(String[] args) {
    try {
      String fullFilename = Joiner.on(GlobalConstants.PATH_SPLITTER).join("statistic-data", "vocabularity-folded.txt");
      //Optional<ImmutableBECParser> cash = ImmutableBECParser.getDefaultCashInstance();
      //   Кажется обращение через cash не верное
      //Tools.print(ImmutableBECParser.getDefaultCashInstance().get().getWordByIdx(1110));
      //Tools.print(ImmutableBECParser.getDefaultCashInstance().get().getWordByIdx(110));
      ImmutableList<String> content = Tools.fileToList(fullFilename);

      ImmutableBECSave cash = ImmutableBECSave.create(content);

    } catch (IOException e) {
      Tools.print(e.getMessage());
      //} catch (VParserException e) {
      //  Tools.print(e.getMessage());
    } catch (IllegalStateException e) {
      Tools.print(e.getMessage());
    }
  }*/
}
