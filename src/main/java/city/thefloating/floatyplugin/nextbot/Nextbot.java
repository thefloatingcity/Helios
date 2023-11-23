package city.thefloating.floatyplugin.nextbot;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public final class Nextbot {

  private final Mob pathfinder;
  private final AreaEffectCloud icon;
  private final Nextbot.Attributes attributes;

  private Instant lastJump = null;
  private final Map<Player, Instant> startedMusic = new HashMap<>();

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

  public Map<Player, Instant> startedMusic() {
    return this.startedMusic;
  }

  public Nextbot.Attributes attributes() {
    return this.attributes;
  }

  public Nextbot.Attributes attr() {
    return this.attributes();
  }

  public enum Type {
    OBUNGA(new Attributes(Character.toString(0xF101), Key.key("floating", "nextbot.obunga"), Duration.ofSeconds(223))),
    C0(new Attributes(Character.toString(0xF200), Key.key("floating", "nextbot.c0"), Duration.ofSeconds(17))),
    C1(new Attributes(Character.toString(0xF201), Key.key("floating", "nextbot.c1"), Duration.ofSeconds(139))),
    C2(new Attributes(Character.toString(0xF202), Key.key("floating", "nextbot.c2"), Duration.ofSeconds(320))),
    C3(new Attributes(Character.toString(0xF203), Key.key("floating", "nextbot.c3"), Duration.ofSeconds(177))),
    C4(new Attributes(Character.toString(0xF204), Key.key("floating", "nextbot.c4"), Duration.ofSeconds(40))),
    C5(new Attributes(Character.toString(0xF205), Key.key("floating", "nextbot.c5"), Duration.ofSeconds(80))),
    C6(new Attributes(Character.toString(0xF206), Key.key("floating", "nextbot.c6"), Duration.ofSeconds(61))),
    C7(new Attributes(Character.toString(0xF207), Key.key("floating", "nextbot.c7"), Duration.ofSeconds(83))),
    C8(new Attributes(Character.toString(0xF208), Key.key("floating", "nextbot.c8"), Duration.ofSeconds(124))),
    C9(new Attributes(Character.toString(0xF209), Key.key("floating", "nextbot.c9"), Duration.ofSeconds(204)));

    private final Attributes attributes;

    Type(final Attributes attributes) {
      this.attributes = attributes;
    }

    public Attributes attributes() {
      return this.attributes;
    }
  }

  public static final class Attributes {

    private final String iconChar;
    private final Sound music;
    private final SoundStop musicStop;
    private final Duration musicLength;

    public Attributes(
        final String iconChar,
        final Key musicKey,
        final Duration musicLength
    ) {
      this.iconChar = iconChar;
      this.music = Sound.sound(musicKey, Sound.Source.MASTER, 1F, 1);
      this.musicStop = SoundStop.namedOnSource(musicKey, Sound.Source.MASTER);
      this.musicLength = musicLength.plus(Duration.ofSeconds(1)); // just some padding.
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

    public Duration musicLength() {
      return this.musicLength;
    }

  }

}
