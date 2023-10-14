package city.thefloating.floatyplugin.loop;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.Ticks;
import city.thefloating.floatyplugin.realm.Habitat;
import com.google.inject.Inject;
import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

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
          loc.setY(VoidLoopPositions.lowTo(habitat));
          teleportRelative(player, loc);
        } else if (loc.getY() >= VoidLoopPositions.highEngage(habitat)) { // they're too high.
          loc.setY(VoidLoopPositions.highTo(habitat));
          teleportRelative(player, loc);
        }
      }
    }, 0, Ticks.in(Duration.ofSeconds(1)));
  }

  private static void teleportRelative(
      final Player player, final Location loc
  ) {
    player.teleport(
        loc,
        TeleportFlag.Relative.X,
        TeleportFlag.Relative.Y,
        TeleportFlag.Relative.Z,
        TeleportFlag.Relative.YAW,
        TeleportFlag.Relative.PITCH
    );
  }

}
