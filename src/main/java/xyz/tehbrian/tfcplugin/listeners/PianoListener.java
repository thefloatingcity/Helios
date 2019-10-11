package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import xyz.tehbrian.tfcplugin.TFCPlugin;
import xyz.tehbrian.tfcplugin.managers.PianoManager;

@SuppressWarnings("unused")
public class PianoListener implements Listener {

    private final TFCPlugin main;

    public PianoListener(TFCPlugin main) {
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
                if (event.getView().getTitle().equals(main.getConfig().getString("inventories.piano_notes.name"))) {
                    if (event.isRightClick()) {
                        event.setCancelled(true);
                        PianoManager.play(player, event.getCurrentItem(), false);
                    }
                }
            }
        }
    }
}
