package xyz.tehbrian.floatyplugin.void_loop;

import org.bukkit.event.Listener;
import xyz.tehbrian.floatyplugin.realm.Habitat;

/**
 * Provides the positions at which the void loop will engage for each habitat.
 */
public final class VoidLoopPositions implements Listener {

  private VoidLoopPositions() {
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
