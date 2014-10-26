package frozen.dal.accessors_text_file_storage;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 21.07.13
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
public class hided_code {

  static public void main(String[] args) {

    /*List<String> nodes = ImmutableBaseCoursor.getListNodes();
    String node = nodes.get(0);  // пока один

    List<String> filtered_sorted_idx = get_base_filtered_sorted_idx(node);
    Map<String, Integer> freq_idx = get_freq_idx(node);
    for (String stem: filtered_sorted_idx) {
      utils.print(Joiner.on(",").join(stem, freq_idx.get(stem)));
    }

    utils.print(get_sorted_idx(node).size());
    utils.print(filtered_sorted_idx.size());
    //ImmutableIdxGetters.get_coupled_idx_for_node(node, nodes.subList(1, nodes.size()));
    //ImmutableIdxGetters.get_follow_data(node, nodes);//.subList(1, nodes.size()));*/
  }



  /*
  static public HashMap<String, HashMap<String, String>>  getStaticNotes() {
    String metadata_static_notes_json = utils.file2string(
      Joiner.on(GlobalConstants.PATH_SPLITTER)
        .join(
          ImmutableProcessorTargets.getPathToIndex(),
          GlobalConstants.STATIC_NOTES_FILENAME));
    return (new Gson().fromJson(metadata_static_notes_json,
      new TypeToken<HashMap<String, HashMap<String, String>>>() {}.getType()));
  }

  static List<String> get_base_filtered_sorted_idx(String node) {
    List<String> sorted_idx = get_sorted_idx(node);

    List<String> filtered = new ArrayList<String>();
    for (String stem : sorted_idx) {
      if (ImmutableBaseFilter.isContentStem(stem)) {
        filtered.add(stem);
      }
    }
    return filtered;
  }

   */

   /*
  // Получаем пересечение индексов
  static void get_confluence_idx() {
    Multiset<String> confluence_idx = HashMultiset.create();
    List<String> nodes = ImmutableBaseCoursor.getListNodes();

    Map<String, Integer> one_freq_idx =  get_freq_idx(nodes.get(0));  // можно любой
    for (Map.Entry<String, Integer> pair:
        one_freq_idx.entrySet()) {
      String word = pair.getKey();
      // Ищем слово в индексах
      Boolean occure = new Boolean(true);
      Integer summary_frequency = 0;
      for (String node: nodes) {
        // Учитываются все узлы, а первый только для получения списка ключей
        Map<String, Integer> tmp_freq_idx =  get_freq_idx(node);
        if (!tmp_freq_idx.containsKey(word)) {
          occure = false;
          break;
        }
        summary_frequency += pair.getValue();
      }
      // Если нашелся ключ
      if (occure) {
        utils.print(word+", "+summary_frequency);
        confluence_idx.add(word, summary_frequency);
      }
    }
  }

  // Получить индекс со словами оставшимися после сжатия
  static public HashMap<String, String> get_rest_idx(String node) {
    String sorted_freq_idx_json = utils.file2string(
      Joiner.on(GlobalConstants.PATH_SPLITTER)
        .join(
          ImmutableProcessorTargets.getPathToIndex(),
          GlobalConstants.COMPRESSED_IDX_FOLDER,
          node,
          GlobalConstants.FILENAME_REST_IDX));
    return (new Gson().fromJson(sorted_freq_idx_json,
      new TypeToken<HashMap<String, String>>() {}.getType()));
  }


  */

  /*
  // Получить список указателей на предложеия в которых встречалось слово.
  static public HashMap<String, List<Integer>> get_sentences_idx(String node) {
    String sorted_freq_idx_json = Tools.fileToString(
      Joiner.on(GlobalConstants.PATH_SPLITTER)
        .join(
          ProcessorTargets.getPathToIndex(),
          GlobalConstants.COMPRESSED_IDX_FOLDER,
          node,
          GlobalConstants.FILENAME_SENTENCES_IDX));
    return (new Gson().fromJson(sorted_freq_idx_json,
      new TypeToken<HashMap<String, List<Integer>>>() {}.getType()));
  } */

  /*


  static public Optional<ImmutableList<String>> getSortedIdx(String node) {
    try {
      String sortedIdxJson = Tools.file2string(
        Joiner.on(GlobalConstants.PATH_SPLITTER)
          .join(
            ProcessorTargets.getPathToIndex(),
            GlobalConstants.COMPRESSED_IDX_FOLDER,
            node,
            GlobalConstants.SORTED_IDX_FILENAME));

      List<String> sortedIdxCash = (new Gson().fromJson(sortedIdxJson,
        new TypeToken<ArrayList<String>>() {}.getType()));
      return Optional.of(ImmutableList.copyOf(sortedIdxCash));
    } catch (CrosscuttingsException e) {
      return Optional.absent();
    }
  }
    */
}


