package com.outlook.tehbrian.tfcplugin.utils;

import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    private static final Main main = Main.getInstance();

    private TextUtils() {
    }

    public static String format(String configKey, Object... replacements) {
        return formatC("tfc_prefix", configKey, replacements);
    }

    public static String formatC(String prefixKey, String configKey, Object... replacements) {
        FileConfiguration config = main.getConfig();
        if (prefixKey.equalsIgnoreCase("none")) {
            return color(String.format(config.getString(configKey), replacements));
        }
        return prefixC(prefixKey, String.format(config.getString(configKey), replacements));
    }

    public static String prefix(String string) {
        return prefixC("tfc_prefix", string);
    }

    public static String prefixC(String prefixKey, String string) {
        FileConfiguration config = main.getConfig();
        return color(config.getString(prefixKey) + " " + string);
    }

    public static String color(String string) {
        return string == null ? null : ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> createPage(int pageNumber, int maxPage, String type, String prefixKey, String multiKey) {
        ConfigurationSection page = main.getConfig().getConfigurationSection(type + ".page" + pageNumber);
        List<String> messages = new ArrayList<>();
        messages.add(formatC(prefixKey, "msg_page", page.getString("topic"), pageNumber, maxPage));
        for (String line : page.getStringList("content")) {
            messages.add(prefixC(multiKey, line));
        }
        return messages;
    }
}
