package xyz.tehbrian.floatyplugin.music;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public final class RainMusicListener implements Listener {

  @EventHandler
  public void onWeatherChange(final WeatherChangeEvent event) {
    if (event.toWeatherState()) {
      for (final Player player : event.getWorld().getPlayers()) {
        player.playSound(Sound.sound(
            Key.key("floating", "music.april_showers"), Sound.Source.MUSIC, 1, 1)
        );
      }
    } else {
      for (final Player player : event.getWorld().getPlayers()) {
        player.stopSound(SoundStop.named(Key.key("floating", "music.april_showers")));
      }
    }
  }

}
