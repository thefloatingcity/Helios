package xyz.tehbrian.tfcplugin.listeners;

import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.config.InventoriesConfig;
import xyz.tehbrian.tfcplugin.user.UserService;

import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class PianoListener implements Listener {

    private final UserService userService;
    private final InventoriesConfig inventoriesConfig;

    @Inject
    public PianoListener(
            final @NonNull UserService userService,
            final @NonNull InventoriesConfig inventoriesConfig
    ) {
        this.userService = userService;
        this.inventoriesConfig = inventoriesConfig;
    }

    @EventHandler
    public void onItemHeld(final PlayerItemHeldEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPermission("tfcplugin.piano")
                || !this.userService.getUser(player).piano().enabled()) {
            return;
        }

        this.play(player, Objects.requireNonNull(player.getInventory().getItem(event.getNewSlot())));
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof final Player player)
                || !player.hasPermission("tfcplugin.piano")
                || event.getClickedInventory() == null
                || !event.getView().title().contains(MiniMessage.get().parse(
                Objects.requireNonNull(this.inventoriesConfig.rootNode().node("piano_notes", "name").getString())))
                || !event.isRightClick()) {
            return;
        }

        event.setCancelled(true);
        this.play(player, Objects.requireNonNull(event.getCurrentItem()));
    }

    private void play(final Player player, final ItemStack item) {
//        ItemStack itemStack = new ItemStack(Material.DIRT);
//        NamespacedKey key = new NamespacedKey(main, "our-custom-key");
//        ItemMeta itemMeta = itemfStack.getItemMeta();
//        itemMeta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, Math.PI);
//        itemStack.setItemMeta(itemMeta);

        final List<Component> lore = Objects.requireNonNull(item.lore());


        if (!item.getType().name().toLowerCase().contains("pane")
                || !lore.get(1).contains(Component.text("[Note]"))) {
            return;
        }

        player.getWorld().playSound(
                player.getEyeLocation(),
                this.userService.getUser(player).piano().instrument().asBukkitSound(),
                SoundCategory.MASTER,
                3,
                Float.parseFloat(lore.get(2).toString())
        );
    }

}
