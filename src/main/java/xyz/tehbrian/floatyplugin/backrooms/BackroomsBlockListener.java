package xyz.tehbrian.floatyplugin.backrooms;

import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.tehbrian.floatyplugin.world.FloatingWorld;
import xyz.tehbrian.floatyplugin.world.WorldService;

public final class BackroomsBlockListener implements Listener {

  private final JavaPlugin javaPlugin;
  private final WorldService worldService;

  @Inject
  public BackroomsBlockListener(
      final JavaPlugin javaPlugin,
      final WorldService worldService
  ) {
    this.javaPlugin = javaPlugin;
    this.worldService = worldService;
  }

  /**
   * Replaces broken blocks with light to maintain the eerie, constant dimness.
   *
   * @param event the event
   */
  @EventHandler(ignoreCancelled = true)
  public void onBlockBreak(final BlockBreakEvent event) {
    if (this.worldService.getFloatingWorld(event.getBlock().getWorld()) != FloatingWorld.BACKROOMS) {
      return;
    }

    this.javaPlugin.getServer().getScheduler().runTask(this.javaPlugin, () -> {
      event.getBlock().setType(BackroomsBlocks.LIGHT);
      event.getBlock().setBlockData(BackroomsBlocks.LIGHT_DATA);
    });
  }

  /**
   * Prevents the blocks holding the poor players in captivity from being broken.
   *
   * @param event the event
   */
  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onSpaceBreak(final BlockBreakEvent event) {
    if (this.worldService.getFloatingWorld(event.getBlock().getWorld()) != FloatingWorld.BACKROOMS) {
      return;
    }

    if (event.getBlock().getType() == BackroomsBlocks.SPACE) {
      event.setCancelled(true);
    }
  }

  /**
   * Prevents players from placing blocks that {@link #onSpaceBreak} prevents them from breaking.
   * Placing those blocks would allow them to create indestructible barriers.
   * <p>
   * While this <i>works</i>, {@link #onSpaceBreak} should probably just be smarter about
   * what blocks to protect, i.e., only care about blocks in specific Y positions.
   *
   * @param event the event
   */
  @EventHandler(ignoreCancelled = true)
  public void onSpacePlace(final BlockPlaceEvent event) {
    if (this.worldService.getFloatingWorld(event.getBlock().getWorld()) != FloatingWorld.BACKROOMS) {
      return;
    }

    if (event.getBlock().getType() == BackroomsBlocks.SPACE) {
      event.setCancelled(true);
      event.getBlock().getLocation().createExplosion(4F, false, false);
    }
  }

}
