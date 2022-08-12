package xyz.tehbrian.floatyplugin.util.config;

import broccolai.corn.paper.item.PaperItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.tehbrian.floatyplugin.util.FormatUtil;

import java.util.Objects;

public final class InventoryDeserializer {

  public InventoryDeserializer() {
  }

  public static Inventory deserializeInventory(final ConfigurationSection section) {
    final ConfigurationSection items = section.getConfigurationSection("items");
    final Inventory inventory = Bukkit.createInventory(
        null,
        section.getInt("size"),
        FormatUtil.miniMessage(Objects.requireNonNull(section.getString("name")))
    );

    for (final String key : Objects.requireNonNull(items).getKeys(false)) {
      final @Nullable ConfigurationSection item = items.getConfigurationSection(key);
      Objects.requireNonNull(item);

      final @Nullable Material material = Material.matchMaterial(Objects.requireNonNull(item.getString("material")));
      Objects.requireNonNull(material);

      inventory.addItem(PaperItemBuilder.ofType(material)
          .amount(item.isSet("amount") ? item.getInt("amount") : 1)
          .name(FormatUtil.miniMessage(Objects.requireNonNull(item.getString("name"))))
          .loreList(Component.text("uwu broke ur lore"))
          .unbreakable(item.getBoolean("unbreakable"))
          .build());
    }

    return inventory;
  }

}
