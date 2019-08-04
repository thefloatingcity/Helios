package com.outlook.tehbrian.tfcplugin.util;

import com.outlook.tehbrian.tfcplugin.TFCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class MiscUtils {

    private static final TFCPlugin main = TFCPlugin.getInstance();

    private MiscUtils() {
    }

    public static Location getSpawn() {
        FileConfiguration config = main.getConfig();
        return new Location(Bukkit.getWorld(config.getString("spawn.world")), config.getDouble("spawn.x"), config.getDouble("spawn.y"), config.getDouble("spawn.z"));
    }

    public static String color(String string) {
        return string == null ? null : ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String fancifyTime(long milliseconds) {
        if (milliseconds >= 86400000) {
            return new MsgBuilder().msgKey("msg.fancify_time_format").formats(Math.floor((milliseconds / 86400000d) * 100) / 100, "days").build();
        } else if (milliseconds >= 3600000) {
            return new MsgBuilder().msgKey("msg.fancify_time_format").formats(Math.floor((milliseconds / 3600000d) * 100) / 100, "hours").build();
        } else if (milliseconds >= 60000) {
            return new MsgBuilder().msgKey("msg.fancify_time_format").formats(Math.floor((milliseconds / 60000d) * 100) / 100, "minutes").build();
        } else if (milliseconds >= 1000) {
            return new MsgBuilder().msgKey("msg.fancify_time_format").formats(Math.floor((milliseconds / 1000d) * 100) / 100, "seconds").build();
        } else {
            return new MsgBuilder().msgKey("msg.fancify_time_format").formats(Math.floor((milliseconds) * 100) / 100, "milliseconds").build();
        }
    }
}
