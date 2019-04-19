package com.outlook.tehbrian.tfcplugin.events;

import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Utils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;

public class AntiBuildEvents implements Listener {

    private final Main main;

    public AntiBuildEvents(Main main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Utils.format("msg_no_build"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Utils.format("msg_no_build"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Utils.format("msg_no_build"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Utils.format("msg_no_build"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover().getType().equals(EntityType.PLAYER)) {
            Player player = (Player) event.getRemover();
            if (!player.hasPermission("tfcplugin.build")) {
                event.setCancelled(true);
                player.sendMessage(Utils.format("msg_no_build"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHangingPlace(HangingPlaceEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Utils.format("msg_no_build"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Utils.format("msg_no_build"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Utils.format("msg_no_build"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Utils.format("msg_no_build"));
        }
    }
}
