package city.thefloating.helios.piano;

import city.thefloating.helios.ChatFormat;
import city.thefloating.helios.Permission;
import city.thefloating.helios.config.PianoNotesConfig;
import city.thefloating.helios.soul.Charon;
import com.google.inject.Inject;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class PianoPlayListener implements Listener {

  private final Charon charon;
  private final PianoNotesConfig pianoNotesConfig;
  private final PianoNoteItems pianoNoteItems;

  @Inject
  public PianoPlayListener(
      final Charon charon,
      final PianoNotesConfig pianoNotesConfig,
      final PianoNoteItems pianoNoteItems
  ) {
    this.charon = charon;
    this.pianoNotesConfig = pianoNotesConfig;
    this.pianoNoteItems = pianoNoteItems;
  }

  @EventHandler
  public void onItemHeld(final PlayerItemHeldEvent event) {
    final Player player = event.getPlayer();
    if (!player.hasPermission(Permission.PIANO)
        || !this.charon.grab(player).piano().enabled()) {
      return;
    }

    final @Nullable ItemStack item = player.getInventory().getItem(event.getNewSlot());
    if (item != null) {
      this.tryPlay(player, item);
    }
  }

  @EventHandler
  public void onInventoryClick(final InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof final Player player)
        || !player.hasPermission(Permission.PIANO)
        || event.getClickedInventory() == null
        || !event.getView().title().equals(ChatFormat.miniMessage(
        this.pianoNotesConfig.rootNode().node("piano-notes", "name").getString()))) {
      return;
    }

    // we are in the piano menu and the player has the piano permission.
    if (event.isRightClick()) {
      event.setCancelled(true);

      final @Nullable ItemStack item = event.getCurrentItem();
      if (item != null) {
        this.tryPlay(player, item);
      }
    } else if (event.getAction() == InventoryAction.HOTBAR_SWAP
        || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
      event.setCancelled(true);

      final @Nullable ItemStack hotbarItem = player.getInventory().getItem(event.getHotbarButton());
      if (hotbarItem != null) {
        this.tryPlay(player, hotbarItem);
      }
    }
  }

  private void tryPlay(final Player player, final ItemStack item) {
    final Float pitch = this.pianoNoteItems.getPitch(item);
    if (pitch == null) {
      return;
    }

    player.getWorld().playSound(
        player.getEyeLocation(),
        this.charon.grab(player).piano().instrument().sound(),
        SoundCategory.MASTER,
        3,
        pitch
    );
  }

}
