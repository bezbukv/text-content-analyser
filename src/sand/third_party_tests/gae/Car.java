package sand.third_party_tests.gae;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Car {
  @Id Long id;
  @Index String license;  // для фильтрации нужно пометить
  int color; // public?
  //@Load List<Ref<Engine>> owners = new ArrayList<Ref<Engine>>();
}