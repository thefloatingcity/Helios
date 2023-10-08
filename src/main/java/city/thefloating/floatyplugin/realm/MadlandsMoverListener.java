package city.thefloating.floatyplugin.realm;

import city.thefloating.floatyplugin.config.ConfigConfig;
import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * If the madlands is disabled, this listener will teleport all players who join
 * the server in the madlands to the overworld with the same coordinates.
 */
public final class MadlandsMoverListener implements Listener {

  private final WorldService worldService;
  private final ConfigConfig configConfig;

  @Inject
  public MadlandsMoverListener(
      final WorldService worldService,
      final ConfigConfig configConfig
  ) {
    this.worldService = worldService;
    this.configConfig = configConfig;
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent event) {
    if (this.configConfig.data().madlandsEnabled()) {
      return;
    }

    final Player player = event.getPlayer();
    final Location pLoc = player.getLocation();
    if (Realm.from(pLoc.getWorld()) != Realm.MADLANDS) {
      return;
    }

    pLoc.setWorld(this.worldService.getWorld(Realm.OVERWORLD));
    player.teleport(pLoc);
  }

}
