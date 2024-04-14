package city.thefloating.helios.realm;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.Locale;

/**
 * The "worlds" that exist in The Floating City.
 * <p>
 * Each realm is tied to a distinct Minecraft world.
 */
public enum Realm {
  MADLANDS(Milieu.CANON, Habitat.WHITE), // standard for griefers.
  OVERWORLD(Milieu.CANON, Habitat.WHITE), // standard.
  NETHER(Milieu.ONEROUS, Habitat.RED), // hellishly difficult.
  END(Milieu.DOCILE, Habitat.BLACK), // carefree. allows elytras and ender pearls.
  BACKROOMS(Milieu.SPOOKY, Habitat.WHITE); // spooky.

  private final Milieu milieu;
  private final Habitat habitat;

  Realm(final Milieu milieu, final Habitat habitat) {
    this.milieu = milieu;
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

  public static Realm of(final Location location) {
    return from(location.getWorld());
  }

  public static Realm of(final Entity entity) {
    return from(entity.getWorld());
  }

  @Override
  public String toString() {
    return this.name().toLowerCase(Locale.ROOT);
  }

  public Milieu milieu() {
    return this.milieu;
  }

  public Habitat habitat() {
    return this.habitat;
  }
}
