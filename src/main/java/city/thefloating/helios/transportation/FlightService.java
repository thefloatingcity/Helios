package city.thefloating.helios.transportation;

import city.thefloating.helios.Helios;
import city.thefloating.helios.Permission;
import city.thefloating.helios.soul.Charon;
import com.google.inject.Inject;
import org.bukkit.entity.Player;

/**
 * Checks the players' flight.
 */
public final class FlightService {

  private final Charon charon;
  private final Helios plugin;

  @Inject
  public FlightService(
      final Charon charon,
      final Helios plugin
  ) {
    this.charon = charon;
    this.plugin = plugin;
  }

  public void checkFlight(final Player player) {
    if (this.canFly(player)) {
      this.enableFlight(player);
    } else {
      this.disableFlight(player);
    }
  }

  public boolean canFly(final Player player) {
    return player.hasPermission(Permission.FLY) && this.charon.grab(player).flyBypassEnabled();
  }

  public void enableFlight(final Player player) {
    this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
      if (!player.getAllowFlight()) {
        player.setAllowFlight(true);
      }
    });
  }

  public void disableFlight(final Player player) {
    this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
      if (player.getAllowFlight()) {
        player.setAllowFlight(false);
      }
      if (player.isFlying()) {
        player.setFlying(false);
      }
    });
  }

}
