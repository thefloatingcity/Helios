package xyz.tehbrian.floatyplugin.build;

import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.tehbrian.floatyplugin.Constants;
import xyz.tehbrian.floatyplugin.config.ConfigConfig;

@SuppressWarnings({"unused", "ClassCanBeRecord"})
public final class SpawnProtectionListener implements Listener {

    public static final int SPAWN_PROTECTION_RADIUS = 8;
    public static final int SPAWN_PROTECTION_RADIUS_SQUARED = SPAWN_PROTECTION_RADIUS * SPAWN_PROTECTION_RADIUS;

    private final ConfigConfig configConfig;

    @Inject
    public SpawnProtectionListener(final @NonNull ConfigConfig configConfig) {
        this.configConfig = configConfig;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(final BlockPlaceEvent event) {
        this.onSpawnPlace(event, event.getPlayer(), event.getBlockPlaced().getLocation());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(final BlockBreakEvent event) {
        this.onSpawnPlace(event, event.getPlayer(), event.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onHangingPlace(final HangingPlaceEvent event) {
        if (event.getPlayer() != null) {
            this.onSpawnPlace(event, event.getPlayer(), event.getEntity().getLocation());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onHangingBreak(final HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player player) {
            this.onSpawnPlace(event, player, event.getEntity().getLocation());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBucketFill(final PlayerBucketFillEvent event) {
        this.onSpawnPlace(event, event.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
        this.onSpawnPlace(event, event.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(final PlayerInteractEvent event) {
        final @Nullable Block clickedBlock = event.getClickedBlock();
        if (clickedBlock != null) {
            this.onSpawnPlace(event, clickedBlock.getLocation());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            this.onSpawnPlace(event, player, event.getEntity().getLocation());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onArmorStandManipulate(final PlayerArmorStandManipulateEvent event) {
        this.onSpawnPlace(event, event.getRightClicked().getLocation());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteractEntity(final PlayerInteractEntityEvent event) {
        this.onSpawnPlace(event, event.getRightClicked().getLocation());
    }

    private <T extends PlayerEvent & Cancellable> void onSpawnPlace(final T event, final Location interactionLocation) {
        this.onSpawnPlace(event, event.getPlayer(), interactionLocation);
    }

    private <T extends Cancellable> void onSpawnPlace(final T event, final Player player, final Location interactionLocation) {
        if (interactionLocation.distanceSquared(
                configConfig.spawnLocation(interactionLocation.getWorld().getEnvironment())) < SPAWN_PROTECTION_RADIUS_SQUARED
                && !player.hasPermission(Constants.Permissions.SPAWN_BUILD)) {
            event.setCancelled(true);
        }
    }

}
