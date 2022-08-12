package xyz.tehbrian.floatyplugin.transportation;

import com.google.inject.Inject;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.tehbrian.floatyplugin.FloatyPlugin;

@SuppressWarnings("ClassCanBeRecord")
public final class TransportationTask {

  private final FloatyPlugin floatyPlugin;
  private final FlightService flightService;

  @Inject
  public TransportationTask(
      final FloatyPlugin floatyPlugin,
      final FlightService flightService
  ) {
    this.floatyPlugin = floatyPlugin;
    this.flightService = flightService;
  }

  public void start() {
    final Server server = this.floatyPlugin.getServer();

    server.getScheduler().scheduleSyncRepeatingTask(this.floatyPlugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        final World.Environment environment = player.getWorld().getEnvironment();

        // no flight anywhere
        this.flightService.checkFlight(player);

        // elytra only in the end
        if (environment != World.Environment.THE_END) {
          player.setGliding(false);
        }

        if (environment == World.Environment.NETHER) {
          if (player.isSprinting()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000000, 1, true, false, false));
            player.setSprinting(false);
          }

          player.leaveVehicle();

          switch (player.getLocation().add(0, -0.8, 0).getBlock().getType()) {
            case ICE, PACKED_ICE, BLUE_ICE, FROSTED_ICE -> player.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOW, 40, 3, true, false, false
            ));
            case SOUL_SAND, SOUL_SOIL -> player.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOW, 40, 120, true, false, false
            ));
            default -> {
            }
          }
        }
      }
    }, 1, 10);
  }

}
