package city.thefloating.floatyplugin.piano;

import city.thefloating.floatyplugin.ChatFormat;
import city.thefloating.floatyplugin.config.PianoNotesConfig;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class PianoMenuProvider {

  private final PianoNotesConfig pianoNotesConfig;
  private final PianoNoteItems pianoNoteItems;

  @Inject
  public PianoMenuProvider(
      final PianoNotesConfig pianoNotesConfig,
      final PianoNoteItems pianoNoteItems
  ) {
    this.pianoNotesConfig = pianoNotesConfig;
    this.pianoNoteItems = pianoNoteItems;
  }

  public Inventory generate() {
    final var rootNode = Objects.requireNonNull(this.pianoNotesConfig.rootNode());
    final var pianoNotesNode = rootNode.node("piano-notes");

    final String name = Objects.requireNonNull(pianoNotesNode.node("name").getString());
    final var inventory = Bukkit.createInventory(
        null,
        InventoryType.CHEST,
        ChatFormat.miniMessage(name)
    );

    for (final ItemStack item : this.getCollection(NoteCollection.ALL)) {
      inventory.addItem(item);
    }

    return inventory;
  }

  public List<ItemStack> getCollection(final NoteCollection collection) {
    final var rootNode = Objects.requireNonNull(this.pianoNotesConfig.rootNode());
    final var pianoNotesNode = Objects.requireNonNull(rootNode.node("piano-notes"));
    final var itemsNode = Objects.requireNonNull(pianoNotesNode.node("items"));

    final List<ItemStack> items = new ArrayList<>();

    final List<CommentedConfigurationNode> itemNodes = itemsNode.childrenList();
    for (final int i : collection.indexes()) {
      final var itemNode = Objects.requireNonNull(itemNodes.get(i));

      final Material material = Material.valueOf(Objects.requireNonNull(itemNode.node("material").getString()));
      final Component name = ChatFormat.miniMessage(Objects.requireNonNull(itemNode.node("name").getString()));
      final float pitch = itemNode.node("pitch").getFloat();

      items.add(this.pianoNoteItems.createItem(material, name, pitch));
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
