package com.github.zaqwes8811.processor_word_frequency_index.spiders_processors;

import com.github.zaqwes8811.processor_word_frequency_index.AppConstants;
import com.github.zaqwes8811.processor_word_frequency_index.crosscuttings.CrosscuttingsException;
import com.github.zaqwes8811.processor_word_frequency_index.crosscuttings.ImmutableAppConfigurator;
import com.github.zaqwes8811.processor_word_frequency_index.crosscuttings.ImmutableAppUtils;
import com.github.zaqwes8811.processor_word_frequency_index.crosscuttings.ImmutableProcessorTargets;

import com.github.zaqwes8811.processor_word_frequency_index.index_coursors.ImmutableBaseCoursor;
import com.github.zaqwes8811.processor_word_frequency_index.spiders_extractors.ImmutableTikaWrapper;
import com.google.common.io.Closer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: кей
 * Date: 08.05.13
 * Time: 10:41
 * To change this template use File | Settings | File Templates.
 */
public class MinimalSpiderProcessor {
  // DEVELOP
  static void print(Object msg) {
    System.out.println(msg);
  }
  // DEVELOP

  static final int IDX_LANG = 0;
  static final int IDX_SRC_URL = 1;
  static final int IDX_TMP_FILE = 2;

  public List<String> getListNamesMetaFiles(String pathToNode) {
    String regex = ".+\\.meta";
    List<String> result = ImmutableAppUtils.getListNamesMetaFiles(pathToNode, regex);
    return result;
  }

  /*
  *
  * @return: [[lang_mean0, src_url0 filename0], []]
  * */
  public List<List<String>> getTarget(String pathToNode) {
    List<List<String>> targetsInfo = new ArrayList<List<String>>();
    List<String> listNamesMetaFiles = getListNamesMetaFiles(pathToNode);
    for (String filename: listNamesMetaFiles) {

      List<String> oneTarget = new ArrayList<String>();

      // Получаем язык файла, оцененный или заранее известрый, это отражено в мета-файле
      String metafilename = pathToNode+'/'+filename;
      oneTarget.addAll(getAllMetaData(metafilename));

      // Получаем имя файла с контентом
      String fileWithContent = metafilename.substring(0, metafilename.lastIndexOf('.'))+".ptxt";
      oneTarget.add(fileWithContent);
      targetsInfo.add(oneTarget);
    }
    return targetsInfo;
  }

  private List<String> getAllMetaData(String metafilename) {
    List<String> allMetaData = new ArrayList<String>();
    try {
      Closer closer = Closer.create();
      try{
        // Reader
        BufferedReader reader = closer.register(new BufferedReader(new FileReader(metafilename)));
        StringBuilder buffer = new StringBuilder();
        String s;
        while ((s = reader.readLine())!= null) buffer.append(s);

        // Получаем настройки
        String jsonMeta = buffer.toString();
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, String>>() {}.getType();
        HashMap<String, String> meta = gson.fromJson(jsonMeta, type);
        allMetaData.add(meta.get(ImmutableTikaWrapper.LANG_META));
        allMetaData.add(meta.get(ImmutableTikaWrapper.SOURCE_URL));

      } catch (Throwable e) { // must catch Throwable
        throw closer.rethrow(e);
      } finally {
        closer.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return allMetaData;
  }

  public void processOneNode(String node) {
    StringBuilder summaryContent = new StringBuilder();
    String pathToNodeInTmpFolder = ImmutableProcessorTargets.getPathToIndex()+"/"+AppConstants.TMP_FOLDER+"/"+node;
    List<List<String>> summaryMeta = new ArrayList<List<String>>();
    List<List<String>> targets = getTarget(pathToNodeInTmpFolder);

    for (List<String> target: targets) {
      try {  // внутри, чтобы не прервалась обработка из-за одного файла
        Closer readCloser = Closer.create();
        try {
          String lang = target.get(MinimalSpiderProcessor.IDX_LANG);
          String srcUrl = target.get(MinimalSpiderProcessor.IDX_SRC_URL);
          String tmpFilename = target.get(MinimalSpiderProcessor.IDX_TMP_FILE);
          summaryMeta.add(new ArrayList<String>(Arrays.asList(srcUrl, lang)));

          // Reader
          BufferedReader reader = readCloser.register(new BufferedReader(new FileReader(tmpFilename)));
          StringBuilder buffer = new StringBuilder();
          String s;
          while ((s = reader.readLine())!= null) buffer.append(s+'\n');

          // разбиваем не единицы и пишем
          // TODO(zaqwes) TOTH: maybi slow!
          // Убираем переносы строк - могут быть ошибки! Некоторые слова с дефисом могут быть перенес. по дуфису
          String dataForSplitting = buffer.toString().replace("-\n", "\n").replace('\n', ' ');

          BreakIterator bi = BreakIterator.getSentenceInstance();
          bi.setText(dataForSplitting);
          int index = 0;
          while (bi.next() != BreakIterator.DONE) {
            String sentence = dataForSplitting.substring(index, bi.current());
            String oneRecord = lang+' '+sentence+'\n';
            summaryContent.append(oneRecord);
            index = bi.current();
          }

        } catch (Throwable e) { // must catch Throwable
          throw readCloser.rethrow(e);
        } finally {
          readCloser.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      //break;  // DEVELOP
    }

    // Можно писать результат
    try {  // внутри, чтобы не прервалась обработка из-за одного файла
      Closer writeCloser = Closer.create();
      try {
        String path = ImmutableProcessorTargets.getPathToIndex()+"/"+ AppConstants.CONTENT_FOLDER+"/"+node;

        // записываем контент
        BufferedWriter contentOut = writeCloser.register(new BufferedWriter(new FileWriter(path+"/content.txt")));
        contentOut.write(summaryContent.toString());

        // записываем матеданные
        Gson gson = new Gson();
        BufferedWriter metaOut = writeCloser.register(new BufferedWriter(new FileWriter(path+"/meta.txt")));
        metaOut.write(gson.toJson(summaryMeta));
      } catch (Throwable e) { // must catch Throwable
        throw writeCloser.rethrow(e);
      } finally {
        writeCloser.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Main()
  public static void main(String [ ] args) {
    MinimalSpiderProcessor spiderProcessor = new MinimalSpiderProcessor();

    // Processing
    List<String> nodes = ImmutableBaseCoursor.getListNodes();

    // Обрабатываем каждый узел в отдельности
    for (String node : nodes) {
      //System.console().writer().println(node);
      print(node);
      spiderProcessor.processOneNode(node);
      //break;  // DEVELOP
    }
    System.out.print("Done\n");
  }
}


