package city.thefloating.floatyplugin.fun;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.realm.Milieu;
import city.thefloating.floatyplugin.soul.Charon;
import city.thefloating.floatyplugin.soul.Soul;
import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.bukkit.Server;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public final class ElevatorMusicJockey {

  private static final int MIN_FALL_DIST = 1300;

  private static final Key MUSIC_KEY = Key.key("floating", "music.elevator");
  private static final Sound MUSIC = Sound.sound(MUSIC_KEY, Sound.Source.MUSIC, 1, 1);
  private static final SoundStop MUSIC_STOP = SoundStop.named(MUSIC_KEY);

  private final FloatyPlugin plugin;
  private final Charon charon;

  @Inject
  public ElevatorMusicJockey(
      final FloatyPlugin plugin,
      final Charon charon
  ) {
    this.plugin = plugin;
    this.charon = charon;
  }

  public void start() {
    final Server server = this.plugin.getServer();
    server.getScheduler().runTaskTimer(this.plugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        if (Milieu.of(player) != Milieu.CANON) {
          continue;
        }
        this.refresh(player);
      }
    }, 1, 20);
  }

  private void refresh(final Player player) {
    final Soul soul = this.charon.grab(player);
    if (player.getFallDistance() >= MIN_FALL_DIST) {
      if (!soul.elevatorMusicPlaying()) {
        player.stopSound(SoundCategory.MUSIC);
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

}
