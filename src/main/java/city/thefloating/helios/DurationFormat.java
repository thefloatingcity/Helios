package city.thefloating.helios;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class DurationFormat {

  private static final float MILLIS_IN_SECOND = 1000;
  private static final float MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60;
  private static final float MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;
  private static final float MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;

  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

  private DurationFormat() {
  }

  public static String fancifyTime(final Duration duration) {
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

  public static String fancifyTime(final Duration duration, final TimeUnit timeUnit) {
    final var ms = duration.toMillis();
    return switch (timeUnit) {
      case DAYS -> {
        final var value = ms / MILLIS_IN_DAY;
        yield DECIMAL_FORMAT.format(value) + " " + (value == 1 ? "day" : "days");
      }
      case HOURS -> {
        final var value = ms / MILLIS_IN_HOUR;
        yield DECIMAL_FORMAT.format(value) + " " + (value == 1 ? "hour" : "hours");
      }
      case MINUTES -> {
        final var value = ms / MILLIS_IN_MINUTE;
        yield DECIMAL_FORMAT.format(value) + " " + (value == 1 ? "minute" : "minutes");
      }
      case SECONDS -> {
        final var value = ms / MILLIS_IN_SECOND;
        yield DECIMAL_FORMAT.format(value) + " " + (value == 1 ? "second" : "seconds");
      }
      default -> DECIMAL_FORMAT.format(ms) + " " + (ms == 1 ? "millisecond" : "milliseconds");
    };
  }

}
