package core.adapters_3rdparty;

import com.google.common.base.Joiner;
import com.google.common.io.Closer;
import com.google.gson.Gson;
import common.Tools;
import frozen.crosscuttings.AppConstants;
import frozen.jobs_processors.ProcessorTargets;
import frozen.spiders_extractors.ExtractorException;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.language.ProfilingWriter;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final public class TikaWrapper {
  // Fields
  public static final String LANG_META = "lang";
  public static final String SOURCE_URL = "src_url";

  public static void extractAndSaveText(List<String> target) throws ExtractorException {
    String nodeName = target.get(ProcessorTargets.RESULT_NODE_NAME);
    String pathToSrcFile = target.get(ProcessorTargets.RESULT_PATH);
    String inFileName = target.get(ProcessorTargets.RESULT_FILENAME);
    try {
      // Настраиваем пути
      String fullOutFilenameNoExt = Joiner.on("/")
          .join(Arrays.asList(
            ProcessorTargets.getPathToIndex(),
            AppConstants.TMP_FOLDER,
            nodeName,
            inFileName));

      // имя файла старое! для сохр. нужно добавть *.ptxt or *.meta
      String outFileNameRaw = fullOutFilenameNoExt+AppConstants.PURGED_TXT_FILE_EXT;
      String fullNameSourceFile = Joiner.on("/")
          .join(Arrays.asList(
            pathToSrcFile,
            inFileName));

      // TODO(zaqwes): Как установить читабельными русски буквы?
      Tools.print("Process file: " + fullNameSourceFile);

      // Извлекаем содержимое файла
      Closer closer = Closer.create();
      try {
        File file = new File(fullNameSourceFile);
        URL url;
        if (file.isFile()) {
          url = file.toURI().toURL();
        } else {
          url = new URL("file:///"+fullNameSourceFile);
        }
        ParseContext context = new ParseContext();
        Detector detector = new DefaultDetector();
        Parser parser = new AutoDetectParser(detector);
        context.set(Parser.class, parser);
        Metadata metadata = new Metadata();
        InputStream in = closer.register(TikaInputStream.get(url, metadata));
        OutputStream out = closer.register(new FileOutputStream(new File(outFileNameRaw)));
        ContentHandler handler = new BodyContentHandler(out);
        parser.parse(in, handler, metadata, context);

      // Обработка ошибок
      } catch (SAXException e) {
        // LOG()
        throw new ExtractorException("Error on parse file "+fullNameSourceFile);
      } catch (TikaException e) {
        // LOG()
        throw new ExtractorException("Error on parse file "+fullNameSourceFile);
      } catch (Throwable e) { // must catch Throwable
        throw closer.rethrow(e);
      } finally {
        closer.close();
      }
    } catch (IOException e) {
      throw new ExtractorException("Error on parse file. I/O error.");
    }
  }

  // {url: path_to_file, lang: ru}
  public static String extractAndSaveMetadata(List<String> target) {
    String nodeName = target.get(ProcessorTargets.RESULT_NODE_NAME);
    String onlyPathPartSourceFilename = target.get(ProcessorTargets.RESULT_PATH);
    String onlyNameSourceFile = target.get(ProcessorTargets.RESULT_FILENAME);
    try {
      // Настраиваем пути
      String fullOutFilenameNoExt = Joiner.on("/")
        .join(Arrays.asList(
          ProcessorTargets.getPathToIndex(),
          AppConstants.TMP_FOLDER,
          nodeName,
          onlyNameSourceFile));

      // имя файла старое! для сохр. нужно добавть *.ptxt or *.meta
      String nameFileWithContent = fullOutFilenameNoExt+AppConstants.PURGED_TXT_FILE_EXT;
      String sourceFilename = Joiner.on("/")
        .join(Arrays.asList(
          onlyPathPartSourceFilename,
          onlyNameSourceFile));

      // Определяем язык - приходится читать файл еще раз, но по другому пока не ясно как
      //   язык определяется как литовский
      Closer postprocessCloser = Closer.create();
      try {
        BufferedReader inExtractedFile = postprocessCloser.register(
          new BufferedReader(new FileReader(nameFileWithContent)));
        ProfilingWriter writer = new ProfilingWriter();
        String s;
        while ((s = inExtractedFile.readLine()) != null) writer.append(s);
        LanguageIdentifier identifier = writer.getLanguage();
        String lang = identifier.getLanguage();

        // Заполняем метадынные файла
        Map<String, String> meta = new HashMap<String, String>();
        meta.put(LANG_META, lang);
        meta.put(SOURCE_URL, sourceFilename);

        // Запись
        String metaFileName = fullOutFilenameNoExt+AppConstants.META_FILE_EXT;
        PrintWriter metaOut = postprocessCloser.register(
          new PrintWriter(
            new BufferedWriter(
              new FileWriter(metaFileName))));

        Gson gson = new Gson();
        metaOut.println(gson.toJson(meta));
        return lang;

      } catch (Throwable e) { // must catch Throwable
        throw postprocessCloser.rethrow(e);
      } finally {
        postprocessCloser.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
      return "error occure";
    }
  }
}