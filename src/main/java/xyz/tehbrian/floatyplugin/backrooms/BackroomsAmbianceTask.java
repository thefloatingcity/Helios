package xyz.tehbrian.floatyplugin.backrooms;

import com.google.inject.Inject;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.world.FloatingWorld;
import xyz.tehbrian.floatyplugin.world.WorldService;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ClassCanBeRecord")
public final class BackroomsAmbianceTask {

  private static final Random RANDOM = ThreadLocalRandom.current();

  private final FloatyPlugin floatyPlugin;
  private final WorldService worldService;

  @Inject
  public BackroomsAmbianceTask(
      final FloatyPlugin floatyPlugin,
      final WorldService worldService
  ) {
    this.floatyPlugin = floatyPlugin;
    this.worldService = worldService;
  }

  public void start() {
    final Server server = this.floatyPlugin.getServer();

    final var oneTick = 1;
    final var oneSecond = oneTick * 20;

    server.getScheduler().scheduleSyncRepeatingTask(this.floatyPlugin, () -> {
      final World backrooms;
      backrooms = this.worldService.getWorld(FloatingWorld.BACKROOMS);

      for (final Player player : backrooms.getPlayers()) {
        if (RANDOM.nextFloat() < 0.2F) {
          player.playSound(Sound.sound(org.bukkit.Sound.AMBIENT_CAVE.key(), Sound.Source.MASTER, 10F, 0.6F));
        }
      }
    }, 1, 29 * oneSecond);
  }

}
