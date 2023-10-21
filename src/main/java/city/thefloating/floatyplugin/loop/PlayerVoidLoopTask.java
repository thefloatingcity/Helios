package city.thefloating.floatyplugin.loop;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.Ticks;
import city.thefloating.floatyplugin.realm.Habitat;
import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.time.Duration;

public final class PlayerVoidLoopTask {

  private final FloatyPlugin plugin;

  @Inject
  public PlayerVoidLoopTask(
      final FloatyPlugin plugin
  ) {
    this.plugin = plugin;
  }

  public void start() {
    final Server server = this.plugin.getServer();
    server.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        final Location loc = player.getLocation();
        final Habitat habitat = Habitat.of(player.getWorld());
        if (loc.getY() <= LoopPositions.lowEngage(habitat)) { // they're too low.
          loc.setY(LoopPositions.lowTo(habitat));
          Teleport.relative(player, loc);
        } else if (loc.getY() >= LoopPositions.highEngage(habitat)) { // they're too high.
          loc.setY(LoopPositions.highTo(habitat));
          Teleport.relative(player, loc);
        }
      }
    }, 0, Ticks.in(Duration.ofSeconds(1)));
  }

}
