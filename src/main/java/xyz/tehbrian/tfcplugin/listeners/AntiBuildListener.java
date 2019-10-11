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
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

@SuppressWarnings("unused")
public class AntiBuildListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getDamager();
            if (!player.hasPermission("tfcplugin.build")) {
                event.setCancelled(true);
                player.sendMessage(new MsgBuilder().def("msg.no_build").build());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg.no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg.no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingPlace(HangingPlaceEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg.no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getRemover();
            if (!player.hasPermission("tfcplugin.build")) {
                event.setCancelled(true);
                player.sendMessage(new MsgBuilder().def("msg.no_build").build());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBucketFill(PlayerBucketFillEvent event) {
        onAntiBuild(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        onAntiBuild(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        onAntiBuild(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemDrop(PlayerDropItemEvent event) {
        onAntiBuild(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        onAntiBuild(event);
    }

    private <T extends PlayerEvent & Cancellable> void onAntiBuild(T event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg.no_build").build());
        }
    }
}
