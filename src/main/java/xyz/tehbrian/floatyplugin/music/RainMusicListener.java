package xyz.tehbrian.floatyplugin.music;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public final class RainMusicListener implements Listener {

  private static final Key RAIN_MUSIC = Key.key("floating", "music.april_showers");
  private static final Sound SOUND = Sound.sound(RAIN_MUSIC, Sound.Source.MUSIC, 1, 1);
  private static final SoundStop SOUND_STOP = SoundStop.named(RAIN_MUSIC);

  @EventHandler
  public void onWeatherChange(final WeatherChangeEvent event) {
    final ForwardingAudience worldPlayers = Audience.audience(event.getWorld().getPlayers());
    if (event.toWeatherState()) {
      worldPlayers.playSound(SOUND);
    } else {
      worldPlayers.stopSound(SOUND_STOP);
    }
  }

}
