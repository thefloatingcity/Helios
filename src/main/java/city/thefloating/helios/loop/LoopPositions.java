package city.thefloating.helios.loop;

import city.thefloating.helios.realm.Habitat;
import org.bukkit.event.Listener;

/**
 * Provides the positions at which the void loop will engage for each habitat.
 * <p>
 * These positions are a function of the habitat due to the habitats' different
 * fog distances/visual block cutoffs.
 */
public final class LoopPositions implements Listener {

  private LoopPositions() {
  }

  // trouble understanding? no worries, I got you. here's a drawing.
  // https://i.imgur.com/OubxQoa.jpeg

  public static int lowEngage(final Habitat habitat) {
    return switch (habitat) {
      case WHITE -> -250;
      case RED -> -100;
      case BLACK -> -180;
    };
  }

  public static int lowTo(final Habitat habitat) {
    return highEngage(habitat) - 10;
  }

  public static int highEngage(final Habitat habitat) {
    return switch (habitat) {
      case WHITE -> 510;
      case RED -> 350;
      case BLACK -> 440;
    };
  }

  public static int highTo(final Habitat habitat) {
    return lowEngage(habitat) + 10;
  }

}
