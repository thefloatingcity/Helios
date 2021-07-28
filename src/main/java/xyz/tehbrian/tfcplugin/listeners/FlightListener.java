package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import xyz.tehbrian.tfcplugin.TFCPlugin;

@SuppressWarnings("unused")
public class FlightListener implements Listener {

    @EventHandler
    public void onToggleFlight(final PlayerToggleFlightEvent event) {
        TFCPlugin.getInstance().getUserManager().getUser(event.getPlayer()).disableFlight();
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        TFCPlugin.getInstance().getUserManager().getUser(event.getPlayer()).disableFlight();
    }

    @EventHandler
    public void onGameModeChange(final PlayerGameModeChangeEvent event) {
        TFCPlugin.getInstance().getUserManager().getUser(event.getPlayer()).disableFlight();
    }

}
