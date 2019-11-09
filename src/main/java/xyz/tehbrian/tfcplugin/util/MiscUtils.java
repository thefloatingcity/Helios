package xyz.tehbrian.tfcplugin.util;

import org.bukkit.ChatColor;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

public class MiscUtils {

    private MiscUtils() {
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
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
