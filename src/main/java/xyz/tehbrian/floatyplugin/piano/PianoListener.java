package xyz.tehbrian.floatyplugin.piano;

import com.destroystokyo.paper.MaterialTags;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.tehbrian.floatyplugin.Permissions;
import xyz.tehbrian.floatyplugin.config.InventoriesConfig;
import xyz.tehbrian.floatyplugin.user.UserService;
import xyz.tehbrian.floatyplugin.util.FormatUtil;

import java.util.List;
import java.util.Objects;

@SuppressWarnings({"unused"})
public final class PianoListener implements Listener {

  private final UserService userService;
  private final InventoriesConfig inventoriesConfig;

  @Inject
  public PianoListener(
      final UserService userService,
      final InventoriesConfig inventoriesConfig
  ) {
    this.userService = userService;
    this.inventoriesConfig = inventoriesConfig;
  }

  @EventHandler
  public void onItemHeld(final PlayerItemHeldEvent event) {
    final Player player = event.getPlayer();

    if (!player.hasPermission(Permissions.PIANO)
        || !this.userService.getUser(player).piano().enabled()) {
      return;
    }

    final @Nullable ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
    if (newItem != null) {
      this.play(player, newItem);
    }
  }

  @EventHandler
  public void onInventoryClick(final InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof final Player player)
        || !player.hasPermission(Permissions.PIANO)
        || event.getClickedInventory() == null
        || !event.getView().title().equals(FormatUtil.miniMessage(
        Objects.requireNonNull(this.inventoriesConfig.rootNode().node("piano_notes", "name").getString())))
        || !event.isRightClick()) {
      return;
    }

    event.setCancelled(true);

    final @Nullable ItemStack currentItem = event.getCurrentItem();
    if (currentItem != null) {
      this.play(player, currentItem);
    }
  }

  private void play(final Player player, final ItemStack item) {
    final @Nullable List<Component> lore = item.lore();

    if (lore == null
        || !MaterialTags.STAINED_GLASS_PANES.isTagged(item)
        || FormatUtil.plain(lore.get(0)).equals("Note")) {
      return;
    }

    player.getWorld().playSound(
        player.getEyeLocation(),
        this.userService.getUser(player).piano().instrument().asBukkitSound(),
        SoundCategory.MASTER,
        3,
        Float.parseFloat(FormatUtil.plain(lore.get(1)))
    );
  }

}
