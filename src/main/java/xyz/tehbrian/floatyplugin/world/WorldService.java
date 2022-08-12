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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public final class WorldService {

  private final JavaPlugin plugin;
  private final Logger logger;

  @Inject
  public WorldService(final @NonNull JavaPlugin plugin, final @NonNull Logger logger) {
    this.plugin = plugin;
    this.logger = logger;
  }

  public @NonNull World getWorld(final FloatingWorld floatingWorld) {
    final @Nullable World world = this.plugin.getServer().getWorld(floatingWorld.bukkitName());
    if (world == null) {
      throw new RuntimeException("Tried to get the world by name, but it didn't exist.");
    }
    return world;
  }

  public @NonNull FloatingWorld getFloatingWorld(final World world) {
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
      final @NonNull NamespacedKey key = new NamespacedKey(this.plugin, floatingWorld.bukkitName());

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

  public @NonNull Location getPlayerSpawnLocation(final FloatingWorld floatingWorld) {
    final var worldSpawn = this.getSpawnLocation(floatingWorld);
    return switch (floatingWorld) {
      case MADLANDS, OVERWORLD, NETHER, END -> {
        worldSpawn.add(0, 0, -3);
        worldSpawn.setPitch(3);
        yield worldSpawn;
      }
      case BACKROOMS -> worldSpawn;
    };
  }

  public @NonNull Location getSpawnLocation(final FloatingWorld floatingWorld) {
    return this.getWorld(floatingWorld).getSpawnLocation().add(0.5, 0, 0.5);
  }

  public @NonNull ChunkGenerator getDefaultWorldGenerator(
      @NotNull final String worldName,
      @Nullable final String id
  ) {
    return new VoidGenerator();
  }

}
