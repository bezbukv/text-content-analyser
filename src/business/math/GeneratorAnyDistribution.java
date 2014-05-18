package business.math;

import com.google.appengine.repackaged.org.apache.http.annotation.Immutable;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.ArrayList;
import java.util.Random;

// TODO: Вещь довольно законченная, пока расширять не хочу.
@Immutable
public final class GeneratorAnyDistribution {
  @Immutable  // TODO: Maybe
  public static final class DistributionElement implements Comparable<DistributionElement> {
    // http://stackoverflow.com/questions/5560176/is-integer-immutable
    public final Integer frequency;
    public final Boolean enabled;

    public DistributionElement(Integer freq, Boolean ena) {
      frequency = freq;
      enabled = ena;
    }

    @Override
    //@NotNull  // TODO: Какое-то предупреждение в idea ide
    public int compareTo(DistributionElement o2) {
      return frequency.compareTo(o2.frequency);
    }
  }

  private final Integer countPoints_;
  private final Integer maxValue_;
  private final ImmutableList<ImmutableList<Integer>> codeBook_;  // TODO: Это сохранится в gae storage?
  private final Integer INTERVAL_POS_ = 1;
  private final Integer IDX_POSITION_ = 2;

  // Любой список с числами
  // @throws: GeneratorDistributionExc
  public static GeneratorAnyDistribution create(ArrayList<DistributionElement> distribution) {
    if (distribution.isEmpty())
      throw new GeneratorDistributionExc("Input list must be no empty.");
    return new GeneratorAnyDistribution(distribution);
  }

  private GeneratorAnyDistribution(ArrayList<DistributionElement> distribution) {
    // TODO: Сделать или нет? Можно ли вызывать виртуальные функции.
    // Transpose
    ArrayList<Integer> transposed = new ArrayList<Integer>();
    for (DistributionElement elem: distribution)
      // TODO: Проблема!! Похоже нужно обнулять частоты. Иначе индексы собъются.
      // TODO: но как быть при выборке. while(not null)? Это может быть долго...
      // TODO: Стоп - нулевые не должны вообще выпадать!
      if (elem.enabled)
        transposed.add(elem.frequency);
      else {
        transposed.add(0);
      }

    // TODO: Now only enabled
    Triplet<ArrayList<Integer>, Integer, Integer> tupleFx = makeFx(transposed);
    ArrayList<Integer> Fx = tupleFx.getValue0();
    countPoints_ = tupleFx.getValue1();
    maxValue_ = tupleFx.getValue2();
    codeBook_ = ImmutableList.copyOf(makeRanges(Fx));
  }

  public Integer getPosition() {
    // Используется рекурсивная реализация на базе бинарного поиска.
    // На модели она показала наилучшую масштабирумость и скорость работы.
    Float value = new Random().nextFloat()* maxValue_;
    ImmutableList<Integer> result =  split(codeBook_, countPoints_, value).getValue1().get();
    return result.get(IDX_POSITION_);
  }

  private Triplet<ArrayList<Integer>, Integer, Integer> makeFx(ArrayList<Integer> distribution) {
    ArrayList<Integer> Fx = new ArrayList<Integer>();
    Integer Fxi = 0;
    Integer count = 0;
    for (final Integer frequency: distribution) {
      Fxi += frequency;
      Fx.add(Fxi);
      count++;
    }
    return Triplet.with(Fx, count, Fxi);
  }

  private ArrayList<ImmutableList<Integer>> makeRanges(ArrayList<Integer> Fx) {
    ArrayList<ImmutableList<Integer>> ranges = new ArrayList<ImmutableList<Integer>>();
    ranges.add(ImmutableList.of(0, Fx.get(0), 0));
    for (Integer i = 0; i < countPoints_ -1; i++) {
      ranges.add(ImmutableList.of(Fx.get(i), Fx.get(i+1), i+1));
    }
    return ranges;
  }

  private Pair<Boolean, Optional<ImmutableList<Integer>>> split(
    ImmutableList<ImmutableList<Integer>> ranges, Integer n, Float value) {
    Boolean contain = isContain(ranges, n, value);
    if (!contain) {
      Optional<ImmutableList<Integer>> none = Optional.absent();
      return Pair.with(false, none);
    }
    if (n == 1)
      return Pair.with(true, Optional.of(ranges.get(0)));
    else {
      Integer oneSize = n/2;
      Integer twoSize = n-oneSize;
      Pair<Boolean, Optional<ImmutableList<Integer>>> resultSubTree =
        split(ranges.subList(0, oneSize), oneSize, value);
      if (!resultSubTree.getValue0()) {
        resultSubTree = split(ranges.subList(oneSize, n), twoSize, value);
      }
      return resultSubTree;
    }
  }

  private Boolean isContain(ImmutableList<ImmutableList<Integer>> ranges, Integer n, Float value) {
    return ranges.get(0).get(0) < value
      && value <= ranges.get(n-1).get(1);
  }
}