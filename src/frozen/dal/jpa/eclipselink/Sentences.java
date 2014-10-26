package frozen.dal.jpa.eclipselink;

import javax.persistence.*;

@Entity
public class Sentences {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer hashSentence;
  private String sentence;

  public String getSentence() {
    return sentence;
  }

  public void setSentence(String sentence) {
    this.sentence = sentence;
  }

  public Integer getHashSentence() {
    return hashSentence;
  }

  public void setHashSentence(Integer hashSentence) {
    this.hashSentence = hashSentence;
  }

  @Override
  public String toString() {
    return "Sentence ["+hashSentence+", "+sentence+"]";
  }
}
