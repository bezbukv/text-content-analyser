package sand.third_party_tests.gae.relationships;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

// One-to-one
@Entity
public class PersonRel
{
  @Id
  Long id;
  @Index
  String name;
  Key<PersonRel> significantOther;
}
