package xyz.tehbrian.floatyplugin.realm;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
import xyz.tehbrian.floatyplugin.Permission;

/**
 * Prevents players from building in world spawns if they lack the relevant permission.
 * Also prevents pistons from modifying world spawn.
 */
public final class SpawnProtectionListener implements Listener {

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
    if (event.getRemover() instanceof Player player) {
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
    if (event.getDamager() instanceof Player player) {
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
      if (this.isWithinWorldSpawn(block.getLocation())) {
        event.setCancelled(true);
        return;
      }
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onPistonPull(final BlockPistonRetractEvent event) {
    for (final var block : event.getBlocks()) {
      if (this.isWithinWorldSpawn(block.getLocation())) {
        event.setCancelled(true);
        return;
      }
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
    final Location worldSpawn = location.getWorld().getSpawnLocation();
    return location.distanceSquared(worldSpawn) < SPAWN_PROTECTION_RADIUS_SQUARED;
  }

}
