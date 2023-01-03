package xyz.tehbrian.floatyplugin.void_loop;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Ensures that no entity will be damaged to true void damage.
 * {@link EntityDamageEvent.DamageCause#VOID} can be caused by sources other than
 * the void, such as {@code /kill}, so this listener only prevents damage if
 * the entity is below a certain height.
 */
public final class DamageListener implements Listener {

  @EventHandler
  public void onVoidDamage(final EntityDamageEvent event) {
    if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
      return;
    }

    final Entity entity = event.getEntity();
    if (entity.getLocation().getY() < entity.getWorld().getMinHeight() - 50) {
      event.setCancelled(true);
    }
  }

}
