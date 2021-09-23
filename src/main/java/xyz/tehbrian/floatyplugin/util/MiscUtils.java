package xyz.tehbrian.floatyplugin.util;

public class MiscUtils {

    private MiscUtils() {
    }

    public static String fancifyTime(final long milliseconds) {
        final var format = "%.2f %s";
        final var simpleFormat = "%l %s";

        if (milliseconds >= 86400000) {
            return String.format(format, milliseconds / 86400000d, "days");
        } else if (milliseconds >= 3600000) {
            return String.format(format, milliseconds / 3600000d, "hours");
        } else if (milliseconds >= 60000) {
            return String.format(format, milliseconds / 60000d, "minutes");
        } else if (milliseconds >= 1000) {
            return String.format(format, milliseconds / 1000d, "seconds");
        } else {
            return String.format(simpleFormat, milliseconds, "milliseconds");
        }
    }

}
