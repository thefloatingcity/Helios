package xyz.tehbrian.floatyplugin.world.backrooms;

import com.google.inject.Inject;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.tehbrian.floatyplugin.world.FloatingWorld;
import xyz.tehbrian.floatyplugin.world.WorldService;

public final class SpaceBreakListener implements Listener {

  private final WorldService worldService;

  @Inject
  public SpaceBreakListener(
      final WorldService worldService
  ) {
    this.worldService = worldService;
  }

  /**
   * Prevents the blocks that hold the captive players from being broken.
   *
   * @param event the event
   */
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onSpaceInteract(final BlockBreakEvent event) {
    final Block block = event.getBlock();
    final World world = block.getWorld();
    if (this.worldService.getFloatingWorld(world) != FloatingWorld.BACKROOMS) {
      return;
    }

    final int blockY = block.getY();
    final int middleY = ChunkUtil.middleY(world);
    if (blockY < middleY - 2 || blockY > middleY + 2) {
      // don't you love diagrams in comments? I sure do! here's one.
      //
      // ceiling
      // air
      // air
      // middleY
      // air
      // air
      // floor
      //
      event.setCancelled(true);
    }
  }

}
