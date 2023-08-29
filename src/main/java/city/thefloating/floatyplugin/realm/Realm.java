package city.thefloating.floatyplugin.realm;

import org.bukkit.World;

import java.util.Locale;

/**
 * The "worlds" that exist in The Floating City.
 */
public enum Realm {
  MADLANDS(Habitat.WHITE),
  OVERWORLD(Habitat.WHITE),
  NETHER(Habitat.RED),
  END(Habitat.BLACK),
  BACKROOMS(Habitat.WHITE);

  private final Habitat habitat;

  Realm(final Habitat habitat) {
    this.habitat = habitat;
  }

  public static Realm from(final World world) {
    return switch (world.getName()) {
      case "madlands" -> Realm.MADLANDS;
      case "overworld" -> Realm.OVERWORLD;
      case "nether" -> Realm.NETHER;
      case "end" -> Realm.END;
      case "backrooms" -> Realm.BACKROOMS;
      default -> throw new RuntimeException("Could not find realm for world `" + world.getName() + "`.");
    };
  }

  @Override
  public String toString() {
    return this.name().toLowerCase(Locale.ROOT);
  }

  public Habitat habitat() {
    return this.habitat;
  }
}
