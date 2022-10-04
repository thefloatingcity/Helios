package xyz.tehbrian.floatyplugin.voidloop;

import net.kyori.adventure.title.Title;
import org.bukkit.World;
import org.bukkit.event.Listener;

import java.time.Duration;

public final class VoidLoopUtil implements Listener {

  public static final Title.Times INSTANT_IN_TIMES = Title.Times.times(
      Duration.ZERO,
      Duration.ofSeconds(4),
      Duration.ofSeconds(1)
  );
  public static final Title.Times FLASHING_TIMES = Title.Times.times(
      Duration.ofSeconds(1),
      Duration.ofSeconds(3),
      Duration.ofSeconds(1)
  );

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
