package city.thefloating.helios.loop;

import city.thefloating.helios.Helios;
import city.thefloating.helios.Ticks;
import city.thefloating.helios.realm.Habitat;
import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.time.Duration;

public final class PlayerVoidLoopTask {

  private final Helios plugin;

  @Inject
  public PlayerVoidLoopTask(
      final Helios plugin
  ) {
    this.plugin = plugin;
  }

  public void start() {
    final Server server = this.plugin.getServer();
    server.getScheduler().runTaskTimer(this.plugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        final Location loc = player.getLocation();
        final Habitat habitat = Habitat.of(player);
        if (loc.getY() <= LoopPositions.lowEngage(habitat)) { // they're too low.
          loc.setY(LoopPositions.lowTo(habitat));
          Teleport.relative(player, loc);
        } else if (loc.getY() >= LoopPositions.highEngage(habitat)) { // they're too high.
          loc.setY(LoopPositions.highTo(habitat));
          Teleport.relative(player, loc);
        }
      }
    }, 1, Ticks.in(Duration.ofSeconds(1)));
  }

}
