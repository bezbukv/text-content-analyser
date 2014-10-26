package frozen;

import core.math.DistributionElement;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;

/**
 * Created by zaqwes on 5/15/14.
 */
@Deprecated
@Entity
public class DistributionGenFake implements DistributionGen {
  /// Persist
  @Id
  Long id;

  /// Own
  @Override
  public Integer getPosition() {
    return 0;
  }

  @Override
  public void reloadGenerator(ArrayList<DistributionElement> distribution) { }

  @Override
  public void disablePoint(Integer idx) {
    // TODO: Проверка границ - это явно ошибка

    // TODO: Похоже нужна non-XG - транзакция. Кажется может возникнуть исключение.
  }

  @Override
  public void enablePoint(Integer idx) {

  }
}
