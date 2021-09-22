package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.config.LangConfig;

@SuppressWarnings("unused")
public class TransportationListener implements Listener {

    @EventHandler
    public void onElytra(final EntityToggleGlideEvent event) {
        if (event.getEntity().getWorld().getEnvironment() == World.Environment.THE_END
                || !event.isGliding()) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onSprint(final PlayerToggleSprintEvent event) {
        if (event.getPlayer().getWorld().getEnvironment() == World.Environment.NETHER
                && event.isSprinting()) {
            event.setCancelled(true);
        }
    }

}
