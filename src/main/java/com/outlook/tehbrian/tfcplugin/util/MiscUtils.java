package com.outlook.tehbrian.tfcplugin.util;

import com.outlook.tehbrian.tfcplugin.TFCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
            start.add(player.getEyeLocation().getDirection().multiply(0.05D));
        }
        return start.getY() % 1.0D > 0.5D;
    }

    public static String color(String string) {
        return string == null ? null : ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String fancifyTime(long milliseconds) {
        if (milliseconds >= 86400000) {
            return new MsgBuilder().msgKey("fancify_time_format").replace(Math.floor((milliseconds / 86400000d) * 100) / 100, "days").build();
        } else if (milliseconds >= 3600000) {
            return new MsgBuilder().msgKey("fancify_time_format").replace(Math.floor((milliseconds / 3600000d) * 100) / 100, "hours").build();
        } else if (milliseconds >= 60000) {
            return new MsgBuilder().msgKey("fancify_time_format").replace(Math.floor((milliseconds / 60000d) * 100) / 100, "minutes").build();
        } else if (milliseconds >= 1000) {
            return new MsgBuilder().msgKey("fancify_time_format").replace(Math.floor((milliseconds / 1000d) * 100) / 100, "seconds").build();
        } else {
            return new MsgBuilder().msgKey("fancify_time_format").replace(Math.floor((milliseconds) * 100) / 100, "milliseconds").build();
        }
    }
}
