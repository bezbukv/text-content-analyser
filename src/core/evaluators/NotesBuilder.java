package core.evaluators;


import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.Tools;
import frozen.crosscuttings.AppConstants;
import frozen.dal.accessors_text_file_storage.ImmutableIdxGetters;
import frozen.jobs_processors.ProcessorTargets;
import frozen.old.SentencesReduce;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotesBuilder {
  static public final String NOTE_N80_CAPACITY = "f80_p";  // Core
  static public final String NOTE_N20_CAPACITY = "f20";
  static public final String NOTE_N20_COUNT = "w20_p";  // Core
  static public final String NOTE_N80_COUNT = "w80";

  static public String json_get_notes_for_node(String node) {
    return new Gson().toJson(get_notes_for_node(node));
  }

  static public Map<String, String> get_notes_for_node(String node) {
    HashMap<String, HashMap<String, String>> metadataStaticNotes = ImmutableIdxGetters.getStaticNotes();  // TODO(): bad!

    // Получаем статические данные по сложности
    // Статические оценки
    Map<String, String> nodeStaticNotesInfo = metadataStaticNotes.get(node);

    // Данные для каждого из индексов по 80/20
    List<String> sorted_full_idx = ImmutableIdxGetters.get_sorted_idx(node);

    // сам частотынй индекс индекс
    HashMap<String, Integer> sorted_freq_idx = ImmutableIdxGetters.get_freq_idx(node);

    Integer total_amount = 0;
    for (String word: sorted_full_idx) total_amount += sorted_freq_idx.get(word);
    Double threshold = total_amount*0.8;

    // Оценка - 20% слов
    Integer count_unique_words = sorted_full_idx.size();
    Double N20 = count_unique_words*0.2;
    Double N80 = count_unique_words - N20;

    // Оценка - 80% интегральной частоты
    Double sum = new Double(0);
    Integer N80_Amount = 0;
    for (String word: sorted_full_idx) {
      if (sum > threshold) {
        break;
      }
      N80_Amount++;
      sum += sorted_freq_idx.get(word);
    }
    Double N20_Amount = new Double(count_unique_words)-N80_Amount;

    // Итого
    nodeStaticNotesInfo.put(NOTE_N80_CAPACITY, N80_Amount.toString());
    nodeStaticNotesInfo.put(NOTE_N20_CAPACITY, N20_Amount.toString());
    nodeStaticNotesInfo.put(NOTE_N20_COUNT, N20.toString());
    nodeStaticNotesInfo.put(NOTE_N80_COUNT, N80.toString());
    return nodeStaticNotesInfo;
  }

  static public String get_one_record(String node, Map<String, String> info) {
    Tools.print(node + " " + info);
    String record = Joiner.on(";")
        .join(
          node,
          info.get(SentencesReduce.NOTE_RE).replace('.', ','),
          info.get(SentencesReduce.NOTE_MEAN_TIME_FOR_READ).replace('.', ','),
          info.get(SentencesReduce.NOTE_MEAN_LEN_SENT).replace('.', ','),
          info.get(NotesBuilder.NOTE_N20_COUNT).replace('.', ','),
          info.get(NotesBuilder.NOTE_N80_COUNT).replace('.', ','),
          info.get(NotesBuilder.NOTE_N80_CAPACITY).replace('.', ','),
          info.get(NotesBuilder.NOTE_N20_CAPACITY).replace('.', ','));
    return record;
  }

  public static List<String> get_urls_and_langs_node(String node){
    // Путь к мета-файлу
    String pathToMetaFile = Joiner.on(AppConstants.PATH_SPLITTER)
      .join(
        ProcessorTargets.getPathToIndex(),
        AppConstants.CONTENT_FOLDER,
        node,
        AppConstants.CONTENT_META_FILENAME);

    // Преобразуем в json
    String settingsInJson = Tools.file2string(pathToMetaFile);
    Type type = new TypeToken<List<List<String>>>() {}.getType();
    List<List<String>> metadata = new Gson().fromJson(settingsInJson, type);

    // Можно вытряхивать
    List<String> info = new ArrayList<String>();
    for (List<String> item: metadata) {
      String record = "Source url: "+item.get(0)+" Language: "+item.get(1);
      info.add(record);
    }
    return info;
  }
}
