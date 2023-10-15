package city.thefloating.floatyplugin.loop;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.realm.Habitat;
import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Offers a cheap, low-engage only void loop for non-player entities. This is
 * useful because {@link PlayerVoidLoopTask} is player-only.
 * <p>
 * Also prevents void damage for all entities.
 */
public final class VoidDamageListener implements Listener {

  private final FloatyPlugin plugin;

  @Inject
  public VoidDamageListener(
      final FloatyPlugin plugin
  ) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onVoidDamage(final EntityDamageEvent event) {
    if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
      event.setCancelled(true); // void damage? no such thing.
    }

    final Entity entity = event.getEntity();
    if (entity instanceof Player) {
      // players will be handled in PlayerVoidLoop, no need to handle them here.
      return;
    }

    final Location loc = entity.getLocation();
    final Habitat habitat = Habitat.of(entity.getWorld());
    if (loc.getY() > LoopPositions.lowEngage(habitat)) {
      return;
    }

    loc.setY(LoopPositions.lowTo(habitat));
    Teleport.relative(entity, loc);
  }

}