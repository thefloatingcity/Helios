package xyz.tehbrian.floatyplugin.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import javax.inject.Inject;

public class DeathListener implements Listener {

  private final WorldService worldService;

  @Inject
  public DeathListener(
      final WorldService worldService
  ) {
    this.worldService = worldService;
  }

  /**
   * Teleport players to their own world on respawn. This prevents them from
   * ending up in the madlands if they didn't purposefully travel there.
   *
   * @param event the event
   */
  @EventHandler
  public void onRespawn(final PlayerRespawnEvent event) {
    final FloatingWorld playerWorld = this.worldService.getFloatingWorld(event.getPlayer().getWorld());
    event.setRespawnLocation(this.worldService.getPlayerSpawnLocation(playerWorld));
  }

}
