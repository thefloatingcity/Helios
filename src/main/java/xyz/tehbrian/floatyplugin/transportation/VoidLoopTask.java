package xyz.tehbrian.floatyplugin.transportation;

import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.world.WorldService;

@SuppressWarnings({"ClassCanBeRecord", "unused"})
public final class VoidLoopTask {

  private final FloatyPlugin floatyPlugin;
  private final WorldService worldService;
  private final LangConfig langConfig;

  @Inject
  public VoidLoopTask(
      final @NonNull FloatyPlugin floatyPlugin,
      final @NonNull WorldService worldService,
      final @NonNull LangConfig langConfig
  ) {
    this.floatyPlugin = floatyPlugin;
    this.worldService = worldService;
    this.langConfig = langConfig;
  }

  public void start() {
    final Server server = this.floatyPlugin.getServer();
    final BukkitScheduler scheduler = server.getScheduler();

    scheduler.scheduleSyncRepeatingTask(this.floatyPlugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        final World.Environment environment = player.getWorld().getEnvironment();
        final Location location = player.getLocation();

        if (location.getY() < VoidLoopUtil.lowEngage(environment)) { // they too low
          scheduler.runTask(this.floatyPlugin, () -> {
            location.setY(VoidLoopUtil.lowTeleport(environment));
            final var oldVelocity = player.getVelocity();
            player.teleport(location);
            player.setVelocity(oldVelocity);
          });
        } else if (location.getY() > VoidLoopUtil.highEngage(environment)) { // they too high
          scheduler.runTask(this.floatyPlugin, () -> {
            location.setY(VoidLoopUtil.highTeleport(environment));
            final var oldVelocity = player.getVelocity();
            player.teleport(location);
            player.setVelocity(oldVelocity);
          });
        }  // they're in the non-epic zone
      }
    }, 0, 20);

    scheduler.scheduleSyncRepeatingTask(this.floatyPlugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        final float fallDistance = player.getFallDistance();

        if (fallDistance >= 4000) {
          player.showTitle(Title.title(
              this.langConfig.c(NodePath.path("warp", "max")),
              this.langConfig.c(NodePath.path("warp", "max_sub")),
              VoidLoopUtil.INSTANT_IN_TIMES
          ));

          scheduler.scheduleSyncDelayedTask(this.floatyPlugin,
              () -> {
                final Location spawnLocation = this.worldService.getPlayerSpawnLocation(
                    this.worldService.getFloatingWorld(player.getWorld()));

                player.setFallDistance(0);
                player.teleport(spawnLocation);

                player.getWorld().strikeLightningEffect(spawnLocation);
                player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, spawnLocation, 1);
                player.getWorld().playSound(
                    spawnLocation,
                    Sound.ENTITY_GENERIC_EXPLODE,
                    SoundCategory.MASTER,
                    4,
                    1
                );
              }, 5
          );
        } else if (fallDistance >= 3800) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "fourth")),
              VoidLoopUtil.FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 2) {
            scheduler.scheduleSyncDelayedTask(this.floatyPlugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2), i
            );
          }
        } else if (fallDistance >= 3400) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "third")),
              VoidLoopUtil.FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 5) {
            scheduler.scheduleSyncDelayedTask(this.floatyPlugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 1.6F), i
            );
          }
        } else if (fallDistance >= 3000) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "second")),
              VoidLoopUtil.FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 10) {
            scheduler.scheduleSyncDelayedTask(this.floatyPlugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 0.9F), i
            );
          }
        } else if (fallDistance >= 2000) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "first")),
              VoidLoopUtil.FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 20) {
            scheduler.scheduleSyncDelayedTask(this.floatyPlugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 0.5F), i
            );
          }
        }
      }
    }, 0, 100);
  }

}
