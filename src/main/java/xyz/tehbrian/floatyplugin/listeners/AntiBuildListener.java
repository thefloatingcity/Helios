package xyz.tehbrian.floatyplugin.listeners;

import com.google.inject.Inject;
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
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;

@SuppressWarnings("unused")
public class AntiBuildListener implements Listener {

    private final LangConfig langConfig;

    @Inject
    public AntiBuildListener(final @NonNull LangConfig langConfig) {
        this.langConfig = langConfig;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(final BlockPlaceEvent event) {
        this.onAntiBuild(event, event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(final BlockBreakEvent event) {
        this.onAntiBuild(event, event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingPlace(final HangingPlaceEvent event) {
        if (event.getPlayer() != null) {
            this.onAntiBuild(event, event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingBreak(final HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player player) {
            this.onAntiBuild(event, player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBucketFill(final PlayerBucketFillEvent event) {
        this.onAntiBuild(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
        this.onAntiBuild(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemPickup(final PlayerAttemptPickupItemEvent event) {
        this.onAntiBuild(event, true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemDrop(final PlayerDropItemEvent event) {
        this.onAntiBuild(event, true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(final PlayerInteractEvent event) {
        this.onAntiBuild(event, true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            this.onAntiBuild(event, player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onArmorStandManipulate(final PlayerArmorStandManipulateEvent event) {
        this.onAntiBuild(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteractEntity(final PlayerInteractEntityEvent event) {
        this.onAntiBuild(event, true);
    }

    private <T extends PlayerEvent & Cancellable> void onAntiBuild(final T event, final boolean silent) {
        this.onAntiBuild(event, event.getPlayer(), silent);
    }

    private <T extends PlayerEvent & Cancellable> void onAntiBuild(final T event) {
        this.onAntiBuild(event, false);
    }

    private <T extends Cancellable> void onAntiBuild(final T event, final Player player, final boolean silent) {
        if (!player.hasPermission("floatyplugin.build")) {
            event.setCancelled(true);
            if (!silent) {
                player.sendMessage(this.langConfig.c(NodePath.path("no_build")));
            }
        }
    }

    private <T extends Cancellable> void onAntiBuild(final T event, final Player player) {
        this.onAntiBuild(event, player, false);
    }

}
