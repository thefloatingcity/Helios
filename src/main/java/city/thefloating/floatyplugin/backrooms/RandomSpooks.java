package city.thefloating.floatyplugin.backrooms;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.Ticks;
import city.thefloating.floatyplugin.realm.Realm;
import city.thefloating.floatyplugin.realm.WorldService;
import com.google.inject.Inject;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomSpooks {

  private static final Random RANDOM = ThreadLocalRandom.current();

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
      for (final Player player : this.worldService.getWorld(Realm.BACKROOMS).getPlayers()) {
        if (RANDOM.nextFloat() < 0.2F) {
          player.playSound(Sound.sound(org.bukkit.Sound.AMBIENT_CAVE, Sound.Source.MASTER, 10F, 0.6F));
        }
      }
    }, 1, Ticks.in(Duration.ofSeconds(29)));

    // high-pitched noise and jitter camera.
    scheduler.scheduleSyncRepeatingTask(this.plugin, () -> {
      for (final Player player : this.worldService.getWorld(Realm.BACKROOMS).getPlayers()) {
        if (RANDOM.nextFloat() > 0.1F) {
          continue; // random chance.
        }

        if (player.getLocation().getBlock().getLightLevel() >= 4) {
          continue; // player must be in dark area.
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
      }
    }, 1, Ticks.in(Duration.ofSeconds(6)));

    // temporary teleport to black area and slower noise.
    scheduler.scheduleSyncRepeatingTask(this.plugin, () -> {
      for (final Player player : this.worldService.getWorld(Realm.BACKROOMS).getPlayers()) {
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

    // deep boom sound plus blindness.
    scheduler.scheduleSyncRepeatingTask(this.plugin, () -> {
      for (final Player player : this.worldService.getWorld(Realm.BACKROOMS).getPlayers()) {
        if (RANDOM.nextFloat() > 0.13F) {
          continue;
        }

        player.playSound(Sound.sound(
            org.bukkit.Sound.ENTITY_ELDER_GUARDIAN_DEATH,
            Sound.Source.MASTER,
            100,
            RANDOM.nextFloat(0.5F, 0.65F)
        ));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 1, true, true, false));
      }
    }, 1, Ticks.in(Duration.ofMinutes(2)) - 17);
  }

}
