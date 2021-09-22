package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.FlightService;

@SuppressWarnings("unused")
public class FlightListener implements Listener {

    private final FlightService flightService;

    public FlightListener(
            final @NonNull FlightService flightService
    ) {
        this.flightService = flightService;
    }

    @EventHandler
    public void onToggleFlight(final PlayerToggleFlightEvent event) {
        this.flightService.checkFlight(event.getPlayer());
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        this.flightService.checkFlight(event.getPlayer());
    }

    @EventHandler
    public void onGameModeChange(final PlayerGameModeChangeEvent event) {
        this.flightService.checkFlight(event.getPlayer());
    }

}
