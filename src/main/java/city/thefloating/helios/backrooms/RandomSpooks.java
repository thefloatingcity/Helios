package city.thefloating.helios.backrooms;

import city.thefloating.helios.Helios;
import city.thefloating.helios.PotEff;
import city.thefloating.helios.Ticks;
import city.thefloating.helios.nextbot.Nate;
import city.thefloating.helios.nextbot.Nextbot;
import city.thefloating.helios.realm.Realm;
import city.thefloating.helios.realm.WorldService;
import com.google.inject.Inject;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class RandomSpooks {

  private static final Random RANDOM = ThreadLocalRandom.current();

  private final Helios plugin;
  private final WorldService worldService;
  private final Nate nate;

  @Inject
  public RandomSpooks(
      final Helios plugin,
      final WorldService worldService,
      final Nate nate
  ) {
    this.plugin = plugin;
    this.worldService = worldService;
    this.nate = nate;
  }

  public void start() {
    final Server server = this.plugin.getServer();
    final BukkitScheduler scheduler = server.getScheduler();

    // slowed down cave noises.
    scheduler.runTaskTimer(this.plugin, () -> {
      for (final Player player : this.worldService.getWorld(Realm.BACKROOMS).getPlayers()) {
        if (RANDOM.nextFloat() > 0.2F) {
          continue;
        }

        player.playSound(Sound.sound(org.bukkit.Sound.AMBIENT_CAVE, Sound.Source.MASTER, 10F, 0.6F));
      }
    }, 1, Ticks.in(Duration.ofSeconds(29)) - 3);

    // high-pitched noise and jitter camera.
    scheduler.runTaskTimer(this.plugin, () -> {
      for (final Player player : this.worldService.getWorld(Realm.BACKROOMS).getPlayers()) {
        if (RANDOM.nextFloat() > 0.05F) {
          continue;
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
    }, 1, Ticks.in(Duration.ofSeconds(6)) - 4);

    // temporary teleport to black area and low-pitched noise.
    scheduler.runTaskTimer(this.plugin, () -> {
      for (final Player player : this.worldService.getWorld(Realm.BACKROOMS).getPlayers()) {
        if (RANDOM.nextFloat() > 0.1F) {
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

    // deep boom noise plus blindness.
    scheduler.runTaskTimer(this.plugin, () -> {
      for (final Player player : this.worldService.getWorld(Realm.BACKROOMS).getPlayers()) {
        if (RANDOM.nextFloat() > 0.15F) {
          continue;
        }

        player.playSound(Sound.sound(
            org.bukkit.Sound.ENTITY_ELDER_GUARDIAN_DEATH,
            Sound.Source.MASTER,
            100,
            RANDOM.nextFloat(0.5F, 0.65F)
        ));
        player.addPotionEffect(PotEff.hidden(PotionEffectType.BLINDNESS, 30, 1));
      }
    }, 1, Ticks.in(Duration.ofSeconds(123)) - 17);

    // spawn nextbot.
    scheduler.runTaskTimer(this.plugin, () -> {
      for (final Player player : this.worldService.getWorld(Realm.BACKROOMS).getPlayers()) {
        if (RANDOM.nextFloat() > 0.05F) {
          continue;
        }

        for (int dist = 24; dist < 33; dist++) {
          final var locPX = player.getLocation().add(new Vector(dist, 0, 0));
          if (locPX.getBlock().getType() == Material.AIR) {
            this.nate.createNextbot(Nextbot.Type.OBUNGA, locPX);
            return;
          }
          final var locNX = player.getLocation().add(new Vector(-dist, 0, 0));
          if (locNX.getBlock().getType() == Material.AIR) {
            this.nate.createNextbot(Nextbot.Type.OBUNGA, locNX);
            return;
          }
          final var locPZ = player.getLocation().add(new Vector(0, 0, dist));
          if (locPZ.getBlock().getType() == Material.AIR) {
            this.nate.createNextbot(Nextbot.Type.OBUNGA, locPZ);
            return;
          }
          final var locNZ = player.getLocation().add(new Vector(0, 0, -dist));
          if (locNZ.getBlock().getType() == Material.AIR) {
            this.nate.createNextbot(Nextbot.Type.OBUNGA, locNZ);
            return;
          }
        }
      }
    }, 1, Ticks.in(Duration.ofSeconds(497)) - 5);
  }

}
