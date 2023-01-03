package xyz.tehbrian.floatyplugin.void_loop;

import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.realm.Habitat;

/**
 * Offers a cheap, low-engage only void loop for non-player entities. This is
 * useful because {@link PlayerVoidLoopTask} is player-only.
 */
public final class MobVoidLoopListener implements Listener {

  private final FloatyPlugin plugin;

  @Inject
  public MobVoidLoopListener(
      final FloatyPlugin plugin
  ) {
    this.plugin = plugin;
  }


  @EventHandler
  public void onMobVoidDamage(final EntityDamageEvent event) {
    final Entity entity = event.getEntity();
    if (entity instanceof Player) {
      // players will be handled in PlayerVoidLoop, no need to handle them here.
      return;
    }

    final Location loc = entity.getLocation();
    final Habitat habitat = Habitat.of(entity.getWorld());
    if (loc.getY() > VoidLoopPositions.lowEngage(habitat)) {
      return;
    }

    this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
      loc.setY(VoidLoopPositions.lowTo(habitat));
      final var oldVelocity = entity.getVelocity();
      entity.teleport(loc);
      entity.setVelocity(oldVelocity);
    });
  }

}
