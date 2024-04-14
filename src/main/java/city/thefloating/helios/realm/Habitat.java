package city.thefloating.helios.realm;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

/**
 * The looks that a Minecraft world can have.
 * <p>
 * Essentially {@link org.bukkit.World.Environment} but with different names
 * to avoid conflicts with {@link Realm}.
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

  public static Habitat of(final Location location) {
    return of(location.getWorld());
  }

  public static Habitat of(final Entity entity) {
    return of(entity.getWorld());
  }

  public World.Environment environment() {
    return switch (this) {
      case WHITE -> World.Environment.NORMAL;
      case RED -> World.Environment.NETHER;
      case BLACK -> World.Environment.THE_END;
    };
  }
}
