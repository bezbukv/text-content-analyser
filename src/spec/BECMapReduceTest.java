package spec;


import common.Tools;
import frozen.old.JobsFormer;
import frozen.old.OldMapper;
import frozen.old.OldReducer;
import frozen.GlobalConstants;
import frozen.ProcessorTargets;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import com.google.common.io.Closer;
import com.google.gson.Gson;
import frozen.crosscuttings.CrosscuttingsException;
//import frozen.dal.accessors_text_file_storage.IdxAccessor;
import frozen.spiders_extractors.ImmutableBECParser;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class BECMapReduceTest {
  private BECMapReduceTest() {}

  @Test
  public void main() {
    BECMapReduceTest.runBECChain();
  }

  public static void runSentencesLevelProcess () {
    /*
    // Получаем работы
    List<List<String>> jobs = JobsFormer.getJobs();

    // Map Stage
    List<List> result_map_stage = new ArrayList<List>();
    for (List<String> job : jobs) {
      result_map_stage.add(OldMapper.map(job));
      //break;  // DEVELOP
    }

    // Shuffle Stage - сейчас фактически нет - один узел - один файл
    List<List> result_shuffle_stage  = result_map_stage;

    // Reduce Stage  - так же нет, т.к. - один узел - один файл
    Map<String, Map<String, String>> result_reduce_stage =
        new HashMap<String, Map<String, String>>();
    for (List task: result_shuffle_stage) {
      Map<String, String> one = OldReducer.reduce_sentences_level(task);

      String node_name = (String)task.get(OldMapper.IDX_NODE_NAME);
      result_reduce_stage.put(node_name, one);
    }


    // Save result
    try {
      Closer closer = Closer.create();
      try {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String resultInJson = gson.toJson(result_reduce_stage);
        // Write
        String path_for_save = Joiner.on(GlobalConstants.PATH_SPLITTER)
          .join(
            ImmutableProcessorTargets.getPathToIndex(),
            GlobalConstants.STATIC_NOTES_FILENAME);

        BufferedWriter out = closer.register(new BufferedWriter(new FileWriter(path_for_save)));
        out.write(resultInJson);
        Tools.print(resultInJson);

        Tools.print("Notes write to file : "+path_for_save);

      } catch (Throwable e) {
        closer.rethrow(e);
      } finally {
        closer.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    Tools.print("Done. Sentences level processing.");
    */
  }


  public void runWordLevelProcess () {
    Optional<List<List<String>>> jobs = Optional.absent();
    try {
      // Получаем работы
      jobs = Optional.of(JobsFormer.getJobs());

    } catch (CrosscuttingsException e) {
      Tools.print(e.getMessage());
    }

    // Map Stage
    List<List> resultMapStage = new ArrayList<List>();
    for (List<String> job : jobs.get()) {
      List one = OldMapper.mapperWordLevel(job);
      resultMapStage.add(one);
      //break;  // DEVELOP
    }

    // Shuffle Stage - сейчас фактически нет - один узел - один файл
    List<List> resultShuffleStage  = resultMapStage;

    // Reduce Stage  - так же нет, т.к. - один узел - один файл
    List<List> resultReduceStage = new ArrayList<List>();
    for (List task: resultShuffleStage) {
      List one = OldReducer.reduce_word_level_base(task);
      resultReduceStage.add(one);
    }

    // Save results
    for (final List nodeResult: resultReduceStage) {
      String pathToNode;
      try {
        pathToNode = Joiner.on(GlobalConstants.PATH_SPLITTER)
          .join(
            ProcessorTargets.getPathToIndex(),
            GlobalConstants.COMPRESSED_IDX_FOLDER,
            nodeResult.get(OldMapper.IDX_NODE_NAME));
      } catch (CrosscuttingsException e) {
        Tools.print(e.getMessage());
        pathToNode = "";
      }

      // TODO(zaqwes) TOTH: в защитной секции должно быть только то что нужно, или разное?
      // Может быть они тоже нормально сереализуются?
      // Вообще наверное лучше хранить в базе данных, а не в файлах.
      List<String> idxSortedByFrequencyForSave = (ArrayList<String>)nodeResult.get(OldReducer.IDX_SORTED_IDX);
      Multiset<String> idxFrequencies = (Multiset<String>)nodeResult.get(OldMapper.IDX_FREQ_INDEX);

      Multimap<String, Integer> idxSentencesPtrs = (Multimap<String, Integer>)nodeResult.get(OldMapper.IDX_SENT_MAP);
      //Multimap<String, String> idxRestWords = (Multimap<String, String>)nodeResult.get(OldMapper.IDX_RESTS_MAP);

      // Структуры для сереализации
      // Перекомпановка. Возможно есть средства в Guava, но пусть будет
      //   дополнительная фильтрация по сортированному индексу.
      Map<String, Integer> idxFrequenciesForSave = recodeMultiset(idxFrequencies, idxSortedByFrequencyForSave);

      //Map<String, String> idxRestWordsForSave = recodeSS(idxRestWords, idxSortedByFrequencyForSave);
      Map<String, Collection<Integer>> idxSentencesForSave = idxSentencesPtrs.asMap();
      try {
        // Само сохранение. Вряд ли удасться выделить в метод. И свернуть в цикл.
        Closer closer = Closer.create();
        try {
          // Сохраняем в JSON
          closer.register(new BufferedWriter(new FileWriter(
            Joiner.on(GlobalConstants.PATH_SPLITTER).join(pathToNode, GlobalConstants.SORTED_IDX_FILENAME))))
            .write(new Gson().toJson(idxSortedByFrequencyForSave));
          closer.register(new BufferedWriter(new FileWriter(
              Joiner.on(GlobalConstants.PATH_SPLITTER).join(pathToNode, GlobalConstants.FREQ_IDX_FILENAME))))
            .write(new Gson().toJson(idxFrequenciesForSave));
          //closer.register(new BufferedWriter(new FileWriter(
          //    Joiner.on(GlobalConstants.PATH_SPLITTER).join(pathToNode, GlobalConstants.FILENAME_REST_IDX))))
          //  .write(new Gson().toJson(idxRestWordsForSave));
          closer.register(new BufferedWriter(new FileWriter(
              Joiner.on(GlobalConstants.PATH_SPLITTER).join(pathToNode, GlobalConstants.FILENAME_SENTENCES_IDX))))
            .write(new Gson().toJson(idxSentencesForSave));
        } catch (Throwable e) {
          closer.rethrow(e);
        } finally {
          closer.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }  // for..
  }

  private static  Map<String, Integer> recodeMultiset(Multiset<String> in, List<String> filter) {
    Map<String, Integer> idxFrequenciesForSave = new HashMap<String, Integer>();
    for (final String word: filter) {
      idxFrequenciesForSave.put(word, in.count(word));
    }
    return idxFrequenciesForSave;
  }


  // Chain for BEC dictionary.
  public static void runBECChain() {
    // Как-то нужно правильно сопоставить слово и контент.
    try {
      final String pathToAppFolder = "";//GlobalConfigurator.getPathToAppFolder();
      // Begin "MapReduce" stage
      ImmutableList<String> content =
          Tools.fileToList(
            Joiner.on(GlobalConstants.PATH_SPLITTER)
              .join("raw-dicts", "vocabularity-folded.txt"));
      ImmutableBECParser cash = ImmutableBECParser.create(content);

      // Извлекаем данные и обрабатываем их
      List<String> idxSortedByFrequencyForSave = cash.getDict();
      Multiset<String> frequencyIdx = HashMultiset.create();
      Multimap<String, String> dictContent = cash.getContent();
      Multimap<String, String> dictTranslate = cash.getWordTranslates();

      Multimap<String, Integer> idxSentencesPtrs = HashMultimap.create();
      List<String> sentences = new ArrayList<String>();

      // Нуменация предложения с нуля!
      int idxSentence = 1;
      Set<String> keys = dictContent.keySet();
      String lang = "en";
      for (final String key: keys) {
        Collection<String> value = dictContent.asMap().get(key);
        for (final String item: value) {
          sentences.add(lang+" "+item);
          idxSentencesPtrs.put(key, idxSentence);
          ++idxSentence;
        }
      }

      // Сохраняем список единиц контента
      Tools.listToFile(sentences, Joiner.on(GlobalConstants.PATH_SPLITTER)
        .join(pathToAppFolder, "bec-node", "content.txt"));

      // Make frequency index. Пока плохо - словарь грязный.
      for (final String word: idxSortedByFrequencyForSave) {
        frequencyIdx.add(word);
      }
      // End "MapReduce" stage

      // Save
      String pathToNode = pathToAppFolder+ GlobalConstants.PATH_SPLITTER+"bec-node";
      Map<String, Object> tmp = new HashMap<String, Object>();
      Map<String, Integer> idxFrequenciesForSave = recodeMultiset(frequencyIdx, idxSortedByFrequencyForSave);
      Map<String, Collection<Integer>> idxSentencesForSave = idxSentencesPtrs.asMap();

      tmp.put(Joiner.on(
          GlobalConstants.PATH_SPLITTER).join(pathToNode, GlobalConstants.SORTED_IDX_FILENAME),
          idxSortedByFrequencyForSave);
      tmp.put(Joiner.on(
          GlobalConstants.PATH_SPLITTER).join(pathToNode, GlobalConstants.FREQ_IDX_FILENAME),
          idxFrequenciesForSave);
      tmp.put(Joiner.on(
          GlobalConstants.PATH_SPLITTER).join(pathToNode, GlobalConstants.FILENAME_SENTENCES_IDX),
          idxSentencesForSave);

    } catch (IOException e) {
      Tools.print(e.getMessage());
    } catch (IllegalStateException e) {
      Tools.print(e.getMessage());
    }
  }
}
