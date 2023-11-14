package city.thefloating.floatyplugin.transportation;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.realm.Realm;
import com.google.inject.Inject;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class TransportationTask {

  private final FloatyPlugin plugin;
  private final FlightService flightService;

  @Inject
  public TransportationTask(
      final FloatyPlugin plugin,
      final FlightService flightService
  ) {
    this.plugin = plugin;
    this.flightService = flightService;
  }

  public void start() {
    final Server server = this.plugin.getServer();

    server.getScheduler().runTaskTimer(this.plugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        final Realm realm = Realm.from((player.getWorld()));

        // no flight anywhere.
        this.flightService.checkFlight(player);

        // elytra only in the end.
        if (realm != Realm.END) {
          player.setGliding(false);
        }

        // nether-specific stuff.
        if (realm == Realm.NETHER) {
          // catch any players who bypassed the sprint listener somehow.
          if (player.isSprinting()) {
            player.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS,
                PotionEffect.INFINITE_DURATION,
                1, true, false, false
            ));
            player.setSprinting(false);
          }

          // catch any players who bypassed the vehicle listener somehow.
          if (player.getVehicle() != null && player.getVehicle().getType() != EntityType.ARROW) {
            // arrow for chairs.
            player.leaveVehicle();
          }

          // block-specific functionality.
          switch (player.getLocation().add(0, -0.8, 0).getBlock().getType()) {
            // ice doesn't work to speed up player.
            case ICE, PACKED_ICE, BLUE_ICE, FROSTED_ICE -> player.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOW, 40, 3, true, false, false
            ));
            // soul sand and soul soil stops the player.
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
