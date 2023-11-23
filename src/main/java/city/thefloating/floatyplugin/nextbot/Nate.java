package city.thefloating.floatyplugin.nextbot;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.PotEff;
import city.thefloating.floatyplugin.Ticks;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.google.inject.Inject;
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
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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

  //<editor-fold desc="creation and deletion">
  private void killNextbot(final Nextbot nextbot) {
    nextbot.pf().getPassengers().forEach(Entity::remove); // remove text.
    nextbot.pf().remove();
    this.nextbots.remove(nextbot);
  }

  public void killNextbots() {
    for (final Nextbot nextbot : clone(this.nextbots)) {
      nextbot.pf().getPassengers().forEach(Entity::remove); // remove text.
      nextbot.pf().remove();
    }
    this.nextbots.clear();
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
          self.setRemoveWhenFarAway(false);
        }
    );
    final Nextbot nextbot = new Nextbot(pathfinder, icon, attributes);
    this.startTargetingTask(nextbot);
    this.startMusicTask(nextbot);
    this.nextbots.add(nextbot);
  }
  //</editor-fold>

  //<editor-fold desc="nextbot-specific listeners (for deletion)">

  /**
   * Prevent fox from being leashed.
   */
  @EventHandler
  public void onLeash(final PlayerLeashEntityEvent event) {
    for (final Nextbot nextbot : this.nextbots) {
      if (!event.getEntity().equals(nextbot.pf())) {
        continue;
      }

      event.setCancelled(true);
      nextbot.pf().setLeashHolder(null);
    }
  }

  @EventHandler(priority = EventPriority.HIGH) // to avoid conflicting with void loop.
  public void onDamage(final EntityDamageEvent event) {
    for (final Nextbot nextbot : clone(this.nextbots)) {
      if (!event.getEntity().equals(nextbot.pf())) {
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
      if (!event.getDismounted().equals(nextbot.pf())) {
        continue;
      }

      this.killNextbot(nextbot);
    }
  }
  //</editor-fold>

  //<editor-fold desc="chasing">
  // nextbot speed constants for variable acceleration.
  private static final double NBS_MEASURE_MIN = 0.012;
  private static final double NBS_MEASURE_MAX = 0.036;
  private static final double NBS_TARGET_MIN = 0.9;
  private static final double NBS_TARGET_MAX = 1.5;

  // player speed constants for speed boost when being chased.
  private static final double PS_MIN_DIST = 20;
  private static final double PS_MIN_DIST_SQR = (int) Math.pow(PS_MIN_DIST, 2);
  private static final double PS_MIN_ZERO = 1.1;
  private static final double PS_MIN_ONE = 1.3;
  private static final double PS_MIN_TWO = 1.4;

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
                if (velLen <= NBS_MEASURE_MIN) {
                  speed = NBS_TARGET_MIN;
                } else if (velLen >= NBS_MEASURE_MAX) {
                  speed = NBS_TARGET_MAX;
                } else {
                  // https://stats.stackexchange.com/questions/281162/scale-a-number-between-a-range
                  speed = (
                      ((velLen - NBS_MEASURE_MIN) / (NBS_MEASURE_MAX - NBS_MEASURE_MIN))
                          * (NBS_TARGET_MAX - NBS_TARGET_MIN)
                  ) + NBS_TARGET_MIN;
                }

                pf.getPathfinder().moveTo(this.requestLaggedLocation(target), speed);

                if (nextbot.pf().getLocation().distanceSquared(target.getLocation()) < PS_MIN_DIST_SQR) {
                  if (speed >= PS_MIN_TWO) {
                    givePlayerRunningSpeed(target, 2);
                  } else if (speed >= PS_MIN_ONE) {
                    givePlayerRunningSpeed(target, 1);
                  } else if (speed >= PS_MIN_ZERO) {
                    givePlayerRunningSpeed(target, 0);
                  }
                }
              });
        },
        60,
        4
    );
  }

  private static void givePlayerRunningSpeed(final Player player, final int amplifier) {
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30, amplifier, true, false, false));
  }
  //</editor-fold>

  //<editor-fold desc="lagged location">
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
  //</editor-fold>

  //<editor-fold desc="jump">
  private void jump(final Nextbot nextbot, final int amplifier) {
    final PotionEffect jump = PotEff.hidden(PotionEffectType.JUMP, 5, amplifier);
    nextbot.pf().addPotionEffect(jump);
    nextbot.pf().setJumping(true);
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
  //</editor-fold>

  //<editor-fold desc="music">
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

          pf.getWorld().getNearbyPlayers(pf.getLocation(), 32).forEach(player -> {
            if (nextbot.startedMusic().containsKey(player)
                && Duration.between(nextbot.startedMusic().get(player), Instant.now())
                .compareTo(nextbot.attr().musicLength()) < 0) {
              // current time that song has been played is less than song length.
              return;
            }

            // stop previous music.
            player.stopSound(nextbot.attr().musicStop());
            // play sound from icon so that we can set pathfinder (fox) to silent.
            player.playSound(nextbot.attr().music(), ic);
            nextbot.startedMusic().put(player, Instant.now());
          });
        },
        1,
        20
    );
  }

  @EventHandler
  public void onPlayerRespawn(final PlayerPostRespawnEvent event) {
    this.removeFromActiveMusic(event.getPlayer());
  }

  @EventHandler
  public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
    this.removeFromActiveMusic(event.getPlayer());
  }

  @EventHandler
  public void onPlayerQuit(final PlayerQuitEvent event) {
    this.removeFromActiveMusic(event.getPlayer());
  }

  private void removeFromActiveMusic(final Player player) {
    this.floatyPlugin.getServer().getScheduler().runTaskLater(
        this.floatyPlugin,
        () -> {
          for (final Nextbot nextbot : this.nextbots) {
            nextbot.startedMusic().remove(player);
          }
        },
        Ticks.in(Duration.ofSeconds(2))
    );
  }
  //</editor-fold>

  private static <T> List<T> clone(final List<T> list) {
    return new ArrayList<>(list);
  }

}

