package xyz.tehbrian.floatyplugin.user;

import dev.tehbrian.tehlib.paper.user.PaperUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.piano.Instrument;

import java.util.UUID;

public final class User extends PaperUser {

  private final @NonNull Piano piano = new Piano(false, Instrument.HARP);
  private boolean flyBypassEnabled = false;
  private int netherBlindnessCount = 0;
  private boolean elevatorMusicPlaying = false;

  public User(final UUID uuid) {
    super(uuid);
  }

  public Player getPlayer() {
    return Bukkit.getPlayer(this.uuid);
  }

  public boolean flyBypassEnabled() {
    return this.flyBypassEnabled;
  }

  public void flyBypassEnabled(final boolean flyBypassEnabled) {
    this.flyBypassEnabled = flyBypassEnabled;
  }

  public boolean toggleFlyBypassEnabled() {
    this.flyBypassEnabled(!this.flyBypassEnabled());
    return this.flyBypassEnabled();
  }

  public int netherBlindnessCount() {
    return this.netherBlindnessCount;
  }

  public void netherBlindnessCount(final int netherBlindnessCount) {
    this.netherBlindnessCount = netherBlindnessCount;
  }

  public @NonNull Piano piano() {
    return this.piano;
  }

  public boolean elevatorMusicPlaying() {
    return this.elevatorMusicPlaying;
  }

  public void elevatorMusicPlaying(final boolean elevatorMusicPlaying) {
    this.elevatorMusicPlaying = elevatorMusicPlaying;
  }

  public static final class Piano {

    private boolean enabled;
    private Instrument instrument;

    public Piano(
        final boolean enabled,
        final Instrument instrument
    ) {
      this.enabled = enabled;
      this.instrument = instrument;
    }

    public boolean enabled() {
      return this.enabled;
    }

    public void enabled(final boolean enabled) {
      this.enabled = enabled;
    }

    public boolean toggleEnabled() {
      this.enabled(!this.enabled());
      return this.enabled();
    }

    public Instrument instrument() {
      return this.instrument;
    }

    public void instrument(final Instrument instrument) {
      this.instrument = instrument;
    }

  }

}
