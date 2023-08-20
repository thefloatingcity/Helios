package city.thefloating.floatyplugin.realm;

import org.bukkit.World;

/**
 * The looks that a Minecraft world can have. Essentially
 * {@link org.bukkit.World.Environment} but with different names to avoid
 * name conflicts with {@link Realm}.
 */
public enum Habitat {
  WHITE,
  RED,
  BLACK;

  public static Habitat from(final World.Environment env) {
    return switch (env) {
      case NORMAL, CUSTOM -> Habitat.WHITE;
      case NETHER -> Habitat.RED;
      case THE_END -> Habitat.BLACK;
    };
  }

  public static Habitat of(final World world) {
    return from(world.getEnvironment());
  }

  public World.Environment environment() {
    return switch (this) {
      case WHITE -> World.Environment.NORMAL;
      case RED -> World.Environment.NETHER;
      case BLACK -> World.Environment.THE_END;
    };
  }
}
