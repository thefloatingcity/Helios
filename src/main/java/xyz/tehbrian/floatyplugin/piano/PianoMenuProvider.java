package xyz.tehbrian.floatyplugin.piano;

import broccolai.corn.paper.item.PaperItemBuilder;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import xyz.tehbrian.floatyplugin.config.InventoriesConfig;
import xyz.tehbrian.floatyplugin.util.FormatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
public final class PianoMenuProvider {

  private final InventoriesConfig inventoriesConfig;

  @Inject
  public PianoMenuProvider(
      final InventoriesConfig inventoriesConfig
  ) {
    this.inventoriesConfig = inventoriesConfig;
  }

  public Inventory generate() {
    final CommentedConfigurationNode pianoNotesNode = Objects.requireNonNull(
        this.inventoriesConfig.rootNode()).node("piano_notes");

    final var inventory = Bukkit.createInventory(
        null,
        InventoryType.CHEST,
        FormatUtil.miniMessage(Objects.requireNonNull(pianoNotesNode.node("name").getString()))
    );

    for (final ItemStack item : this.getCollection(NoteCollection.ALL)) {
      inventory.addItem(item);
    }

    return inventory;
  }

  public List<ItemStack> getCollection(final NoteCollection collection) {
    final CommentedConfigurationNode pianoNotesNode = Objects.requireNonNull(
        this.inventoriesConfig.rootNode()).node("piano_notes");
    final CommentedConfigurationNode itemsNode = Objects.requireNonNull(pianoNotesNode).node("items");

    final List<ItemStack> items = new ArrayList<>();

    final List<CommentedConfigurationNode> itemNodes = Objects.requireNonNull(itemsNode).childrenList();
    for (final int i : collection.indexes()) {
      final var itemNode = itemNodes.get(i);

      final List<Component> lore = new ArrayList<>();
      try {
        for (final String s : Objects.requireNonNull(itemNode.node("lore").getList(String.class))) {
          lore.add(FormatUtil.miniMessage(s));
        }
      } catch (final SerializationException e) {
        e.printStackTrace();
      }

      // TODO: use persistent data?
      // data.set(new NamespacedKey(main, "our-custom-key"), PersistentDataType.DOUBLE, Math.PI);
      items.add(PaperItemBuilder.ofType(Material.valueOf(Objects.requireNonNull(itemNode.node("material").getString())))
          .amount(itemNode.node("amount").getInt(1))
          .name(FormatUtil.miniMessage(Objects.requireNonNull(itemNode.node("name").getString())))
          .lore(lore)
          .unbreakable(itemNode.node("unbreakable").getBoolean(false))
          .build());
    }

    return items;
  }

  public enum NoteCollection {
    FS_MAJOR(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19, 21, 23, 24),
    FS_MINOR(0, 2, 3, 5, 7, 8, 10, 12, 14, 15, 17, 19, 20, 22, 24),
    ALL(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24),
    MEGALOVANIA(8, 6, 5, 4, 20, 15, 14, 13, 11);

    private final List<Integer> indexes;

    NoteCollection(final Integer... noteIndexes) {
      this.indexes = Arrays.asList(noteIndexes);
    }

    public List<Integer> indexes() {
      return this.indexes;
    }
  }

}
