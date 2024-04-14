package city.thefloating.helios.nextbot;

import city.thefloating.helios.Helios;
import city.thefloating.helios.PotEff;
import city.thefloating.helios.Ticks;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the lifecycle of nextbots.
 */
public final class Nate implements Listener {

  private final List<Nextbot> nextbots = new ArrayList<>();

  private final Helios helios;

  @Inject
  public Nate(
      final Helios helios
  ) {
    this.helios = helios;
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
    this.helios.getServer().getScheduler().runTaskTimer(
        this.helios,
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
    player.addPotionEffect(PotEff.hidden(PotionEffectType.SPEED, 30, amplifier));
  }
  //</editor-fold>

  //<editor-fold desc="lagged location">
  private static final int LAG_AMOUNT = 60;
  private final Map<UUID, Location> laggedLocation = new HashMap<>();

  private Location requestLaggedLocation(final Player player) {
    this.helios.getServer().getScheduler().runTaskLater(
        this.helios,
        () -> this.laggedLocation.put(player.getUniqueId(), player.getLocation()),
        LAG_AMOUNT
    );
    return this.laggedLocation.getOrDefault(player.getUniqueId(), player.getLocation());
  }
  //</editor-fold>

  //<editor-fold desc="jump">
  private void jump(final Nextbot nextbot, final int amplifier) {
    nextbot.pf().addPotionEffect(PotEff.hidden(PotionEffectType.JUMP, 5, amplifier));
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
  /**
   * Players within this radius will have their music started.
   */
  private static final int START_RADIUS = 32;
  /**
   * Players outside this radius will have their music stopped.
   * <p>
   * This was <b>not</b> added to restart music. That is only an unfortunate
   * side effect. This was added to deal with sound from emitters permanently
   * stopping outside a certain radius. I observed that behavior, but it doesn't
   * seem to be documented anywhere.
   * <p>
   * This number seems to be the magic number for the entities that nextbots use.
   */
  private static final int STOP_RADIUS = 64;

  private Collection<Player> playersInsideRadius(final Location location, final int radius) {
    return location.getWorld().getNearbyPlayers(location, radius);
  }

  private Collection<Player> playersOutsideRadius(final Location location, final int radius) {
    final List<Player> players = new ArrayList<>(location.getWorld().getPlayers());
    for (final Player player : this.playersInsideRadius(location, radius)) {
      players.remove(player);
    }
    return players;
  }

  private void startMusicTask(final Nextbot nextbot) {
    final var pf = nextbot.pf();
    this.helios.getServer().getScheduler().runTaskTimer(
        this.helios,
        task -> {
          if (pf.isDead()) {
            task.cancel();
            return;
          }

          this.playersInsideRadius(pf.getLocation(), START_RADIUS).forEach(p -> {
            // don't re-play music if we think that music is already playing.
            if (nextbot.musicPlaying(p)) {
              return;
            }

            // stop any possible leftover music.
            // this unfortunately means that nextbots of the same type will
            // override other nextbots' music.
            p.stopSound(nextbot.attr().musicStop());
            // play music from icon so that pathfinder (fox) can be silent.
            p.playSound(nextbot.attr().music(), nextbot.ic());
            nextbot.startedMusic().put(p, Instant.now());
          });

          this.playersOutsideRadius(pf.getLocation(), STOP_RADIUS).forEach(p -> {
            p.stopSound(nextbot.attr().musicStop());
            nextbot.startedMusic().remove(p);
          });
        }, 1, 20
    );
  }

  @EventHandler
  public void onPlayerRespawn(final PlayerPostRespawnEvent event) {
    this.removeFromAllMusicLater(event.getPlayer());
  }

  @EventHandler
  public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
    this.removeFromAllMusicLater(event.getPlayer());
  }

  @EventHandler
  public void onPlayerQuit(final PlayerQuitEvent event) {
    this.removeFromAllMusicLater(event.getPlayer());
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent event) {
    this.removeFromAllMusicLater(event.getPlayer());
  }

  private void removeFromAllMusic(final Player player) {
    for (final Nextbot nextbot : this.nextbots) {
      nextbot.startedMusic().remove(player);
    }
  }

  private void removeFromAllMusicLater(final Player player) {
    this.removeFromAllMusicLater(player, Duration.ofSeconds(2));
  }

  private void removeFromAllMusicLater(final Player player, final Duration delay) {
    this.helios.getServer().getScheduler().runTaskLater(
        this.helios,
        () -> this.removeFromAllMusic(player),
        Ticks.in(delay)
    );
  }
  //</editor-fold>

  private static <T> List<T> clone(final List<T> list) {
    return new ArrayList<>(list);
  }

}

