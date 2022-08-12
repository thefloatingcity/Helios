package xyz.tehbrian.floatyplugin.world;

import org.bukkit.World;

public enum FloatingWorld {
  MADLANDS("madlands", World.Environment.NORMAL),
  OVERWORLD("overworld", World.Environment.NORMAL),
  NETHER("nether", World.Environment.NETHER),
  END("end", World.Environment.THE_END),
  BACKROOMS("backrooms", World.Environment.NORMAL);

  private final String bukkitName;
  private final World.Environment environment;

  FloatingWorld(final String name, final World.Environment environment) {
    this.bukkitName = name;
    this.environment = environment;
  }

  public String bukkitName() {
    return this.bukkitName;
  }

  public World.Environment environment() {
    return this.environment;
  }
}
