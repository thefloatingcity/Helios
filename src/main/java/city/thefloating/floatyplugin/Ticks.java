package city.thefloating.floatyplugin;

import java.time.Duration;

public final class Ticks {

  private Ticks() {
  }

  public static long in(final Duration duration) {
    return duration.toSeconds() * 20;
  }

  /**
   * Casts {@link #in} to int.
   */
  public static int inT(final Duration duration) {
    return (int) (duration.toSeconds() * 20);
  }

}
