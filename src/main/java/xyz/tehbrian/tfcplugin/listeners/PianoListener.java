package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import xyz.tehbrian.tfcplugin.TFCPlugin;

import java.util.Objects;

@SuppressWarnings("unused")
public class PianoListener implements Listener {

    private final TFCPlugin main;

    public PianoListener(final TFCPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onItemHeld(final PlayerItemHeldEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPermission("tfcplugin.piano")) {
            return;
        }
        if (!this.main.getUserManager().getUser(player).hasPianoEnabled()) {
            return;
        }

        this.play(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()));
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getWhoClicked();

        if (!player.hasPermission("tfcplugin.piano")) {
            return;
        }
        if (event.getClickedInventory() == null) {
            return;
        }
        if (!event.getView().getTitle().equals(this.main.getConfig().getString("inventories.piano_notes.name"))) {
            return;
        }
        if (!event.isRightClick()) {
            return;
        }

        event.setCancelled(true);
        this.play(player, event.getCurrentItem());
    }

    private void play(final Player player, final ItemStack item) {
        /* TODO
        ItemStack itemStack = new ItemStack(Material.DIRT);
        NamespacedKey key = new NamespacedKey(main, "our-custom-key");
        ItemMeta itemMeta = itemfStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, Math.PI);
        itemStack.setItemMeta(itemMeta);
        */

        if (item == null) {
            return;
        }
        if (!item.getType().name().toLowerCase().contains("pane")) {
            return;
        }
        if (!item.getItemMeta().hasLore() && Objects.requireNonNull(item.getLore()).get(1).equals(ChatColor.DARK_GRAY + "[Note]")) {
            return;
        }

        player.getWorld().playSound(
                player.getEyeLocation(),
                this.main.getUserManager().getUser(player).getPianoSound().toSound(),
                SoundCategory.MASTER,
                3,
                Float.parseFloat(item.getLore().get(2))
        );
    }

}
