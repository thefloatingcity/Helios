package com.outlook.tehbrian.tfcplugin.utils;

import com.outlook.tehbrian.tfcplugin.TFCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MiscUtils {

    private static final TFCPlugin main = TFCPlugin.getInstance();

    private MiscUtils() {
    }

    public static Location getSpawn() {
        FileConfiguration config = main.getConfig();
        return new Location(Bukkit.getWorld(config.getString("spawn.world")), config.getDouble("spawn.x"), config.getDouble("spawn.y"), config.getDouble("spawn.z"));
    }

    public static boolean isTop(Player player, Block block) {
        Location start = player.getEyeLocation().clone();
        while (!start.getBlock().equals(block) && start.distance(player.getEyeLocation()) < 6.0D) {
            start.add(player.getLocation().getDirection().multiply(0.05D));
        }
        return start.getY() % 1.0D > 0.5D;
    }

    public static String color(String string) {
        return string == null ? null : ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> createPage(int pageNumber, int maxPage, String type, String prefixKey, String multiKey) {
        ConfigurationSection page = main.getConfig().getConfigurationSection(type + ".page" + pageNumber);
        List<String> messages = new ArrayList<>();
        messages.add(new MsgBuilder().prefix(prefixKey).msg("msg_page").replace(page.getString("topic"), pageNumber, maxPage).build());
        for (String line : page.getStringList("content")) {
            messages.add(new MsgBuilder().prefix(multiKey).msgString(line).build());
        }
        return messages;
    }
}
