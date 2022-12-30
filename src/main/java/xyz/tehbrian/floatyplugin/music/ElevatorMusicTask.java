package xyz.tehbrian.floatyplugin.music;

import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.user.User;
import xyz.tehbrian.floatyplugin.user.UserService;

public final class ElevatorMusicTask {

  public static final int FALL_DISTANCE_MIN = 150;

  private static final Key SOUND_KEY = Key.key("floating", "music.elevator");
  private static final Sound SOUND = Sound.sound(SOUND_KEY, Sound.Source.MUSIC, 1, 1);
  private static final SoundStop SOUND_STOP = SoundStop.named(SOUND_KEY);

  private final FloatyPlugin floatyPlugin;
  private final UserService userService;

  @Inject
  public ElevatorMusicTask(
      final FloatyPlugin floatyPlugin,
      final UserService userService
  ) {
    this.floatyPlugin = floatyPlugin;
    this.userService = userService;
  }

  public void start() {
    final Server server = this.floatyPlugin.getServer();

    server.getScheduler().scheduleSyncRepeatingTask(this.floatyPlugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
          return;
        }

        final User user = this.userService.getUser(player);

        if (player.getFallDistance() > FALL_DISTANCE_MIN) {
          if (!user.elevatorMusicPlaying()) {
            player.playSound(SOUND);
            user.elevatorMusicPlaying(true);
          }
        } else {
          if (user.elevatorMusicPlaying()) {
            player.stopSound(SOUND_STOP);
            user.elevatorMusicPlaying(false);
          }
        }
      }
    }, 1, 20);
  }

}
