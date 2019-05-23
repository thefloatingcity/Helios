package com.outlook.tehbrian.tfcplugin.piano;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

@SuppressWarnings("unused")
public class PianoEvents implements Listener {

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        PianoManager.play(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()), true);
    }
}
