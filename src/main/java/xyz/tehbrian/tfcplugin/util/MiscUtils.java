package xyz.tehbrian.tfcplugin.util;

import org.bukkit.ChatColor;

public class MiscUtils {

    private MiscUtils() {
    }

    public static String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String fancifyTime(final long milliseconds) {
        if (milliseconds >= 86400000) {
            return new MsgBuilder().msgKey("fancify_time_format").formats(String.valueOf(milliseconds / 86400000d), "days").build();
        } else if (milliseconds >= 3600000) {
            return new MsgBuilder().msgKey("fancify_time_format").formats(String.valueOf(milliseconds / 3600000d), "hours").build();
        } else if (milliseconds >= 60000) {
            return new MsgBuilder().msgKey("fancify_time_format").formats(String.valueOf(milliseconds / 60000d), "minutes").build();
        } else if (milliseconds >= 1000) {
            return new MsgBuilder().msgKey("fancify_time_format").formats(String.valueOf(milliseconds / 1000d), "seconds").build();
        } else {
            return new MsgBuilder().msgKey("fancify_time_format").formats(String.valueOf(milliseconds), "milliseconds").build();
        }
    }

}
