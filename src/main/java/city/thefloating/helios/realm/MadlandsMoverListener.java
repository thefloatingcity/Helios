package city.thefloating.helios.realm;

import city.thefloating.helios.config.ConfigConfig;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * If the madlands is disabled, this listener will teleport all players who join
 * the server in the madlands to the overworld.
 */
public final class MadlandsMoverListener implements Listener {

  private final ConfigConfig configConfig;
  private final Transposer transposer;

  @Inject
  public MadlandsMoverListener(
      final ConfigConfig configConfig,
      final Transposer transposer
  ) {
    this.configConfig = configConfig;
    this.transposer = transposer;
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent event) {
    if (this.configConfig.data().madlandsEnabled()) {
      return;
    }

    final Player player = event.getPlayer();
    if (Realm.of(player) != Realm.MADLANDS) {
      return;
    }

    this.transposer.transpose(player, Realm.OVERWORLD);
  }

}
