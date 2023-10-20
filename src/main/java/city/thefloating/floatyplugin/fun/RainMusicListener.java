package city.thefloating.floatyplugin.fun;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.Random;

public final class RainMusicListener implements Listener {

  private static final Random RANDOM = new Random();

  private static final Key MUSIC_KEY = Key.key("floating", "music.april_showers");
  private static final Sound MUSIC = Sound.sound(MUSIC_KEY, Sound.Source.MUSIC, 1, 1);
  private static final SoundStop MUSIC_STOP = SoundStop.named(MUSIC_KEY);

  @EventHandler
  public void onWeatherChange(final WeatherChangeEvent event) {
    if (event.toWeatherState() && RANDOM.nextFloat() <= 0.2) { // 20% chance.
      event.getWorld().stopSound(SoundStop.source(Sound.Source.MUSIC));
      event.getWorld().playSound(MUSIC);
    } else {
      event.getWorld().stopSound(MUSIC_STOP);
    }
  }

}
