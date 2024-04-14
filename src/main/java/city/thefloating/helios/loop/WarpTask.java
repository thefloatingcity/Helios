package city.thefloating.helios.loop;

import city.thefloating.helios.Helios;
import city.thefloating.helios.Ticks;
import city.thefloating.helios.config.LangConfig;
import city.thefloating.helios.realm.Realm;
import city.thefloating.helios.realm.Transposer;
import city.thefloating.helios.realm.WorldService;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.spongepowered.configurate.NodePath;

import java.time.Duration;
import java.util.Random;

public final class WarpTask {

  private static final Title.Times INSTANT_IN_TIMES = Title.Times.times(
      Duration.ZERO,
      Duration.ofSeconds(4),
      Duration.ofSeconds(1)
  );
  private static final Title.Times FLASHING_TIMES = Title.Times.times(
      Duration.ofSeconds(1),
      Duration.ofSeconds(3),
      Duration.ofSeconds(1)
  );

  private static final Random RANDOM = new Random();

  private final Helios plugin;
  private final WorldService worldService;
  private final LangConfig langConfig;
  private final Transposer transposer;

  @Inject
  public WarpTask(
      final Helios plugin,
      final WorldService worldService,
      final LangConfig langConfig,
      final Transposer transposer
  ) {
    this.plugin = plugin;
    this.worldService = worldService;
    this.langConfig = langConfig;
    this.transposer = transposer;
  }

  private void warpPlayer(final Player player) {
    // random chance to noclip into the backrooms. 15% chance.
    if (RANDOM.nextFloat() < 0.15) {
      this.transposer.noclipIntoBackrooms(player);
    } else {
      final Location spawn = this.worldService.ornateSpawn(Realm.of(player));

      player.showTitle(Title.title(
          this.langConfig.c(NodePath.path("warp", "max")),
          this.langConfig.c(NodePath.path("warp", "max-sub")),
          INSTANT_IN_TIMES
      ));

      player.setFallDistance(0);
      player.teleport(spawn);

      player.getWorld().strikeLightningEffect(spawn);
      player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, spawn, 1);
      player.getWorld().playSound(spawn, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 4, 1);
    }
  }

  public void start() {
    final Server server = this.plugin.getServer();
    final BukkitScheduler scheduler = server.getScheduler();
    scheduler.runTaskTimer(this.plugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        final float fallDistance = player.getFallDistance();
        if (fallDistance >= 6000) {
          scheduler.runTaskLater(this.plugin, () -> this.warpPlayer(player), 5);
        } else if (fallDistance >= 5800) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "fourth")),
              FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 2) {
            scheduler.runTaskLater(this.plugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2), i
            );
          }
        } else if (fallDistance >= 5400) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "third")),
              FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 5) {
            scheduler.runTaskLater(this.plugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 1.6F), i
            );
          }
        } else if (fallDistance >= 5000) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "second")),
              FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 10) {
            scheduler.runTaskLater(this.plugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 0.9F), i
            );
          }
        } else if (fallDistance >= 4000) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "first")),
              FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 20) {
            scheduler.runTaskLater(this.plugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 0.5F), i
            );
          }
        }
      }
    }, 0, Ticks.in(Duration.ofSeconds(5)));
  }

}
