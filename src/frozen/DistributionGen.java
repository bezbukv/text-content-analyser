package frozen;


import core.math.DistributionElement;

import java.util.ArrayList;

/**
 * About: Генерирует последовательность 0-N по заданному закону распределения.
 * Позволяет исключать точки из генерируемой последовательности.
 */

//@Entity
@Deprecated  // пока не ясно как сохранять, возможно потом пригодится.
public interface DistributionGen {

  public Integer getPosition();

  // TODO: Но нужно ли? Может сделать создание в конструкторе?
  @Deprecated
  public void reloadGenerator(ArrayList<DistributionElement> distribution);

  public void disablePoint(Integer idx);
  public void enablePoint(Integer idx);

  // TODO: Получать распределение, иначе как узнаем как разрешить точку обратно.
}