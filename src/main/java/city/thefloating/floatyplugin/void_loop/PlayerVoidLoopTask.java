package city.thefloating.floatyplugin.void_loop;

import city.thefloating.floatyplugin.FloatyPlugin;
import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import city.thefloating.floatyplugin.Ticks;
import city.thefloating.floatyplugin.realm.Habitat;

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
    final BukkitScheduler scheduler = server.getScheduler();

    scheduler.scheduleSyncRepeatingTask(this.plugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        final Location loc = player.getLocation();
        final Habitat habitat = Habitat.of(player.getWorld());

        if (loc.getY() <= VoidLoopPositions.lowEngage(habitat)) { // they're too low.
          scheduler.runTask(this.plugin, () -> {
            loc.setY(VoidLoopPositions.lowTo(habitat));
            final var oldVelocity = player.getVelocity();
            player.teleport(loc);
            player.setVelocity(oldVelocity);
          });
        } else if (loc.getY() >= VoidLoopPositions.highEngage(habitat)) { // they're too high.
          scheduler.runTask(this.plugin, () -> {
            loc.setY(VoidLoopPositions.highTo(habitat));
            final var oldVelocity = player.getVelocity();
            player.teleport(loc);
            player.setVelocity(oldVelocity);
          });
        }
      }
    }, 0, Ticks.in(Duration.ofSeconds(1)));
  }

}
