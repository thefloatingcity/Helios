package city.thefloating.floatyplugin.fun;

import city.thefloating.floatyplugin.realm.Milieu;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FlingerListener implements Listener {

  private static final float MIN_PITCH = 0.5F;
  private static final float MAX_PITCH = 2F;
  private static final Duration COOLDOWN_DURATION = Duration.ofMillis(700);

  private final Map<UUID, Integer> heat = new HashMap<>();
  private final Map<UUID, Instant> lastFire = new HashMap<>();

  private static boolean isFlinger(final Material material) {
    return material == Material.LIGHT_WEIGHTED_PRESSURE_PLATE
        || material == Material.HEAVY_WEIGHTED_PRESSURE_PLATE;
  }

  @EventHandler
  public void onInteract(final PlayerInteractEvent event) {
    if (event.getClickedBlock() == null
        || event.getAction() != Action.PHYSICAL
        || event.getPlayer().isDead() // prevent player from being exploded multiple times.
        || !isFlinger(event.getClickedBlock().getType())) {
      return;
    }

    final var player = event.getPlayer();
    if (this.isVolatile(player)) {
      this.explodePlayer(player);
    } else {
      this.flingPlayer(player, event.getClickedBlock());
    }
  }

  private void explodePlayer(final Player player) {
    player.setHealth(0.0);
    player.getWorld().playSound(
        Sound.sound(
            org.bukkit.Sound.ENTITY_GENERIC_EXPLODE,
            Sound.Source.PLAYER,
            1F, 1F
        ), player.getX(), player.getY(), player.getZ()
    );
    player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 1);
  }

  private void flingPlayer(final Player player, final Block flinger) {
    // get magnitude based on type of flinger.
    final Material material = flinger.getType();
    final float magnitude;
    if (material == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
      magnitude = 0.9F;
    } else if (material == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
      magnitude = 1.4F;
    } else {
      return;
    }

    // shoot the player.
    final Vector eyeDir = player.getEyeLocation().getDirection();
    final var newVel = new Vector(0, 0.8, 0)
        .setX(eyeDir.getX())
        .setZ(eyeDir.getZ())
        .multiply(magnitude);
    player.setVelocity(newVel);

    // play some noises.
    final Location bLoc = flinger.getLocation();
    player.getWorld().playSound(
        Sound.sound(
            org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,
            Sound.Source.PLAYER,
            0.3F, 1F
        ), bLoc.getBlockX(), bLoc.getBlockY(), bLoc.getBlockZ()
    );
    player.getWorld().playSound(
        Sound.sound(
            org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING,
            Sound.Source.PLAYER,
            1F, this.getPitch(player)
        ), bLoc.getBlockX(), bLoc.getBlockY(), bLoc.getBlockZ()
    );

    // increase heat for next time.
    final var uuid = player.getUniqueId();
    this.heat.put(uuid, this.getHeat(uuid) + 1);
    this.lastFire.put(uuid, Instant.now());
  }

  private boolean isVolatile(final Player player) {
    // should be at 30 plates. 0.5 + 0.05 * 30 = 2.
    return this.getPitch(player) == 2F;
  }

  private float getPitch(final Player player) {
    final float increaseBy;
    if (Milieu.of(player) == Milieu.ONEROUS) {
      increaseBy = 1;
    } else {
      increaseBy = 0.1F;
    }
    return Math.min(MAX_PITCH, MIN_PITCH + (increaseBy * this.getHeat(player.getUniqueId())));
  }

  private int getHeat(final UUID uuid) {
    if (this.lastFire.containsKey(uuid)) {
      final var timeSinceLastFire = Duration.between(this.lastFire.get(uuid), Instant.now());
      if (timeSinceLastFire.compareTo(COOLDOWN_DURATION) > 0) {
        this.heat.remove(uuid);
      }
    }
    return this.heat.getOrDefault(uuid, 0);
  }

}
