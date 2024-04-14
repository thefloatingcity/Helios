package city.thefloating.helios;

import java.time.Duration;

public final class Ticks {

  private Ticks() {
  }

  /**
   * Converts a duration to ticks.
   *
   * @param duration the duration
   * @return the duration in ticks
   */
  public static long in(final Duration duration) {
    return duration.toSeconds() * 20;
  }

  /**
   * Converts a duration to ticks. Casts {@link #in} to int.
   *
   * @param duration the duration
   * @return the duration in ticks
   */
  public static int inT(final Duration duration) {
    return (int) in(duration);
  }

}
