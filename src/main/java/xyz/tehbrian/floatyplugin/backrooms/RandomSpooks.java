package xyz.tehbrian.floatyplugin.backrooms;

import com.google.inject.Inject;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.Ticks;
import xyz.tehbrian.floatyplugin.realm.Realm;
import xyz.tehbrian.floatyplugin.realm.WorldService;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomSpooks {

  private static final Random RANDOM = ThreadLocalRandom.current();

  private static final Duration JITTER_COOLDOWN = Duration.ofSeconds(269);
  private final Map<UUID, Instant> lastJitter = new HashMap<>();

  private final FloatyPlugin plugin;
  private final WorldService worldService;

  @Inject
  public RandomSpooks(
      final FloatyPlugin plugin,
      final WorldService worldService
  ) {
    this.plugin = plugin;
    this.worldService = worldService;
  }

  public void start() {
    final Server server = this.plugin.getServer();
    final BukkitScheduler scheduler = server.getScheduler();

    // slowed down cave noises.
    scheduler.scheduleSyncRepeatingTask(this.plugin, () -> {
      final World backrooms = this.worldService.getWorld(Realm.BACKROOMS);
      for (final Player player : backrooms.getPlayers()) {
        if (RANDOM.nextFloat() < 0.2F) {
          player.playSound(Sound.sound(org.bukkit.Sound.AMBIENT_CAVE, Sound.Source.MASTER, 10F, 0.6F));
        }
      }
    }, 1, Ticks.in(Duration.ofSeconds(29)));

    // high-pitched noise and jitter camera.
    scheduler.scheduleSyncRepeatingTask(this.plugin, () -> {
      final World backrooms = this.worldService.getWorld(Realm.BACKROOMS);
      final var now = Instant.now();
      for (final Player player : backrooms.getPlayers()) {
        if (RANDOM.nextFloat() > 0.1F) {
          continue; // random chance.
        }

        if (player.getLocation().getBlock().getLightLevel() >= 4) {
          continue; // player must be in dark area.
        }

        if (this.lastJitter.containsKey(player.getUniqueId())
            && Duration
            .between(now, this.lastJitter.get(player.getUniqueId()))
            .compareTo(JITTER_COOLDOWN) < 0) {
          continue; // not too often.
        }

        player.playSound(Sound.sound(
            org.bukkit.Sound.ENTITY_GHAST_SCREAM,
            Sound.Source.MASTER,
            100,
            RANDOM.nextFloat(0.7F, 1.9F)
        ));
        final var loc = player.getLocation();
        loc.setYaw(loc.getYaw() + RANDOM.nextInt(-10, 10));
        loc.setPitch(loc.getPitch() + RANDOM.nextInt(-10, 10));
        player.teleport(loc);

        this.lastJitter.put(player.getUniqueId(), Instant.now());
      }
    }, 1, Ticks.in(Duration.ofSeconds(6)));

    // temporary teleport to black area and slower noise.
    scheduler.runTaskTimer(this.plugin, () -> {
      final World backrooms = this.worldService.getWorld(Realm.BACKROOMS);
      for (final Player player : backrooms.getPlayers()) {
        if (RANDOM.nextFloat() > 0.05F) {
          continue;
        }

        player.playSound(Sound.sound(
            org.bukkit.Sound.ENTITY_ALLAY_HURT,
            Sound.Source.MASTER,
            100,
            RANDOM.nextFloat(0.5F, 0.6F)
        ));

        final var previous = player.getLocation();
        final var up = previous.clone().add(0, 50, 0);

        player.teleport(up);
        scheduler.runTaskLater(this.plugin, () -> player.teleport(previous), 7);
      }
    }, 1, Ticks.in(Duration.ofSeconds(57)) - 7);
  }

}
