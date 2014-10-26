package frozen.dal.jpa.eclipselink;

import javax.persistence.*;

@Entity
public class Words {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Integer id;
  private String value;

  //private Set<Sentences> sentences = new HashSet<Sentences>();

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String description) {
    this.value = description;
  }

  //@OneToMany
  //public Set<Sentences> getSentences() {
  //  return sentences;
  //}
}
