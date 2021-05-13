package xyz.tehbrian.tfcplugin.util;

import org.bukkit.ChatColor;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

public class MiscUtils {

    private MiscUtils() {}

    public static String color(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String fancifyTime(final long milliseconds) {
        if (milliseconds >= 86400000) {
            return new MsgBuilder().msgKey("msg.fancify_time_format").formats(milliseconds / 86400000d, "days").build();
        } else if (milliseconds >= 3600000) {
            return new MsgBuilder().msgKey("msg.fancify_time_format").formats(milliseconds / 3600000d, "hours").build();
        } else if (milliseconds >= 60000) {
            return new MsgBuilder().msgKey("msg.fancify_time_format").formats(milliseconds / 60000d, "minutes").build();
        } else if (milliseconds >= 1000) {
            return new MsgBuilder().msgKey("msg.fancify_time_format").formats(milliseconds / 1000d, "seconds").build();
        } else {
            return new MsgBuilder().msgKey("msg.fancify_time_format").formats(milliseconds, "milliseconds").build();
        }
    }
}
