package xyz.tehbrian.floatyplugin.realm;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import javax.inject.Inject;

public class RespawnListener implements Listener {

  private final WorldService worldService;

  @Inject
  public RespawnListener(
      final WorldService worldService
  ) {
    this.worldService = worldService;
  }

  /**
   * Teleports players to their current realm on respawn. This prevents them from
   * ending up in the madlands if they didn't purposefully transpose there.
   *
   * @param event the event
   */
  @EventHandler
  public void onRespawn(final PlayerRespawnEvent event) {
    final Realm current = Realm.from(event.getPlayer().getWorld());
    event.setRespawnLocation(this.worldService.getSpawnPoint(current));
  }

}
