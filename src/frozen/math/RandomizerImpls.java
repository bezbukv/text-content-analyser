package frozen.math;

import java.util.Random;

public class RandomizerImpls {
  private RandomizerImpls() {}

  public static Randomizer create(Integer maxValue) {
    return new UniformRandomizer(maxValue);
  }

  private static class UniformRandomizer implements Randomizer {
    public UniformRandomizer(Integer maxValue) {
      MAX_VALUE = maxValue;
    }
    private final Integer MAX_VALUE;
    private final Random random_ = new Random();

    @Override
    public Integer getSample() {
      return random_.nextInt(MAX_VALUE);
    }
  }
}