package city.thefloating.floatyplugin.void_loop;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.Ticks;
import city.thefloating.floatyplugin.config.LangConfig;
import city.thefloating.floatyplugin.realm.Realm;
import city.thefloating.floatyplugin.realm.Transposer;
import city.thefloating.floatyplugin.realm.WorldService;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

  private final FloatyPlugin plugin;
  private final WorldService worldService;
  private final LangConfig langConfig;
  private final Transposer transposer;

  @Inject
  public WarpTask(
      final FloatyPlugin plugin,
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
    // random chance to noclip into the backrooms. 10% chance.
    if (RANDOM.nextFloat() > 0.9) {
      final Location nextLocation = this.transposer.getNextLocation(player, Realm.BACKROOMS);
      this.transposer.transpose(player, Realm.BACKROOMS);

      player.addPotionEffect(new PotionEffect(
          PotionEffectType.BLINDNESS,
          Ticks.inT(Duration.ofSeconds(3)),
          10,
          true,
          false,
          false
      ));
      player.getWorld().spawnParticle(Particle.SMOKE_LARGE, nextLocation, 40, 2, 2, 2);
      player.playSound(nextLocation, Sound.BLOCK_PORTAL_TRAVEL, SoundCategory.MASTER, 4, 1);
    } else {
      final Location spawn = this.worldService.getSpawnPoint(Realm.from(player.getWorld()));

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

    scheduler.scheduleSyncRepeatingTask(this.plugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        final float fallDistance = player.getFallDistance();
        if (fallDistance >= 4000) {
          scheduler.scheduleSyncDelayedTask(this.plugin, () -> warpPlayer(player), 5);
        } else if (fallDistance >= 3800) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "fourth")),
              FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 2) {
            scheduler.scheduleSyncDelayedTask(this.plugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2), i
            );
          }
        } else if (fallDistance >= 3400) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "third")),
              FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 5) {
            scheduler.scheduleSyncDelayedTask(this.plugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 1.6F), i
            );
          }
        } else if (fallDistance >= 3000) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "second")),
              FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 10) {
            scheduler.scheduleSyncDelayedTask(this.plugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 0.9F), i
            );
          }
        } else if (fallDistance >= 2000) {
          player.showTitle(Title.title(
              Component.empty(),
              this.langConfig.c(NodePath.path("warp", "first")),
              FLASHING_TIMES
          ));

          final Location location = player.getLocation();
          for (int i = 0; i < 100; i = i + 20) {
            scheduler.scheduleSyncDelayedTask(this.plugin,
                () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 0.5F), i
            );
          }
        }
      }
    }, 0, Ticks.in(Duration.ofSeconds(5)));
  }

}
