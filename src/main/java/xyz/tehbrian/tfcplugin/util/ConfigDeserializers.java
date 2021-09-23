package xyz.tehbrian.tfcplugin.util;

import broccolai.corn.paper.item.PaperItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigDeserializers {

    private ConfigDeserializers() {
    }

    public static List<Component> deserializePage(final CommentedConfigurationNode book, final Integer pageNumber)
            throws SerializationException {
        final CommentedConfigurationNode pages = Objects.requireNonNull(book).node("pages");
        final CommentedConfigurationNode page = Objects.requireNonNull(pages).node(pageNumber.toString());
        final List<Component> messages = new ArrayList<>();

        messages.add(MiniMessage.get().parse(
                book.node("multistart").getString() + book.node("page_header").getString(),
                Template.of("title", Objects.requireNonNull(page.node("title").getString())),
                Template.of("page", pageNumber.toString()),
                Template.of("page_count", String.valueOf(pages.childrenList().size()))
        ));

        final String multi = book.node("multi").getString();
        for (final String line : Objects.requireNonNull(page.node("content").getList(String.class))) {
            messages.add(MiniMessage.get().parse(multi + line));
        }

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
