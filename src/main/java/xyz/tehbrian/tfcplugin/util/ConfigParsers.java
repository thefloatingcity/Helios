package xyz.tehbrian.tfcplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import xyz.tehbrian.tfcplugin.TFCPlugin;
import xyz.tehbrian.tfcplugin.util.item.ItemBuilder;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

import java.util.ArrayList;
import java.util.List;

public class ConfigParsers {

    private static final TFCPlugin main = TFCPlugin.getInstance();

    private ConfigParsers() {
    }

    public static List<String> getPage(String configKey, Integer pageNumber) {
        ConfigurationSection book = main.getConfig().getConfigurationSection(configKey);
        ConfigurationSection pages = book.getConfigurationSection("pages");
        ConfigurationSection page = pages.getConfigurationSection(pageNumber.toString());
        List<String> messages = new ArrayList<>();

        messages.add(new MsgBuilder()
                .prefixString(book.getString("multistart"))
                .msgKey("msg.page_format")
                .formats(page.getString("title"), pageNumber, pages.getKeys(false).size())
                .build());

        for (String line : page.getStringList("content")) {
            messages.add(new MsgBuilder()
                    .prefixString(book.getString("multi"))
                    .msgString(line)
                    .build());
        }

        messages.replaceAll(MiscUtils::color);
        return messages;
    }

    public static Inventory getInventory(String configKey) {
        ConfigurationSection invConfigSection = main.getConfig().getConfigurationSection(configKey);
        ConfigurationSection items = invConfigSection.getConfigurationSection("items");
        Inventory inventory = Bukkit.createInventory(null, invConfigSection.getInt("size"), MiscUtils.color(invConfigSection.getString("name")));

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = items.getConfigurationSection(key);

            inventory.addItem(new ItemBuilder(Material.matchMaterial(item.getString("material")))
                    .amount(item.isSet("amount") ? item.getInt("amount") : 1)
                    .name(item.getString("name"))
                    .lore(item.getStringList("lore"))
                    .unbreakable(item.getBoolean("unbreakable"))
                    .build());
        }

        return inventory;
    }
}
