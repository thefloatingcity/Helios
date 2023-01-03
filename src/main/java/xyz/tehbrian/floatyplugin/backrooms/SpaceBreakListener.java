package xyz.tehbrian.floatyplugin.backrooms;

import com.google.inject.Inject;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.tehbrian.floatyplugin.realm.Realm;
import xyz.tehbrian.floatyplugin.realm.RealmService;

public final class SpaceBreakListener implements Listener {

  private final RealmService realmService;

  @Inject
  public SpaceBreakListener(
      final RealmService realmService
  ) {
    this.realmService = realmService;
  }

  /**
   * Prevents breaking the blocks that hold the players captive.
   *
   * @param event the event
   */
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onSpaceBreak(final BlockBreakEvent event) {
    final Block block = event.getBlock();
    final World world = block.getWorld();
    if (this.realmService.getRealm(world) != Realm.BACKROOMS) {
      return;
    }

    final int blockY = block.getY();
    final int middleY = WorldUtil.middleY(world);
    if (blockY < middleY - 2 || blockY > middleY + 2) {
      event.setCancelled(true);
    }
  }

}
