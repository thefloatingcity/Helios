package city.thefloating.helios.realm;

import city.thefloating.helios.Permission;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Prevents players from building in world spawns if they lack the relevant permission.
 * Also prevents pistons from modifying world spawn.
 */
public final class WorldSpawnProtectionListener implements Listener {

  public static final int SPAWN_PROTECTION_RADIUS = 8;
  public static final int SPAWN_PROTECTION_RADIUS_SQUARED = SPAWN_PROTECTION_RADIUS * SPAWN_PROTECTION_RADIUS;

  @EventHandler(priority = EventPriority.LOW)
  public void onBlockPlace(final BlockPlaceEvent event) {
    this.handle(event, event.getPlayer(), event.getBlockPlaced().getLocation());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onBlockBreak(final BlockBreakEvent event) {
    this.handle(event, event.getPlayer(), event.getBlock().getLocation());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onHangingPlace(final HangingPlaceEvent event) {
    if (event.getPlayer() != null) {
      this.handle(event, event.getPlayer(), event.getEntity().getLocation());
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onHangingBreak(final HangingBreakByEntityEvent event) {
    if (event.getRemover() instanceof final Player player) {
      this.handle(event, player, event.getEntity().getLocation());
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onBucketFill(final PlayerBucketFillEvent event) {
    this.handle(event, event.getBlock().getLocation());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
    this.handle(event, event.getBlock().getLocation());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onInteract(final PlayerInteractEvent event) {
    final @Nullable Block clickedBlock = event.getClickedBlock();
    if (clickedBlock != null) {
      this.handle(event, clickedBlock.getLocation());
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof final Player player) {
      this.handle(event, player, event.getEntity().getLocation());
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onArmorStandManipulate(final PlayerArmorStandManipulateEvent event) {
    this.handle(event, event.getRightClicked().getLocation());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onInteractEntity(final PlayerInteractEntityEvent event) {
    this.handle(event, event.getRightClicked().getLocation());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onPistonPush(final BlockPistonExtendEvent event) {
    for (final var block : event.getBlocks()) {
      // if new position is within spawn, cancel event.
      final Location current = block.getLocation();
      final Location predicted = this.applyDirection(current, event.getDirection());
      if (this.isWithinWorldSpawn(predicted)) {
        event.setCancelled(true);
        return;
      }
    }
  }

  private Location applyDirection(final Location location, final BlockFace direction) {
    return new Location(
        location.getWorld(),
        location.getX() + direction.getModX(),
        location.getY() + direction.getModY(),
        location.getZ() + direction.getModZ()
    );
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onPistonPull(final BlockPistonRetractEvent event) {
    for (final var block : event.getBlocks()) {
      // if position is within spawn, cancel event.
      if (this.isWithinWorldSpawn(block.getLocation())) {
        event.setCancelled(true);
        return;
      }
    }
  }

  /**
   * Prevents water, lava, and dragon eggs from getting into spawn.
   */
  @EventHandler
  public void onBlockFromTo(final BlockFromToEvent event) {
    if (this.isWithinWorldSpawn(event.getToBlock().getLocation())) {
      event.setCancelled(true);
    }
  }

  private <T extends PlayerEvent & Cancellable> void handle(final T event, final Location interactionLocation) {
    this.handle(event, event.getPlayer(), interactionLocation);
  }

  private <T extends Cancellable> void handle(
      final T event,
      final Player player,
      final Location interactionLocation
  ) {
    if (this.isWithinWorldSpawn(interactionLocation) && !player.hasPermission(Permission.BUILD_SPAWN)) {
      event.setCancelled(true);
    }
  }

  private boolean isWithinWorldSpawn(final Location location) {
    final Location loc1 = location.clone();
    loc1.setY(0);
    final Location loc2 = location.getWorld().getSpawnLocation();
    loc2.setY(0);
    return loc1.distanceSquared(loc2) < SPAWN_PROTECTION_RADIUS_SQUARED;
  }

}
