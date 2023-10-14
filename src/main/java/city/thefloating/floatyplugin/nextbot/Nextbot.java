package city.thefloating.floatyplugin.nextbot;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class Nextbot {

  private final Mob pathfinder;
  private final AreaEffectCloud icon;
  private final Nextbot.Attributes attributes;

  private Instant lastJump = null;
  private final List<Player> activeMusic = new ArrayList<>();

  public Nextbot(
      final Mob pathfinder,
      final AreaEffectCloud icon,
      final Nextbot.Attributes attributes
  ) {
    this.pathfinder = pathfinder;
    this.icon = icon;
    this.attributes = attributes;
  }

  public Mob pathfinder() {
    return this.pathfinder;
  }

  public Mob pf() {
    return this.pathfinder();
  }

  public AreaEffectCloud icon() {
    return this.icon;
  }

  public AreaEffectCloud ic() {
    return this.icon();
  }

  public @Nullable Instant lastJump() {
    return this.lastJump;
  }

  public void lastJump(final Instant lastJump) {
    this.lastJump = lastJump;
  }

  public List<Player> activeMusic() {
    return this.activeMusic;
  }

  public Nextbot.Attributes attributes() {
    return this.attributes;
  }

  public Nextbot.Attributes attr() {
    return this.attributes();
  }

  public static final class Attributes {

    private final String iconChar;
    private final Sound music;
    private final SoundStop musicStop;

    public Attributes(
        final String iconChar,
        final Key musicKey
    ) {
      this.iconChar = iconChar;
      this.music = Sound.sound(musicKey, Sound.Source.HOSTILE, 1F, 1);
      this.musicStop = SoundStop.namedOnSource(musicKey, Sound.Source.HOSTILE);
    }

    public String iconChar() {
      return this.iconChar;
    }

    public Sound music() {
      return this.music;
    }

    public SoundStop musicStop() {
      return this.musicStop;
    }

  }

  public enum Type {
    OBUNGA(new Attributes("띂", Key.key("floating", "music.ussr_anthem.mono")));

    private final Attributes attributes;

    Type(final Attributes attributes) {
      this.attributes = attributes;
    }

    public Attributes attributes() {
      return this.attributes;
    }
  }

}
