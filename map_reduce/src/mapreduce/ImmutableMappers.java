package mapreduce;


import common.ImmutableAppUtils;
import jobs_processors.ImmutableJobsFormer;
import nlp.BaseTokenizer;
import com.google.common.base.CharMatcher;
import com.google.common.collect.*;
import com.google.common.io.Closer;
import org.apache.commons.lang3.StringUtils;
import org.tartarus.snowball.ext.englishStemmer;
import org.tartarus.snowball.ext.russianStemmer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 10.05.13
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */
final public class ImmutableMappers {

  /*
  *
  * Mapper for word level processing
  * */
  public final static int IDX_NODE_NAME = 0;
  public final static int IDX_FREQ_INDEX = 1;
  public final static int IDX_LANG_MAP = 2;
  public final static int IDX_SENT_MAP = 3;
  public final static int IDX_RESTS_MAP = 4;
  public static List mapper_word_level(List<String> job) {
    List response = new ArrayList();
    String node = job.get(ImmutableJobsFormer.IDX_NODE_NAME);
    String filename = job.get(ImmutableJobsFormer.IDX_FILENAME);

    // Processing
    try {
      Closer closer = Closer.create();
      try {
        int sentenceNumber = 1;
        Multiset<String> wordsFrequenceMultyset = HashMultiset.create();
        HashMultimap<String, String> langMap = HashMultimap.create();
        HashMultimap<String, Integer> sentencesPtrsMap = HashMultimap.create();

        // Reading
        BufferedReader reader = closer.register(new BufferedReader(new FileReader(filename)));
        String s;
        while ((s = reader.readLine()) != null) {
          String workCopy = s.toLowerCase();
          int langPtr = workCopy.indexOf(' ');
          List<String> words = BaseTokenizer.extractWords(workCopy.substring(langPtr, workCopy.length()));
          String lang = workCopy.substring(0, langPtr);

          // Добавляем в частотный индекс
          wordsFrequenceMultyset.addAll(words);

          // Получаем язык, нужно для деления на слоги
          for (String word : words) {
            langMap.put(word, lang);
            sentencesPtrsMap.put(word, sentenceNumber);
          }

          // Указываем не следующее
          sentenceNumber++;
        }

        ImmutableAppUtils.print("Size Raw = " + wordsFrequenceMultyset.elementSet().size());

        // Make result
        response.add(node);
        response.add(wordsFrequenceMultyset);
        response.add(langMap);
        response.add(sentencesPtrsMap);
        response.add(null);
      } catch (Throwable e) {
        closer.rethrow(e);
      } finally {
        closer.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    //
    return response;
  }

  public static List mapper_word_level_with_compression(List<String> job) {
    List one = ImmutableMappers.mapper_word_level(job);
    Multiset<String> frequencies = (Multiset<String>)one.get(ImmutableMappers.IDX_FREQ_INDEX);
    String node = (String)one.get(ImmutableMappers.IDX_NODE_NAME);

    // Буду брать только нулевыой элемент, но из-за того, что язык опред. по документу
    //   одному слову может соотв. несколько языков
    HashMultimap<String, String> langPerWordMap =
        (HashMultimap<String, String>)one.get(ImmutableMappers.IDX_LANG_MAP);

    // Номера единиц контента
    HashMultimap<String, Integer> sentencesRawMap =
      (HashMultimap<String, Integer>)one.get(ImmutableMappers.IDX_SENT_MAP);

    // Результаты стадии
    Multiset<String> frequenciesCompressed = HashMultiset.create();
    Multimap<String, String> frequenciesWordRest = HashMultimap.create();
    Multimap<String, String> langMeanMapCompressed = HashMultimap.create();
    Multimap<String, Integer> sentencesCompressed = HashMultimap.create();

    // Компрессоры
    russianStemmer ruStemmer = new russianStemmer();
    englishStemmer enStemmer = new englishStemmer();

    // Перебираем слова
    for (String key: frequencies.elementSet()) {
      if (isEnabled(key)) {
        String compressedKey = key;
        String meanLangWord = (new ArrayList<String>(langPerWordMap.get(key))).get(0);
        if (meanLangWord.equals("ru")) {
          // Сжимаем ключ
          ruStemmer.setCurrent(key);
          ruStemmer.stem();
          compressedKey = ruStemmer.getCurrent();
          if (RUSSIAN_STOP_WORDS.contains(compressedKey)) {
            // В списке стоп-слов
            // Отброшенное слово при второй фильтарции
            //ImmutableAppUtils.print(key);
            continue;
          }
        } else if (meanLangWord.equals("en")) {
          // Сжимаем ключ
          enStemmer.setCurrent(key);
          enStemmer.stem();
          compressedKey = enStemmer.getCurrent();
        } else {
          compressedKey = key;
        }

        frequenciesCompressed.add(compressedKey, frequencies.count(key));
        langMeanMapCompressed.put(compressedKey, meanLangWord);
        frequenciesWordRest.put(compressedKey, key);
        sentencesCompressed.putAll(compressedKey, sentencesRawMap.get(key));
      } else {
        // Отброшенное слово при перой фильтарции
        //ImmutableAppUtils.print(key);
      }
    }

    // Смотрим результат
    /*
    for (String key: frequenciesCompressed.elementSet()) {
      ImmutableAppUtils.print(
        Joiner.on(" ").join(
          node,
          key,
          frequenciesCompressed.count(key),
          langMeanMapCompressed.get(key),
          sentencesCompressed.get(key),
          frequenciesWordRest.get(key)));
    }
    //*/
    ImmutableAppUtils.print("Size = "+frequenciesCompressed.elementSet().size()+" "+node);

    // Make result
    List result = new ArrayList();
    result.add(node);  // Имя узла
    result.add(frequenciesCompressed);  // Частотный индекс
    result.add(langMeanMapCompressed);  // Языковой индекс
    result.add(sentencesCompressed);  // Предложения
    result.add(frequenciesWordRest);  // Остаточный индекс
    return result;
  }

  // Base filters
  private static final Set<String> RUSSIAN_STOP_WORDS = new HashSet<String>(Arrays.asList("ль", "рад", "преимуществ", "нача", "примен", "прит", "формул", "раз", "сам", "вопрек", "вкуп", "ран", "будт", "поскольк", "вмест", "близ", "с", "присущ", "у", "позвол", "при", "случа", "стол", "эт", "прич", "дан", "достига", "опя", "немног", "моч", "снабд", "пример", "про", "дат", "подтверд", "все", "дела", "пот", "хот", "применя", "никак", "перед", "поч", "как", "провод", "состоя", "обуславлива", "некотор", "ли", "этот", "наш", "несмотр", "мал", "различн", "нескольк", "ко", "пользова", "создава", "служ", "над", "вблиз", "совс", "сво", "цел", "рассмотрет", "яв", "чег", "ввид", "никт", "отт", "чем", "нов", "тем", "выполня", "внов", "под", "описа", "ближайш", "насчет", "заявля", "пок", "сдела", "вкратц", "нужн", "поз", "дал", "посл", "даж", "существова", "даб", "разн", "знат", "кром", "этак", "нельз", "разв", "тут", "использован", "к", "определя", "им", "ин", "вдол", "начина", "и", "откуд", "обозначен", "ил", "о", "поясня", "кто", "в", "новизн", "их", "а", "назва", "ит", "ж", "е", "осуществля", "быт", "немал", "позволя", "нискольк", "был", "изображен", "очен", "небольш", "необходим", "поначал", "нема", "сущност", "нек", "фиг", "всюд", "из-под", "нем", "известн", "дава", "вот", "вроз", "сперв", "пр", "нет", "врод", "тогд", "никуд", "предложен", "себ", "называ", "немн", "неч", "подл", "заявк", "зач", "показа", "однак", "идт", "по", "зат", "рассматрива", "нег", "почт", "взам", "отнюд", "смоч", "реализова", "пут", "прежд", "прежн", "от", "позж", "предполож", "вдруг", "он", "охарактеризовыва", "част", "впред", "получ", "скольк", "постольк", "проведен", "об", "зде", "отсюд", "станов", "частн", "наибольш", "но", "изготов", "поздн", "нибуд", "вряд", "межд", "ни", "не", "пред", "достаточн", "следова", "на", "приведен", "согласн", "обуслов", "когд", "где", "работоспособн", "можн", "инач", "стольк", "ним", "хотет", "простот", "обеспечива", "нич", "созда", "относ", "всяк", "них", "впоследств", "такж", "существ", "сверх", "описан", "налич", "снабжа", "предлага", "друг", "выполнен", "установлен", "осуществ", "последова", "сил", "упрощен", "да", "поперек", "ве", "сперед", "наряд", "без", "предназнач", "во", "нигд", "наканун", "патент", "понемног", "итак", "либ", "отлич", "характеризова", "помим", "напр", "невозможн", "преимуществен", "представля", "осуществлен", "предполага", "чертеж", "изобретен", "мим", "расположен", "работа", "обеспеч", "тож", "благодар", "сообразн", "через", "получа", "сопоставительн", "указа", "че", "бы", "чтоб", "тог", "конкретн", "окол", "сраз", "посредств", "создан", "прост", "использова", "предлож", "ниоткуд", "выполн", "далек", "изготовлен", "требова", "нект", "вслед", "существен", "охарактеризова", "наподоб", "применим", "особен", "см", "со", "относительн", "снов", "горазд", "показыва", "некогд", "есл", "котор", "близк", "возл", "решен", "из", "ест", "представ", "что", "буд", "сравнен", "описыва", "счет", "аналог", "вокруг", "тот", "реализац", "соб", "том", "никогд", "результат", "наибол", "то", "полн", "тольк", "изобретательск", "устанавлива", "имет", "отч", "вед", "имен", "за", "куд", "поставлен", "подчас", "та", "обычн", "заключа", "совокупн", "скол", "чут", "ничт", "ничут", "предназнача", "лиш", "для", "так", "частност", "ем", "из-з", "там", "подтвержда", "отличн", "вероятн", "выраз", "снача", "едв", "вследств", "определен", "же", "прот", "указыва", "явля", "намн", "отлича", "ещ", "кажд", "др", "почест", "до", "содержа", "определ", "должн", "заявлен", "прототип", "поэт", "недостаток", "ег", "сред", "след", "например" ));
  public static final int MIN_COUNT_LETTERS_IN_WORD = 2;
  private static boolean isEnabled(String key) {
    // предлоги
    //
    String noDigits = CharMatcher.JAVA_DIGIT.replaceFrom(key, "*"); // star out all digits
    int countDigits = StringUtils.countMatches(noDigits, "*");
    if (countDigits == 0) {
      // в ключе чисел нет
      if (key.length() > MIN_COUNT_LETTERS_IN_WORD) {
        // однобуквенные удаляем
        if (StringUtils.countMatches(key, ".") == 0) {
          // Нет точек в слове
          return true;
        }
      }
    }
    return false;
  }
 }
