package xyz.tehbrian.floatyplugin.ascension;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.Duration;
import java.util.Locale;

public enum Rank {
  BOARDING(null),
  PASSENGER(null),
  NAVIGATOR(Duration.ofHours(1)),
  PILOT(Duration.ofHours(5)),
  CAPTAIN(Duration.ofHours(25)),
  ASTRONAUT(Duration.ofHours(75));

  private final @Nullable Duration playtimeRequired;

  Rank(final @Nullable Duration playtimeRequired) {
    this.playtimeRequired = playtimeRequired;
  }

  /**
   * Gets the time required to ascend to this rank. If null, the rank is not
   * attainable through time and must be gotten through alternative methods.
   *
   * @return the time required to ascend
   */
  public @Nullable Duration playtimeRequired() {
    return this.playtimeRequired;
  }

  public static Rank from(final String name) {
    try {
      return Rank.valueOf(name.toUpperCase(Locale.ROOT));
    } catch (final IllegalArgumentException e) {
      throw new IllegalStateException("Unknown rank: " + name);
    }
  }

}
