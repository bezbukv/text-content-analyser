package sand.third_party_tests.gae;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;
import com.googlecode.objectify.condition.IfNotZero;
import com.googlecode.objectify.condition.IfTrue;

@Entity
public class Person {
  @Id Long id;
  @Index String name;

  // The admin field is only indexed when it is true
  @Index(IfTrue.class) boolean admin;

  // You can provide multiple conditions, any of which will satisfy
  @Index({IfNotNull.class, IfNotZero.class}) Long serialNumber;

  private Person() {}
  public Person(String name) {this.name = name;}

  static Person create(String name) {
    return new Person(name);
  }
}