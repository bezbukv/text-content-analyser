package frozen.crosscuttings.configurator;

// Сделать уточнения, хотя бы перелить в какой строке.
public class ConfigurationFileIsCorrupted extends Exception {
  public final String WHAT_HAPPENED;
  public ConfigurationFileIsCorrupted(Exception e) {
    super(e);
    WHAT_HAPPENED = e.getMessage();
  }

  public ConfigurationFileIsCorrupted(Exception e, String whatHappened) {
    super(e);
    WHAT_HAPPENED = whatHappened;
  }
}
