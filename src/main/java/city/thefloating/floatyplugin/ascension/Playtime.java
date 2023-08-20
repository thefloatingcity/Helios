package city.thefloating.floatyplugin.ascension;

import net.kyori.adventure.util.Ticks;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.time.Duration;

public final class Playtime {

  private Playtime() {
  }

  public static Duration getTimePlayed(final Player player) {
    return Ticks.duration(player.getStatistic(Statistic.PLAY_ONE_MINUTE));
  }

}
