package xyz.tehbrian.floatyplugin.void_loop;

import org.bukkit.World;
import org.bukkit.event.Listener;

public final class VoidLoopUtil implements Listener {

  private VoidLoopUtil() {
  }

  // trouble understanding? no worries, I got you.
  // https://i.ibb.co/VtNXkSM/961-F66-F1-48-A7-4-E48-AEA4-6-AC469-E7-A052.jpg
  // mirror: https://i.imgur.com/OubxQoa.jpeg

  public static int lowEngage(final World.Environment environment) {
    return switch (environment) {
      case NETHER -> -100;
      case THE_END -> -170;
      default -> -260;
    };
  }

  public static int lowTeleport(final World.Environment environment) {
    return highEngage(environment) - 10;
  }

  public static int highEngage(final World.Environment environment) {
    return switch (environment) {
      case THE_END -> 460;
      case NETHER -> 350;
      default -> 470;
    };
  }

  public static int highTeleport(final World.Environment environment) {
    return lowEngage(environment) + 10;
  }

}
