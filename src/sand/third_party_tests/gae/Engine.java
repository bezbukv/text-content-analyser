package sand.third_party_tests.gae;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Engine {
  @Id Long id;
  @Index String name;

  public Engine() {}
  public Engine(String name) { this.name = name; }
}
