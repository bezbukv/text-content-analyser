package sand.third_party_tests.gae;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

@Entity
public class CarOneToOne {
  @Load
  @Parent
  Ref<Person> owner;
  public Person getOwner() { return owner.get(); }
  public void setOwner(Person value) { owner = Ref.create(value); }

  // Coupling
  // TODO: Why?
  @Load
  Ref<Engine> engine;
  public Engine getEngine() { return engine.get(); }
  public void setEngine(Engine value) { engine = Ref.create(value); }


  @Id
  Long id;

  @Index
  String vin;
  int color;
  //byte[] rawData;
  //@Ignore int irrelevant;


  private CarOneToOne() {}

  public CarOneToOne(String vin, int color) {
    this.vin = vin;
    this.color = color;
  }

  public static CarOneToOne create(String vin, int color, Person value, Engine engine) {
    CarOneToOne thing = new CarOneToOne(vin, color);
    thing.setOwner(value);
    thing.setEngine(engine);
    return thing;
  }


}