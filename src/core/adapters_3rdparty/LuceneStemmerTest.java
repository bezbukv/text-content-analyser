package core.adapters_3rdparty;

import com.google.gson.Gson;
import org.junit.Test;
import org.tartarus.snowball.ext.russianStemmer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import com.google.gson.internal.LinkedHashTreeMap;  // in 2.2.3 not in 2.2.2


public class LuceneStemmerTest {
  @Test
  public void testMain1() {
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
    public String stem(String fname) {
      try {
        BufferedReader in = new BufferedReader(new FileReader(fname));
        String s;
        while ((s = in.readLine())!= null) {
            String word = s;
            russianStemmer rs_new = new russianStemmer();
            rs_new.setCurrent(word);
            rs_new.stem();
            System.out.println(rs_new.getCurrent());
        }
      } catch (IOError e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return "Test return";

    }

  @Test
  public void testMain() {
    try {
        BufferedReader in = new BufferedReader(new FileReader("apps/indexes/first_index.json"));
        String s;
        HashMap<String, HashMap<String, Integer>> result_collection =
                new HashMap<String, HashMap<String, Integer>>();
        while ((s = in.readLine())!= null) {

            Gson gson = new Gson();


            //System.out.println(gson.fromJson(result, List.class));
            HashMap<String, ArrayList<HashMap<Integer, String>>> collectionType =
                new HashMap<String, ArrayList<HashMap<Integer,String>>>();


            //new Map<String, List<Map<Integer, String>>>();
            HashMap<String, ArrayList<HashMap<Integer, String>>>  type =
                    gson.fromJson(s, collectionType.getClass());

            // Можно обрабатывать

            for (Map.Entry<String, ArrayList<HashMap<Integer, String>>> entry : type.entrySet()) {
                String node_name = entry.getKey();
                ArrayList<HashMap<Integer, String>> value = entry.getValue();
                System.out.println("src: "+value.size());
                HashMap<String, Integer> result_list = new
                        HashMap<String, Integer>();
                //System.out.println(value);
                result_collection.put(node_name, result_list);
                int len = value.size();
                for(int i = 0; i < len; ++i) {
                    HashMap<Integer, String> wordTuple = value.get(i);
                    // Слова
                    for (Map.Entry<Integer, String> entry_word : wordTuple.entrySet()) {
                        Object count = entry_word.getKey();
                        String wordOne = entry_word.getValue();
                        String word = wordOne;
                        russianStemmer rs_new = new russianStemmer();
                        rs_new.setCurrent(word);
                        rs_new.stem();
                        String result = rs_new.getCurrent();

                        //System.out.println(result);
                        if (result_collection.get(node_name).containsKey(result)) {
                            Integer addValue = Integer.parseInt((String)count);
                            result_collection.get(node_name).put(result, result_collection.get(node_name).get(result)+addValue);
                        } else {
                            result_collection.get(node_name).put(result, Integer.parseInt((String)count));
                        }
                    }
                }
                System.out.println("result: "+result_collection.get(node_name).size());
                //break;
                // Do things with the list
            }


            // Пишем результат

        }
        Gson gson = new Gson();
        String json_index = gson.toJson(result_collection);
        PrintWriter out = new PrintWriter("apps/out.txt");
        out.println(json_index);
        out.close();


        /*word = "смотрим";
        rs_new.setCurrent(word);
        rs_new.stem();
        System.out.println(rs_new.getCurrent());  */
    } catch (IOError e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }
}
