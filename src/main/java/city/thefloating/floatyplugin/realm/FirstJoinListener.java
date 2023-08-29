package city.thefloating.floatyplugin.realm;

import city.thefloating.floatyplugin.config.ConfigConfig;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Teleports new players to either the madlands or the overworld depending on
 * whether the madlands is enabled.
 */
public final class FirstJoinListener implements Listener {

  private final ConfigConfig configConfig;
  private final WorldService worldService;

  @Inject
  public FirstJoinListener(
      final ConfigConfig configConfig,
      final WorldService worldService
  ) {
    this.configConfig = configConfig;
    this.worldService = worldService;
  }

  @EventHandler
  public void onFirstJoin(final PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    if (player.hasPlayedBefore()) {
      return;
    }

    if (this.configConfig.data().madlandsEnabled()) {
      event.getPlayer().teleport(this.worldService.getSpawnPoint(Realm.MADLANDS));
    } else {
      event.getPlayer().teleport(this.worldService.getSpawnPoint(Realm.OVERWORLD));
    }
  }

}
