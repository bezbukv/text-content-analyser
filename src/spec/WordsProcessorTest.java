package spec;

import frozen.old.NewMapper;
import frozen.old.NewReducer;
import com.google.common.collect.Multimap;
import frozen.jobs_processors.JobsFormer;
import frozen.jobs_processors.ProcessorTargets;
import frozen.crosscuttings.AppConstants;
import com.google.common.base.Joiner;
import com.google.common.collect.Multiset;
import com.google.common.io.Closer;
import com.google.gson.Gson;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WordsProcessorTest {
  @Test
  public void testDevelop () {
    // Получаем работы
    List<List<String>> jobs = JobsFormer.getJobs();

    // Map Stage
    List<List> resultMapStage = new ArrayList<List>();
    for (List<String> job : jobs) {
      List one = NewMapper.mapper_word_level_with_compression(job);
      resultMapStage.add(one);
      //break;  // DEVELOP
    }

    // Shuffle Stage - сейчас фактически нет - один узел - один файл
    List<List> resultShuffleStage  = resultMapStage;

    // Reduce Stage  - так же нет, т.к. - один узел - один файл
    List<List> resultReduceStage = new ArrayList<List>();
    for (List task: resultShuffleStage) {
      List one = NewReducer.reduce_word_level_base(task);
      resultReduceStage.add(one);
    }

    // Save results
    for (List result: resultReduceStage) {
      // Куда сохраняем результаты
      String path_for_save = Joiner.on(AppConstants.PATH_SPLITTER)
          .join(
            ProcessorTargets.getPathToIndex(),
            AppConstants.COMPRESSED_IDX_FOLDER,
            result.get(NewMapper.IDX_NODE_NAME));

      String pathForSaveSortedIdx = Joiner.on(AppConstants.PATH_SPLITTER)
        .join(
          ProcessorTargets.getPathToIndex(),
          AppConstants.COMPRESSED_IDX_FOLDER,
          result.get(NewMapper.IDX_NODE_NAME),
          AppConstants.SORTED_IDX_FILENAME);
      String path_for_save_freq_idx = Joiner.on(AppConstants.PATH_SPLITTER)
        .join(
          ProcessorTargets.getPathToIndex(),
          AppConstants.COMPRESSED_IDX_FOLDER,
          result.get(NewMapper.IDX_NODE_NAME),
          AppConstants.FREQ_IDX_FILENAME);
      String path_for_save_rest_idx = Joiner.on(AppConstants.PATH_SPLITTER)
        .join(
          ProcessorTargets.getPathToIndex(),
          AppConstants.COMPRESSED_IDX_FOLDER,
          result.get(NewMapper.IDX_NODE_NAME),
          AppConstants.FILENAME_REST_IDX);
      String path_for_save_sentences_idx = Joiner.on(AppConstants.PATH_SPLITTER)
        .join(
          ProcessorTargets.getPathToIndex(),
          AppConstants.COMPRESSED_IDX_FOLDER,
          result.get(NewMapper.IDX_NODE_NAME),
          AppConstants.FILENAME_SENTENCES_IDX);

      try {
        Closer closer = Closer.create();
        try {
          // TODO(zaqwes) TOTH: в защитной секции должно быть только то что нужно, или разное?
          // Сохраняем сортированные индекс
          List<String> sorted_index =
              (ArrayList<String>)result.get(NewReducer.IDX_SORTED_IDX);

          // частоты
          Multiset<String> frequency_index =
              (Multiset<String>)result.get(NewMapper.IDX_FREQ_INDEX);

          // Обрезки слов
          Multimap<String, String> rest_words =
              (Multimap<String, String>)result.get(NewMapper.IDX_RESTS_MAP);

          // Sent. index
          Map<String, Collection<Integer>> sentences_inv_idx =
            ((Multimap<String, Integer>)result.get(NewMapper.IDX_SENT_MAP)).asMap();


          Map<String, Integer> index_for_save = new HashMap<String, Integer>();
          Map<String, String> rest_idx_for_save = new HashMap<String, String>();
          Map<String, List<Integer>> sentences_idx_for_save = new HashMap<String, List<Integer>>();
          for (String word: sorted_index) {
            index_for_save.put(word, frequency_index.count(word));
            rest_idx_for_save.put(word, Joiner.on(" ").join(rest_words.get(word)));
            sentences_idx_for_save.put(word, new ArrayList<Integer>(sentences_inv_idx.get(word)));
          }

          // Сохраняем в JSON
          closer.register(new BufferedWriter(new FileWriter(pathForSaveSortedIdx)))
              .write(new Gson().toJson(sorted_index));
          closer.register(new BufferedWriter(new FileWriter(path_for_save_freq_idx)))
              .write(new Gson().toJson(index_for_save));
          closer.register(new BufferedWriter(new FileWriter(path_for_save_rest_idx)))
              .write(new Gson().toJson(rest_idx_for_save));
          closer.register(new BufferedWriter(new FileWriter(path_for_save_sentences_idx)))
            .write(new Gson().toJson(sentences_idx_for_save));


          //InnerReuse.print(new Gson().toJson(index_for_save));
        } catch (Throwable e) {
          closer.rethrow(e);
        } finally {
          closer.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
