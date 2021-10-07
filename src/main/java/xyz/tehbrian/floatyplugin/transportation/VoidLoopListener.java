package xyz.tehbrian.floatyplugin.transportation;

import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.FloatyPlugin;

@SuppressWarnings({"ClassCanBeRecord", "unused"})
public final class VoidLoopListener implements Listener {

    private final FloatyPlugin floatyPlugin;

    @Inject
    public VoidLoopListener(
            final @NonNull FloatyPlugin floatyPlugin
    ) {
        this.floatyPlugin = floatyPlugin;
    }

    /**
     * Ensures that no entity will be damaged due to true void damage, meaning
     * void damage actually caused by the void. Additionally, offers a low-engage-only
     * void loop for all entities; whereas the scheduler void loop is player-only.
     *
     * @param event the event
     */
    @EventHandler
    public void onEntityDamageByVoid(final EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        final Location location = entity.getLocation();

        if (event.getCause() != EntityDamageEvent.DamageCause.VOID
                || location.getY() > -50) {
            return;
        }

        event.setCancelled(true);

        if (entity instanceof Player) {
            return; // players will be handled by the task; no need to handle them twice
        }

        final World.Environment environment = entity.getWorld().getEnvironment();

        if (location.getY() > VoidLoopUtil.lowEngage(environment)) {
            return;
        }

        this.floatyPlugin.getServer().getScheduler().runTask(this.floatyPlugin, () -> {
            location.setY(VoidLoopUtil.lowTeleport(environment));
            final var oldVelocity = entity.getVelocity();
            entity.teleport(location);
            entity.setVelocity(oldVelocity);
        });
    }

}
