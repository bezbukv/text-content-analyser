package frozen;

// Очень плохая инкапсуляция, часть перенести бы в конфигурационный файл,
//   или создать файл метаданных - yaml формата например
public class GlobalConstants {
  private GlobalConstants() {}

  // TODO(zaqwes): Перенести к конфигурационный файл
  public final static java.lang.String SPIDER_TARGETS_FILENAME = "apps/targets/spider_extractor_target.txt";
  public final static java.lang.String SPIDER_TARGETS_FILENAME_GLOBAL = "apps/targets/spider_extractor_target.json";

  public final static java.lang.String APP_CFG_FULL_FILENAME = "apps/cfgs/app_cfg.yaml";

  // Index folders
  public final static java.lang.String CONTENT_FOLDER = "content";
  public final static java.lang.String TMP_FOLDER = "tmp";
  public final static java.lang.String COMPRESSED_IDX_FOLDER = "compressed_freq_index";

  // TODO(zaqwes): Сокрыть в классе доступа к индексу.
  public final static java.lang.String CONTENT_FILENAME = "content.txt";
  public final static java.lang.String CONTENT_META_FILENAME = "meta.txt";
  public final static java.lang.String STATIC_NOTES_FILENAME = "static_notes.txt";
  public final static java.lang.String SORTED_IDX_FILENAME = "sorted.txt";
  public final static java.lang.String FREQ_IDX_FILENAME = "frequences.txt";
  public final static java.lang.String FILENAME_REST_IDX = "rest.txt";
  public final static java.lang.String FILENAME_SENTENCES_IDX = "sentences.txt";

  //
  public final static java.lang.String PURGED_TXT_FILE_EXT = ".ptxt";
  public final static java.lang.String META_FILE_EXT = ".meta";
  public final static java.lang.String PATH_SPLITTER = "/";  // *nix splitter
}
