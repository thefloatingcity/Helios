package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.entity.EntityType;
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
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

import java.util.Objects;

@SuppressWarnings("unused")
public class AntiBuildListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg.no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(final BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg.no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingPlace(final HangingPlaceEvent event) {
        if (!Objects.requireNonNull(event.getPlayer()).hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg.no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingBreak(final HangingBreakByEntityEvent event) {
        if (Objects.requireNonNull(event.getRemover()).getType() == EntityType.PLAYER) {
            final Player player = (Player) event.getRemover();
            if (!player.hasPermission("tfcplugin.build")) {
                event.setCancelled(true);
                player.sendMessage(new MsgBuilder().def("msg.no_build").build());
            }
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
        this.onAntiBuild(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemDrop(final PlayerDropItemEvent event) {
        this.onAntiBuild(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(final PlayerInteractEvent event) {
        this.onAntiBuild(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER) {
            final Player player = (Player) event.getDamager();
            if (!player.hasPermission("tfcplugin.build")) {
                event.setCancelled(true);
                player.sendMessage(new MsgBuilder().def("msg.no_build").build());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onArmorStandManipulate(final PlayerArmorStandManipulateEvent event) {
        this.onAntiBuild(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemFrameRotate(final PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            this.onAntiBuild(event);
        }
    }

    private <T extends PlayerEvent & Cancellable> void onAntiBuild(final T event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg.no_build").build());
        }
    }

}
