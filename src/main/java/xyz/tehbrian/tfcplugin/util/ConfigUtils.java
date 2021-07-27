package xyz.tehbrian.tfcplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import xyz.tehbrian.tfcplugin.TFCPlugin;
import xyz.tehbrian.tfcplugin.util.item.ItemBuilder;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigUtils {

    private static final TFCPlugin main = TFCPlugin.getInstance();

    private ConfigUtils() {
    }

    public static List<String> getPage(final String configKey, final Integer pageNumber) {
        final ConfigurationSection book = main.getConfig().getConfigurationSection(configKey);
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

    public static Inventory getInventory(final String configKey) {
        final ConfigurationSection invConfigSection = main.getConfig().getConfigurationSection(configKey);
        final ConfigurationSection items = Objects.requireNonNull(invConfigSection).getConfigurationSection("items");
        final Inventory inventory = Bukkit.createInventory(
                null,
                invConfigSection.getInt("size"),
                MiscUtils.color(invConfigSection.getString("name"))
        );

        for (final String key : Objects.requireNonNull(items).getKeys(false)) {
            final ConfigurationSection item = items.getConfigurationSection(key);

            inventory.addItem(new ItemBuilder(Material.matchMaterial(Objects.requireNonNull(Objects
                    .requireNonNull(item)
                    .getString("material"))))
                    .amount(item.isSet("amount") ? item.getInt("amount") : 1)
                    .name(item.getString("name"))
                    .lore(item.getStringList("lore"))
                    .unbreakable(item.getBoolean("unbreakable"))
                    .build());
        }

        return inventory;
    }

    public static Location getSpawn() {
        final FileConfiguration config = main.getConfig();
        return new Location(
                Bukkit.getWorld(Objects.requireNonNull(config.getString("spawn.world"))),
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z")
        );
    }

}
