package city.thefloating.floatyplugin.fun;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.realm.Habitat;
import city.thefloating.floatyplugin.soul.Charon;
import city.thefloating.floatyplugin.soul.Soul;
import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public final class ElevatorMusicTask {

  private static final int MIN_FALL_DIST = 1300;

  private static final Key MUSIC_KEY = Key.key("floating", "music.elevator");
  private static final Sound MUSIC = Sound.sound(MUSIC_KEY, Sound.Source.MUSIC, 1, 1);
  private static final SoundStop MUSIC_STOP = SoundStop.named(MUSIC_KEY);

  private final FloatyPlugin plugin;
  private final Charon charon;

  @Inject
  public ElevatorMusicTask(
      final FloatyPlugin plugin,
      final Charon charon
  ) {
    this.plugin = plugin;
    this.charon = charon;
  }

  public void start() {
    final Server server = this.plugin.getServer();
    server.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        if (Habitat.of(player.getWorld()) != Habitat.WHITE) {
          continue;
        }

        final Soul soul = this.charon.getSoul(player);
        if (player.getFallDistance() > MIN_FALL_DIST) {
          if (!soul.elevatorMusicPlaying()) {
            player.playSound(MUSIC);
            soul.elevatorMusicPlaying(true);
          }
        } else {
          if (soul.elevatorMusicPlaying()) {
            player.stopSound(MUSIC_STOP);
            soul.elevatorMusicPlaying(false);
          }
        }
      }
    }, 1, 20);
  }

}
