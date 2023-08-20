package city.thefloating.floatyplugin.ascension;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.Duration;
import java.util.Locale;

public enum Rank {
  GROUNDED(null),
  MAD(null),      // /rules accept if madlands enabled.
  BOARDING(null), // /rules accept if madlands disabled, otherwise, manual.
  PASSENGER(Duration.ofHours(1)),
  NAVIGATOR(Duration.ofHours(10)),
  PILOT(Duration.ofHours(30)),
  CAPTAIN(Duration.ofHours(75)),
  ASTRONAUT(Duration.ofHours(150));

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
