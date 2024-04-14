package city.thefloating.helios.transportation;

import city.thefloating.helios.Helios;
import city.thefloating.helios.PotEff;
import city.thefloating.helios.realm.Milieu;
import city.thefloating.helios.realm.Realm;
import com.google.inject.Inject;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public final class TransportationTask {

  private final Helios plugin;
  private final FlightService flightService;

  @Inject
  public TransportationTask(
      final Helios plugin,
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
            case ICE, PACKED_ICE, BLUE_ICE, FROSTED_ICE -> player.addPotionEffect(
                PotEff.hidden(PotionEffectType.SLOW, 40, 3));
            // soul blocks stop the player.
            case SOUL_SAND, SOUL_SOIL -> player.addPotionEffect(
                PotEff.hidden(PotionEffectType.SLOW, 40, 120));
            default -> {
            }
          }
        }
      }
    }, 1, 10);
  }

}
