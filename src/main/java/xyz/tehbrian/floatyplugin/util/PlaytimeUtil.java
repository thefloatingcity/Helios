package xyz.tehbrian.floatyplugin.util;

import net.kyori.adventure.util.Ticks;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class PlaytimeUtil {

    private static final float MILLIS_IN_SECOND = 1000;
    private static final float MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60;
    private static final float MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;
    private static final float MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;

    private PlaytimeUtil() {
    }

    public static @NonNull String fancifyTime(final Duration duration) {
        final var ms = duration.toMillis();
        if (ms >= MILLIS_IN_DAY) {
            return fancifyTime(duration, TimeUnit.DAYS);
        } else if (ms >= MILLIS_IN_HOUR) {
            return fancifyTime(duration, TimeUnit.HOURS);
        } else if (ms >= MILLIS_IN_MINUTE) {
            return fancifyTime(duration, TimeUnit.MINUTES);
        } else {
            return fancifyTime(duration, TimeUnit.SECONDS);
        }
    }

    public static @NonNull String fancifyTime(final Duration duration, final TimeUnit timeUnit) {
        final var decimalFormat = new DecimalFormat("#.##");

        final var ms = duration.toMillis();
        return switch (timeUnit) {
            case DAYS -> {
                final var value = ms / MILLIS_IN_DAY;
                yield decimalFormat.format(value) + " " + (value == 1 ? "day" : "days");
            }
            case HOURS -> {
                final var value = ms / MILLIS_IN_HOUR;
                yield decimalFormat.format(value) + " " + (value == 1 ? "hour" : "hours");
            }
            case MINUTES -> {
                final var value = ms / MILLIS_IN_MINUTE;
                yield decimalFormat.format(value) + " " + (value == 1 ? "minute" : "minutes");
            }
            case SECONDS -> {
                final var value = ms / MILLIS_IN_SECOND;
                yield decimalFormat.format(value) + " " + (value == 1 ? "second" : "seconds");
            }
            default -> decimalFormat.format(ms) + " " + (ms == 1 ? "millisecond" : "milliseconds");
        };
    }

    public static @NonNull Duration getTimePlayed(final Player player) {
        return Ticks.duration(player.getStatistic(Statistic.PLAY_ONE_MINUTE));
    }

}
