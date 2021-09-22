package xyz.tehbrian.tfcplugin.util;

import broccolai.corn.paper.item.PaperItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigDeserializers {

    private ConfigDeserializers() {
    }

    public static List<String> deserializePage(final ConfigurationSection book, final Integer pageNumber) {
        final ConfigurationSection pages = Objects.requireNonNull(book).getConfigurationSection("pages");
        final ConfigurationSection page = Objects.requireNonNull(pages).getConfigurationSection(pageNumber.toString());
        final List<String> messages = new ArrayList<>();

        messages.add(new MsgBuilder()
                .prefixString(book.getString("multistart"))
                .msgKey("msg.page_header")
                .formats(Objects.requireNonNull(page).getString("title"), pageNumber, pages.getKeys(false).size())
                .build());

        for (final String line : page.getStringList("content")) {
            messages.add(new MsgBuilder()
                    .prefixString(book.getString("multi"))
                    .msgString(line)
                    .build());
        }

        messages.replaceAll(MiscUtils::color);
        return messages;
    }

    public static Inventory deserializeInventory(final ConfigurationSection section) {
        final ConfigurationSection items = Objects.requireNonNull(section).getConfigurationSection("items");
        final Inventory inventory = Bukkit.createInventory(
                null,
                section.getInt("size"),
                MiscUtils.color(section.getString("name"))
        );

        for (final String key : Objects.requireNonNull(items).getKeys(false)) {
            final ConfigurationSection item = items.getConfigurationSection(key);

            inventory.addItem(PaperItemBuilder.ofType(
                    Objects.requireNonNull(
                            Material.matchMaterial(
                                    Objects.requireNonNull(
                                            Objects.requireNonNull(item).getString("material")
                                    )
                            )))
                    .amount(item.isSet("amount") ? item.getInt("amount") : 1)
                    .name(MiniMessage.get().parse(Objects.requireNonNull(item.getString("name"))))
                    .lore(Component.text("uwu broke ur lore"))
                    .unbreakable(item.getBoolean("unbreakable"))
                    .build());
        }

        return inventory;
    }

    public static Location deserializeLocation(final CommentedConfigurationNode section) {
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(section.node("world").getString())),
                section.node("x").getDouble(),
                section.node("y").getDouble(),
                section.node("z").getDouble()
        );
    }

}
