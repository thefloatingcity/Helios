package city.thefloating.floatyplugin.transportation;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.PotEff;
import city.thefloating.floatyplugin.realm.Milieu;
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
        final Realm realm = Realm.of(player);
        final Milieu milieu = realm.milieu();

        // no flight anywhere.
        this.flightService.checkFlight(player);

        // elytra only in the end.
        if (milieu != Milieu.DOCILE) {
          player.setGliding(false);
        }

        // nether-specific behavior.
        if (milieu == Milieu.ONEROUS) {
          // catch players who bypassed the sprint listener.
          if (player.isSprinting()) {
            player.addPotionEffect(PotEff.hidden(PotionEffectType.BLINDNESS, PotEff.INF, 1));
            player.setSprinting(false);
          }

          // catch players who bypassed the vehicle listener.
          if (player.getVehicle() != null
              && player.getVehicle().getType() != EntityType.ARROW // allow arrow chairs.
          ) {
            player.leaveVehicle();
          }

          // block-specific functionality.
          switch (player.getLocation().add(0, -0.8, 0).getBlock().getType()) {
            // ice blocks slow down player.
            case ICE, PACKED_ICE, BLUE_ICE, FROSTED_ICE -> player.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOW, 40, 3, true, false, false
            ));
            // soul blocks stop the player.
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
