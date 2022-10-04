package xyz.tehbrian.floatyplugin.voidloop;

import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.tehbrian.floatyplugin.FloatyPlugin;

public final class VoidLoopListener implements Listener {

  private final FloatyPlugin floatyPlugin;

  @Inject
  public VoidLoopListener(
      final FloatyPlugin floatyPlugin
  ) {
    this.floatyPlugin = floatyPlugin;
  }

  /**
   * Ensures that no entity will be damaged due to true void damage, which is
   * void damage actually caused by the void. (Void damage can occur from sources
   * other than the void, such as /kill, so this only activates if the entity
   * is below a certain height.)
   *
   * @param event the event
   */
  @EventHandler
  public void onVoidDamage(final EntityDamageEvent event) {
    final Entity entity = event.getEntity();
    final Location loc = entity.getLocation();

    if (event.getCause() != EntityDamageEvent.DamageCause.VOID
        || loc.getY() > entity.getWorld().getMinHeight() - 50) {
      return;
    }

    event.setCancelled(true);
  }

  /**
   * Offers a low-engage only void loop for non-player entities. This is useful
   * because the {@link VoidLoopTask} is player-only.
   *
   * @param event the event
   */
  @EventHandler
  public void onNonPlayerVoidDamage(final EntityDamageEvent event) {
    final Entity entity = event.getEntity();

    if (entity instanceof Player) {
      // players will be handled by VoidLoopTask, no need to handle them here.
      return;
    }

    final Location loc = entity.getLocation();
    final World.Environment environment = entity.getWorld().getEnvironment();

    if (loc.getY() > VoidLoopUtil.lowEngage(environment)) {
      return;
    }

    this.floatyPlugin.getServer().getScheduler().runTask(this.floatyPlugin, () -> {
      loc.setY(VoidLoopUtil.lowTeleport(environment));
      final var oldVelocity = entity.getVelocity();
      entity.teleport(loc);
      entity.setVelocity(oldVelocity);
    });
  }

}
