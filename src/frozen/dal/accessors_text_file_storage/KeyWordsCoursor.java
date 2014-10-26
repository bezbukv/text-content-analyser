package frozen.dal.accessors_text_file_storage;

@Deprecated
public class KeyWordsCoursor {
  private static final String WEB_NEW_LINE = "<br>";
  static Double cut_to(Double value) {
    Double time_to_read = Math.floor(100*value)*1.0/100;
    return time_to_read;
  }
  public static void main(String [] args) {
    // Need read nodes
    /*List<String> nodes = ImmutableBaseCoursor.getListNodes();

    // Получаем индекс-пересечеие
    HashMap<String, HashMap<String, String>>  notes = ImmutableIdxGetters.getStaticNotes();

    // Real processing
    for (String node: nodes) {
      Double time_to_read = cut_to(Double.parseDouble(
          notes.get(node).get(SentencesReduce.NOTE_MEAN_TIME_FOR_READ)));
      utils.print(Joiner.on(", ")
        .join(
          (long)Math.floor(time_to_read),
          (long)Math.floor((time_to_read-Math.floor(time_to_read))*60),
          node));
      List<String> page = new ArrayList<String>();
      page.add("<!DOCTYPE html><html><head>" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">" +
        "</head><body>");
      List<String> rpt = new ArrayList<String>();

      // Получаем исходные адреса
      rpt.add("Content item name: "+node+WEB_NEW_LINE);
      //rpt.addAll(ImmutableIdxGetters.get_urls_and_langs_node(node));
      page.add(Joiner.on(WEB_NEW_LINE).join(rpt));
      page.add(WEB_NEW_LINE+WEB_NEW_LINE);

      // Получаем сортировынный список слов
      List<String> sortedWords = ImmutableIdxGetters.get_sorted_idx(node);
      Map<String, Integer> frequencyIdx = ImmutableIdxGetters.get_freq_idx(node);
      List<String> recordPerWord = new ArrayList<String>();
      HashMap<String, String> restIdx = ImmutableIdxGetters.get_rest_idx(node);
      HashMap<String, List<Integer>> sentences_idx = ImmutableIdxGetters.get_sentences_idx(node);

      List<String> list_sentences = ImmutableIdxGetters.get_list_sentences(node);
      for (String word: sortedWords) {
        List<Integer> idxs_sentences = sentences_idx.get(word);
        Iterable<String> rest_words =
            Splitter.on(" ")
                .trimResults()
                .omitEmptyStrings()
                .split(restIdx.get(word));
        String sentence = list_sentences.get(idxs_sentences.get(0)-1);
        sentence = sentence.substring(sentence.indexOf(" ")+1, sentence.length());
        for (String rest_word: rest_words) {
          sentence = sentence.replace(rest_word,
              "<span style=\"background-color:Crimson;color:azure;\">"+rest_word+"</span>");
          //utils.print(rest_word);
          //break;  // DEVELOP
        }

        // Добавляем в запись
        recordPerWord.add(Joiner.on("<br>&nbsp&nbsp&nbsp")
          .join(
            "Key word: <span style=\"background-color:Crimson;color:azure;\">"+word+"</span>",
            "Actuality: "+"<span style=\"background-color:OrangeRed;color:azure;\">"+"new"+"</span>",
            "Rest words: "+"<span style=\"background-color:DarkCyan;color:azure;\">"+rest_words.toString()+"</span>",
            "Frequency: <span style=\"background-color:Crimson;color:azure;\">"+frequencyIdx.get(word).toString()+"</span>",
            "Count sentences: <span style=\"background-color:Crimson;color:azure;\">"+sentences_idx.get(word).size()+"</span>",
            "<span style=\"background-color:Darkorange;color:azure;\">"+"Content item:"+"</span> "+sentence));
        recordPerWord.add("<hr>");
        // Если слова в глобально списке ингорирования - одни цвеь

        // Если в локальном - другой
        //break;  // DEVELOP
      }
      page.add("<hr>");
      page.add(Joiner.on(WEB_NEW_LINE).join(recordPerWord));


      // Получаем оценки

      // сохраняем отчет

      page.add("</body></html>");
      String rptFileName = Joiner.on(GlobalConstants.PATH_SPLITTER)
        .join("rpts", "key_words",node+".html");
      try {
        utils.list2file(page, rptFileName);
      } catch (IOException e) {
        e.printStackTrace();
      }
      //break;  // DEVELOP

    }
    //Multiset<String> confluence_idx = ImmutableIdxGetters.get_confluence_idx();
    // rpt
    */
  }
}
