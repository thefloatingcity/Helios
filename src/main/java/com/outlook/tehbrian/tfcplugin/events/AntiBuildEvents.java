package com.outlook.tehbrian.tfcplugin.events;

import com.outlook.tehbrian.tfcplugin.utils.MsgBuilder;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@SuppressWarnings("unused")
public class AntiBuildEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg_no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg_no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingPlace(HangingPlaceEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg_no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover().getType().equals(EntityType.PLAYER)) {
            Player player = (Player) event.getRemover();
            if (!player.hasPermission("tfcplugin.build")) {
                event.setCancelled(true);
                player.sendMessage(new MsgBuilder().def("msg_no_build").build());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg_no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg_no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg_no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg_no_build").build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(new MsgBuilder().def("msg_no_build").build());
        }
    }
}
