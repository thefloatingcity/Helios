package com.outlook.tehbrian.tfcplugin.piano;

import com.outlook.tehbrian.tfcplugin.TFCPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

@SuppressWarnings("unused")
public class PianoEvents implements Listener {

    private final TFCPlugin main;

    public PianoEvents(TFCPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        PianoManager.play(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()), true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getClickedInventory() != null) {
                if (event.getClickedInventory().getName().equals(main.getConfig().getString("inventories.piano_notes.name"))) {
                    if (event.isRightClick()) {
                        event.setCancelled(true);
                        PianoManager.play(player, event.getCurrentItem(), false);
                    }
                }
            }
        }
    }
}
