package xyz.tehbrian.floatyplugin.world;

import com.google.inject.Inject;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import xyz.tehbrian.floatyplugin.world.backrooms.BackroomsGenerator;

public final class WorldService {

  private final JavaPlugin plugin;
  private final Logger logger;

  @Inject
  public WorldService(final JavaPlugin plugin, final Logger logger) {
    this.plugin = plugin;
    this.logger = logger;
  }

  public World getWorld(final FloatingWorld floatingWorld) {
    final @Nullable World world = this.plugin.getServer().getWorld(floatingWorld.bukkitName());
    if (world == null) {
      throw new RuntimeException("Floating world `" + floatingWorld.bukkitName() + "` doesn't exist.");
    }
    return world;
  }

  public FloatingWorld getFloatingWorld(final World world) {
    return switch (world.getName()) {
      case "madlands" -> FloatingWorld.MADLANDS;
      case "overworld" -> FloatingWorld.OVERWORLD;
      case "nether" -> FloatingWorld.NETHER;
      case "end" -> FloatingWorld.END;
      case "backrooms" -> FloatingWorld.BACKROOMS;
      default -> throw new IllegalArgumentException("Unknown world: " + world.getName());
    };
  }

  public void init() {
    this.createWorlds();
    this.setGameRules();
  }

  private void createWorlds() {
    for (final FloatingWorld floatingWorld : FloatingWorld.values()) {
      if (floatingWorld == FloatingWorld.MADLANDS) {
        continue; // madlands is created by the server as it is the level-name in server.properties
      }

      this.logger.info("Creating world {}", floatingWorld.bukkitName());
      final NamespacedKey key = new NamespacedKey(this.plugin, floatingWorld.bukkitName());

      final ChunkGenerator generator;
      if (floatingWorld == FloatingWorld.BACKROOMS) {
        generator = new BackroomsGenerator();
      } else {
        generator = new VoidGenerator();
      }

      this.plugin.getServer().createWorld(WorldCreator.ofKey(key)
          // prevent black horizon.
          // FLAT worlds turn black below Y-60; NORMAL worlds turn black below Y60.
          .type(WorldType.FLAT)
          .environment(floatingWorld.environment())
          .generator(generator)
      );
    }
  }

  private void setGameRules() {
    for (final World world : this.plugin.getServer().getWorlds()) {
      world.setGameRule(GameRule.SPAWN_RADIUS, 0);
      world.setGameRule(GameRule.DO_FIRE_TICK, false);
      world.setGameRule(GameRule.MOB_GRIEFING, false);

      // no mob spawning! >:(
      world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
      world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
      world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
      world.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
      world.setGameRule(GameRule.DO_INSOMNIA, false);
      world.setGameRule(GameRule.DISABLE_RAIDS, true);

      if (world.getEnvironment() == World.Environment.NETHER) {
        world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
        world.setGameRule(GameRule.KEEP_INVENTORY, false);
      } else {
        world.setGameRule(GameRule.REDUCED_DEBUG_INFO, false);
        world.setGameRule(GameRule.KEEP_INVENTORY, true);
      }
    }
  }

  public Location getPlayerSpawnLocation(final FloatingWorld floatingWorld) {
    final var worldSpawn = this.getSpawnLocation(floatingWorld);
    worldSpawn.add(0, 0, -3);
    worldSpawn.setPitch(3);
    return worldSpawn;
  }

  public Location getSpawnLocation(final FloatingWorld floatingWorld) {
    return this.getWorld(floatingWorld).getSpawnLocation().add(0.5, 0, 0.5);
  }

}
