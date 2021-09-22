package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.user.UserService;

import java.util.Objects;

@SuppressWarnings("unused")
public class PianoListener implements Listener {

    private final UserService userService;

    public PianoListener(final @NonNull UserService userService) {
        this.userService = userService;
    }

    @EventHandler
    public void onItemHeld(final PlayerItemHeldEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPermission("tfcplugin.piano")
                || !this.userService.getUser(player).piano().enabled()) {
            return;
        }

        this.play(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()));
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof final Player player)
                || !player.hasPermission("tfcplugin.piano")
                || event.getClickedInventory() == null
                //|| !event.getView().title().equals(this.main.getConfig().getString("inventories.piano_notes.name"))
                || !event.isRightClick()) {
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
                this.userService.getUser(player).piano().instrument().asBukkitSound(),
                SoundCategory.MASTER,
                3,
                Float.parseFloat(item.getLore().get(2))
        );
    }

}
