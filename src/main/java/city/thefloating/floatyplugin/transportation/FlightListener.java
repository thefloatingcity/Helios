package city.thefloating.floatyplugin.transportation;

import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

/**
 * Prevents flight.
 */
public final class FlightListener implements Listener {

  private final FlightService flightService;

  @Inject
  public FlightListener(
      final FlightService flightService
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
