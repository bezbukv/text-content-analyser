package crosscuttings;

// Сделать уточнения, хотя бы перелить в какой строке.
public class ConfFileIsCorrupted extends Exception {
  public ConfFileIsCorrupted(Exception e) {
    super(e);
  }
}
