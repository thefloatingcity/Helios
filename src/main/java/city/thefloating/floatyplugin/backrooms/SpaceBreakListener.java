package city.thefloating.floatyplugin.backrooms;

import city.thefloating.floatyplugin.realm.Realm;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class SpaceBreakListener implements Listener {

  /**
   * Prevents breaking the blocks that hold the players captive.
   */
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onSpaceBreak(final BlockBreakEvent event) {
    final Block block = event.getBlock();
    final World world = block.getWorld();
    if (Realm.from(world) != Realm.BACKROOMS) {
      return;
    }

    final int blockY = block.getY();
    final int middleY = WorldUtil.middleY(world);
    if (blockY < middleY - 2 || blockY > middleY + 2) {
      event.setCancelled(true);
    }
  }

}
