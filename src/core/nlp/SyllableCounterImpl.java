package core.nlp;


//import com.sun.xml.internal.ws.util.StringUtils;

import org.apache.commons.lang3.StringUtils;

public class SyllableCounterImpl {
  private static final String RUSSIAN_VOWEL[] = {"а", "е", "ё", "и", "о", "у", "ы", "э", "ю", "я"};

  // English
  //
  //Guava : anyOf(CharSequence)	 Specify all the characters you wish matched. For example, CharMatcher.anyOf("aeiou") matches lowercase English vowels.
  private static final String ENGLISH_VOWEL[] = {"a", "e", "i", "o", "u", "y"};
  private static final String ENGLISH_VOWEL_PAIRS[] = {"ai", "ay", "ee", "ei", "ie", "ie", "oi", "oy", "ue", "oo", "ou"};
  public static int calc (String word, String lang) {
    String workCopy = word.toLowerCase();
    if (lang.equals("ru")) {
      int count = 0;
      for (int i = 0; i < RUSSIAN_VOWEL.length; ++i) {
        count += StringUtils.countMatches(workCopy, RUSSIAN_VOWEL[i]);
      }
      return count;
    } else if (lang.equals("en")) {
      int count = 0;
      for (int i = 0; i < ENGLISH_VOWEL.length; ++i) {
        count += StringUtils.countMatches(workCopy, ENGLISH_VOWEL[i]);

      }

      // Удаляем некоторые
      if (workCopy.substring(workCopy.length()-1).equals("e")) {
        count--;
      }

      // Некоторые парные буквы пары слогов не образуют
      // НО ИНОГДА ЭТО ДВА ЗВУКА!
      for (int i = 0; i < ENGLISH_VOWEL_PAIRS.length; ++i) {
        count -= StringUtils.countMatches(workCopy, ENGLISH_VOWEL_PAIRS[i]);
      }

      //ImmutableAppUtils.print(count+" "+workCopy);
      return count;
    } else {
      return -1;
    }
  }
}
