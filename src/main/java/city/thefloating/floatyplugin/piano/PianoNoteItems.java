package city.thefloating.floatyplugin.piano;

import broccolai.corn.paper.item.PaperItemBuilder;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class PianoNoteItems {

  private final NamespacedKey pitchKey;

  @Inject
  public PianoNoteItems(
      final JavaPlugin javaPlugin
  ) {
    this.pitchKey = new NamespacedKey(javaPlugin, "piano-pitch");
  }

  public @Nullable Float getPitch(final ItemStack item) {
    return PaperItemBuilder.of(item).getData(this.pitchKey, PersistentDataType.FLOAT);
  }

  public ItemStack createItem(final Material material, final Component name, final float pitch) {
    return PaperItemBuilder.ofType(material)
        .name(name)
        .setData(this.pitchKey, PersistentDataType.FLOAT, pitch)
        .build();
  }

}
