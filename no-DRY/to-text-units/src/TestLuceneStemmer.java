/**
 * Created with IntelliJ IDEA.
 * User: Igor
 * Date: 22.04.13
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */

/*import org.apache.lucene.morphology.EnglishAnalyzer;
import org.apache.lucene.morphology.english.EnglishLuceneMorphology;
//import org.apache.lucene.morphology.english.
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.apache.lucene.morphology.russian.RussianMorphology;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.ru.RussianLightStemmer;     */
import org.tartarus.snowball.ext.russianStemmer;

import java.io.IOError;
import java.io.IOException;
import java.util.List;

public class TestLuceneStemmer {
    public static void main(String[] args) {
        try {
            //EnglishAnalyzer english = new EnglishAnalyzer();
            //english.
            /*LuceneMorphology luceneMorph = new RussianLuceneMorphology();
            String word = "раскраска";
            List<String> wordBaseForms = luceneMorph.getMorphInfo(word);
            System.out.print(wordBaseForms);
            word = "вина";
            wordBaseForms = luceneMorph.getMorphInfo(word);
            System.out.print(wordBaseForms);

            RussianLightStemmer rs = new RussianLightStemmer();
            word = "смотрел";
            int result = rs.stem(word.toCharArray(), word.length());
            System.out.println("\n"+word);
            word = "смотрела";
            result = rs.stem(word.toCharArray(), word.length());
            System.out.println(word);   */

            String word = "смотрела";
            russianStemmer rs_new = new russianStemmer();
            rs_new.setCurrent(word);
            rs_new.stem();
            System.out.println(rs_new.getCurrent());

            word = "смотрим";
            rs_new.setCurrent(word);
            rs_new.stem();
            System.out.println(rs_new.getCurrent());
        } catch (IOError e) {
            e.getStackTrace();
        }
    }
}
