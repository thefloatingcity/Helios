package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import xyz.tehbrian.tfcplugin.managers.FlightManager;

@SuppressWarnings("unused")
public class FlightListener implements Listener {

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        FlightManager.disableFlight(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FlightManager.disableFlight(event.getPlayer());
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        FlightManager.disableFlight(event.getPlayer());
    }
}
