package xyz.tehbrian.floatyplugin.util;

import broccolai.corn.paper.item.PaperItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.Objects;

public final class ConfigDeserializers {

    private ConfigDeserializers() {
    }

    public static Inventory deserializeInventory(final ConfigurationSection section) {
        final ConfigurationSection items = Objects.requireNonNull(section).getConfigurationSection("items");
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

    public static Location deserializeLocation(final CommentedConfigurationNode section) {
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(section.node("world").getString("world"))),
                section.node("x").getDouble(0),
                section.node("y").getDouble(0),
                section.node("z").getDouble(0),
                section.node("yaw").getFloat(0),
                section.node("pitch").getFloat(0)
        );
    }

}
