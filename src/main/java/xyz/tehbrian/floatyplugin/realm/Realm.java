package xyz.tehbrian.floatyplugin.realm;

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

  @Override
  public String toString() {
    return this.name().toLowerCase(Locale.ROOT);
  }

  public Habitat habitat() {
    return this.habitat;
  }
}
