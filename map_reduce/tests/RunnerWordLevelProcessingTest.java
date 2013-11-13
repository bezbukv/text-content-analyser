import com.google.common.collect.Multimap;
import common.ImmutableAppUtils;
import common.utils;
import crosscuttings.AppConstants;
import jobs_processors.ImmutableJobsFormer;
import jobs_processors.ImmutableProcessorTargets;
import com.google.common.base.Joiner;
import com.google.common.collect.Multiset;
import com.google.common.io.Closer;
import com.google.gson.Gson;
import mapreduce.ImmutableMappers;
import mapreduce.ImmutableReduces;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 10.05.13
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
public class RunnerWordLevelProcessingTest {
  @Test
  public void testDevelop () {
    // Получаем работы
    List<List<String>> jobs = ImmutableJobsFormer.getJobs();

    // Map Stage
    List<List> result_map_stage = new ArrayList<List>();
    for (List<String> job : jobs) {
      List one = ImmutableMappers.mapper_word_level_with_compression(job);
      result_map_stage.add(one);
      //break;  // DEVELOP
    }

    // Shuffle Stage - сейчас фактически нет - один узел - один файл
    List<List> result_shuffle_stage  = result_map_stage;

    // Reduce Stage  - так же нет, т.к. - один узел - один файл
    List<List> result_reduce_stage = new ArrayList<List>();
    for (List task: result_shuffle_stage) {
      List one = ImmutableReduces.reduce_word_level_base(task);
      result_reduce_stage.add(one);
    }

    // Save results
    for (List result: result_reduce_stage) {
      // Куда сохраняем результаты
      String path_for_save = Joiner.on(AppConstants.PATH_SPLITTER)
          .join(
            ImmutableProcessorTargets.getPathToIndex(),
            AppConstants.COMPRESSED_IDX_FOLDER,
            result.get(ImmutableMappers.IDX_NODE_NAME));

      String path_for_save_sorted_idx = Joiner.on(AppConstants.PATH_SPLITTER)
        .join(
          ImmutableProcessorTargets.getPathToIndex(),
          AppConstants.COMPRESSED_IDX_FOLDER,
          result.get(ImmutableMappers.IDX_NODE_NAME),
          AppConstants.SORTED_IDX_FILENAME);
      String path_for_save_freq_idx = Joiner.on(AppConstants.PATH_SPLITTER)
        .join(
          ImmutableProcessorTargets.getPathToIndex(),
          AppConstants.COMPRESSED_IDX_FOLDER,
          result.get(ImmutableMappers.IDX_NODE_NAME),
          AppConstants.FREQ_IDX_FILENAME);
      String path_for_save_rest_idx = Joiner.on(AppConstants.PATH_SPLITTER)
        .join(
          ImmutableProcessorTargets.getPathToIndex(),
          AppConstants.COMPRESSED_IDX_FOLDER,
          result.get(ImmutableMappers.IDX_NODE_NAME),
          AppConstants.FILENAME_REST_IDX);
      String path_for_save_sentences_idx = Joiner.on(AppConstants.PATH_SPLITTER)
        .join(
          ImmutableProcessorTargets.getPathToIndex(),
          AppConstants.COMPRESSED_IDX_FOLDER,
          result.get(ImmutableMappers.IDX_NODE_NAME),
          AppConstants.FILENAME_SENTENCES_IDX);

      try {
        Closer closer = Closer.create();
        try {
          // TODO(zaqwes) TOTH: в защитной секции должно быть только то что нужно, или разное?
          // Сохраняем сортированные индекс
          List<String> sorted_index =
              (ArrayList<String>)result.get(ImmutableReduces.IDX_SORTED_IDX);

          // частоты
          Multiset<String> frequency_index =
              (Multiset<String>)result.get(ImmutableMappers.IDX_FREQ_INDEX);

          // Обрезки слов
          Multimap<String, String> rest_words =
              (Multimap<String, String>)result.get(ImmutableMappers.IDX_RESTS_MAP);

          // Sent. index
          Map<String, Collection<Integer>> sentences_inv_idx =
            ((Multimap<String, Integer>)result.get(ImmutableMappers.IDX_SENT_MAP)).asMap();


          Map<String, Integer> index_for_save = new HashMap<String, Integer>();
          Map<String, String> rest_idx_for_save = new HashMap<String, String>();
          Map<String, List<Integer>> sentences_idx_for_save = new HashMap<String, List<Integer>>();
          for (String word: sorted_index) {
            index_for_save.put(word, frequency_index.count(word));
            rest_idx_for_save.put(word, Joiner.on(" ").join(rest_words.get(word)));
            sentences_idx_for_save.put(word, new ArrayList<Integer>(sentences_inv_idx.get(word)));
          }

          // Сохраняем в JSON
          closer.register(new BufferedWriter(new FileWriter(path_for_save_sorted_idx)))
              .write(new Gson().toJson(sorted_index));
          closer.register(new BufferedWriter(new FileWriter(path_for_save_freq_idx)))
              .write(new Gson().toJson(index_for_save));
          closer.register(new BufferedWriter(new FileWriter(path_for_save_rest_idx)))
              .write(new Gson().toJson(rest_idx_for_save));
          closer.register(new BufferedWriter(new FileWriter(path_for_save_sentences_idx)))
            .write(new Gson().toJson(sentences_idx_for_save));


          //ImmutableAppUtils.print(new Gson().toJson(index_for_save));
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
