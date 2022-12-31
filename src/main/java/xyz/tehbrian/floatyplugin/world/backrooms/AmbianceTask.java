package xyz.tehbrian.floatyplugin.world.backrooms;

import com.google.inject.Inject;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.world.FloatingWorld;
import xyz.tehbrian.floatyplugin.world.WorldService;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class AmbianceTask {

  private static final Random RANDOM = ThreadLocalRandom.current();

  private static final Duration SPOOK_COOLDOWN = Duration.ofSeconds(269);
  private final Map<UUID, Instant> lastSpook = new HashMap<>();

  private final FloatyPlugin plugin;
  private final WorldService worldService;

  @Inject
  public AmbianceTask(
      final FloatyPlugin plugin,
      final WorldService worldService
  ) {
    this.plugin = plugin;
    this.worldService = worldService;
  }

  public void start() {
    final Server server = this.plugin.getServer();

    final var oneTick = 1;
    final var oneSecond = oneTick * 20;

    // random noises.
    server.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
      final World backrooms = this.worldService.getWorld(FloatingWorld.BACKROOMS);
      for (final Player player : backrooms.getPlayers()) {
        if (RANDOM.nextFloat() < 0.2F) {
          player.playSound(Sound.sound(org.bukkit.Sound.AMBIENT_CAVE, Sound.Source.MASTER, 10F, 0.6F));
        }
      }
    }, 1, 29 * oneSecond);

    // spook.
    server.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
      final World backrooms = this.worldService.getWorld(FloatingWorld.BACKROOMS);
      final var now = Instant.now();
      for (final Player player : backrooms.getPlayers()) {
        if (RANDOM.nextFloat() > 0.1F) {
          continue; // random chance.
        }

        if (player.getLocation().getBlock().getLightLevel() >= 4) {
          continue; // player must be in dark area.
        }

        if (this.lastSpook.containsKey(player.getUniqueId())
            && Duration
            .between(now, this.lastSpook.get(player.getUniqueId()))
            .compareTo(SPOOK_COOLDOWN) < 0) {
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

        this.lastSpook.put(player.getUniqueId(), Instant.now());
      }
    }, 1, 5 * oneSecond);
  }

}
