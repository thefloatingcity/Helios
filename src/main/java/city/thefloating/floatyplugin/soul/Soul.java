package city.thefloating.floatyplugin.soul;

import city.thefloating.floatyplugin.piano.Instrument;

import java.util.UUID;

public final class Soul {

  private final UUID uuid;

  private boolean flyBypassEnabled = false;
  private int netherInfractions = 0;
  private boolean elevatorMusicPlaying = false;

  private final Piano piano = new Piano(false, Instrument.HARP);

  public Soul(final UUID uuid) {
    this.uuid = uuid;
  }

  public UUID getUuid() {
    return this.uuid;
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

  public int netherInfractions() {
    return this.netherInfractions;
  }

  public void netherInfractions(final int netherBlindnessCount) {
    this.netherInfractions = netherBlindnessCount;
  }

  public boolean elevatorMusicPlaying() {
    return this.elevatorMusicPlaying;
  }

  public void elevatorMusicPlaying(final boolean elevatorMusicPlaying) {
    this.elevatorMusicPlaying = elevatorMusicPlaying;
  }

  public Piano piano() {
    return this.piano;
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
