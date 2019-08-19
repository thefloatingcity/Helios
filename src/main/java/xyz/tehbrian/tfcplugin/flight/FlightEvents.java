package xyz.tehbrian.tfcplugin.flight;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

@SuppressWarnings("unused")
public class FlightEvents implements Listener {

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        FlightManager.disableFlight(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FlightManager.disableFlight(event.getPlayer());
    }
}
