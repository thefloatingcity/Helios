package city.thefloating.floatyplugin.nextbot;

import city.thefloating.floatyplugin.FloatyPlugin;
import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the lifecycle of nextbots.
 */
public final class Nate implements Listener {

  private final List<Nextbot> nextbots = new ArrayList<>();

  private final FloatyPlugin floatyPlugin;

  @Inject
  public Nate(
      final FloatyPlugin floatyPlugin
  ) {
    this.floatyPlugin = floatyPlugin;
  }

  private void killNextbot(final Nextbot nextbot) {
    nextbot.pf().getPassengers().forEach(Entity::remove); // remove text.
    nextbot.pf().remove();
    this.nextbots.remove(nextbot);
  }

  public void killNextbots() {
    for (final Nextbot nextbot : this.nextbots) {
      nextbot.pf().getPassengers().forEach(Entity::remove); // remove text.
      nextbot.pf().remove();
    }
    this.nextbots.clear();
  }

  @EventHandler(priority = EventPriority.HIGH) // to avoid conflicting with void loop.
  public void onDamage(final EntityDamageEvent event) {
    for (final Nextbot nextbot : clone(this.nextbots)) {
      if (!nextbot.pf().equals(event.getEntity())) {
        continue;
      }

      if (event.getCause() == EntityDamageEvent.DamageCause.KILL) {
        this.killNextbot(nextbot);
      } else {
        event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onDismount(final EntityDismountEvent event) {
    for (final Nextbot nextbot : clone(this.nextbots)) {
      if (!nextbot.pf().equals(event.getDismounted())) {
        continue;
      }

      this.killNextbot(nextbot);
    }
  }

  public void createNextbot(final Nextbot.Type type, final Location loc) {
    this.createNextbot(type.attributes(), loc);
  }

  private void createNextbot(final Nextbot.Attributes attributes, final Location loc) {
    final AreaEffectCloud icon = (AreaEffectCloud) loc.getWorld().spawnEntity(
        loc, EntityType.AREA_EFFECT_CLOUD,
        CreatureSpawnEvent.SpawnReason.COMMAND,
        e -> {
          final var self = (AreaEffectCloud) e;
          self.customName(Component.text(attributes.iconChar()));
          self.setCustomNameVisible(true);
          self.setDuration(214748364);
          self.setParticle(Particle.BLOCK_CRACK, Material.AIR.createBlockData());
        }
    );
    final Mob pathfinder = (Mob) loc.getWorld().spawnEntity(
        loc, EntityType.FOX,
        CreatureSpawnEvent.SpawnReason.COMMAND,
        e -> {
          final var self = (Mob) e;
          self.setInvisible(true);
          self.setSilent(true);
          self.setInvulnerable(true); // doesn't apply to creative mode players. see onDamage for workaround.
          self.setCanPickupItems(false);
          self.getEquipment().clear(); // foxes have a chance to spawn with something in their mouth.
          self.addPassenger(icon);
        }
    );
    final Nextbot nextbot = new Nextbot(pathfinder, icon, attributes);
    this.startTargetingTask(nextbot);
    this.startMusicTask(nextbot);
    this.nextbots.add(nextbot);
  }

  private static final double MEASURE_MIN = 0.012;
  private static final double MEASURE_MAX = 0.034;
  private static final double TARGET_MIN = 0.9;
  private static final double TARGET_MAX = 1.5;

  /**
   * The rough maximum value of velocity.lengthSquared() when not moving.
   */
  private static final double STILL_VELOCITY = 0.007;

  private void startTargetingTask(final Nextbot nextbot) {
    final var pf = nextbot.pf();
    this.floatyPlugin.getServer().getScheduler().runTaskTimer(
        this.floatyPlugin,
        t -> {
          if (pf.isDead()) {
            t.cancel();
            return;
          }

          pf.getWorld().getNearbyPlayers(pf.getLocation(), 128)
              .stream().findFirst()
              .ifPresent(target -> {
                // kill target and self-destruct if target is close enough to hug.
                if (pf.getLocation().distanceSquared(target.getLocation()) < 1) {
                  target.setVelocity(pf.getVelocity().add(new Vector(0, 0.1, 0)).multiply(25));
                  target.setHealth(0.0);
                  this.killNextbot(nextbot);
                  t.cancel();
                  return;
                }

                final double yDiff = target.getY() - pf.getY();
                final double velLen = pf.getVelocity().lengthSquared();
                if (pf.isOnGround() && yDiff > 1 && velLen <= STILL_VELOCITY) {
                  this.chilledJump(nextbot, (int) yDiff - 1);
                }

                final double speed;
                if (velLen <= MEASURE_MIN) {
                  speed = TARGET_MIN;
                } else if (velLen >= MEASURE_MAX) {
                  speed = TARGET_MAX;
                } else {
                  // https://stats.stackexchange.com/questions/281162/scale-a-number-between-a-range
                  speed = (((velLen - MEASURE_MIN) / (MEASURE_MAX - MEASURE_MIN)) * (TARGET_MAX - TARGET_MIN)) + TARGET_MIN;
                }

                pf.getPathfinder().moveTo(this.requestLaggedLocation(target), speed);
              });
        },
        60,
        4
    );
  }

  private static final int LAG_AMOUNT = 60;
  private final Map<UUID, Location> laggedLocation = new HashMap<>();

  private Location requestLaggedLocation(final Player player) {
    this.floatyPlugin.getServer().getScheduler().runTaskLater(
        this.floatyPlugin,
        () -> this.laggedLocation.put(player.getUniqueId(), player.getLocation()),
        LAG_AMOUNT
    );
    return this.laggedLocation.getOrDefault(player.getUniqueId(), player.getLocation());
  }

  private static final int MUSIC_CUTOFF_DISTANCE = 40;
  private static final int MUSIC_CUTOFF_DISTANCE_SQUARED = (int) Math.pow(MUSIC_CUTOFF_DISTANCE, 2);

  private void startMusicTask(final Nextbot nextbot) {
    final var pf = nextbot.pf();
    final var ic = nextbot.ic();
    this.floatyPlugin.getServer().getScheduler().runTaskTimer(
        this.floatyPlugin,
        t -> {
          if (pf.isDead()) {
            t.cancel();
            return;
          }

          pf.getWorld().getNearbyPlayers(pf.getLocation(), 128).forEach(player -> {
            if (pf.getLocation().distanceSquared(player.getLocation()) > MUSIC_CUTOFF_DISTANCE_SQUARED) {
              player.stopSound(nextbot.attr().musicStop());
              nextbot.activeMusic().remove(player);
            } else if (!nextbot.activeMusic().contains(player)) {
              ic.getWorld().playSound(
                  nextbot.attr().musicStart(),
                  ic // play sound from icon so that we can set pathfinder (fox) to silent.
              );
              nextbot.activeMusic().add(player);
            }
          });
        },
        1,
        20
    );
  }

  private static final Duration JUMP_COOLDOWN = Duration.ofSeconds(4);

  /**
   * Calls {@link #jump(Nextbot, int)} but also juggles the cooldown.
   *
   * @param nextbot   the nextbot to make jump
   * @param amplifier the amplifier of the jump effect
   */
  private void chilledJump(final Nextbot nextbot, final int amplifier) {
    final Instant lastJump = nextbot.lastJump();
    if (lastJump == null) {
      this.jump(nextbot, amplifier);
      nextbot.lastJump(Instant.now());
      return;
    }

    final var sinceLastJump = Duration.between(lastJump, Instant.now());
    if (sinceLastJump.compareTo(JUMP_COOLDOWN) > 0) {
      this.jump(nextbot, amplifier);
      nextbot.lastJump(Instant.now());
    }
  }

  private void jump(final Nextbot nextbot, final int amplifier) {
    final PotionEffect jump = new PotionEffect(
        PotionEffectType.JUMP,
        5, amplifier,
        true, false
    );
    nextbot.pf().addPotionEffect(jump);
    nextbot.pf().setJumping(true);
  }

  private static <T> List<T> clone(final List<T> list) {
    return new ArrayList<>(list);
  }

}

