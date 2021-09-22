package xyz.tehbrian.tfcplugin.user;

import dev.tehbrian.tehlib.paper.user.PaperUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.tehbrian.tfcplugin.piano.Instrument;

import java.util.UUID;

public class User extends PaperUser {

    private boolean flyBypassEnabled = false;
    private boolean pianoEnabled = false;
    private Instrument instrument = Instrument.HARP;

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

    public boolean pianoEnabled() {
        return this.pianoEnabled;
    }

    public void pianoEnabled(final boolean pianoEnabled) {
        this.pianoEnabled = pianoEnabled;
    }

    public boolean togglePianoEnabled() {
        this.pianoEnabled(!this.pianoEnabled());
        return this.pianoEnabled();
    }

    public Instrument instrument() {
        return this.instrument;
    }

    public void instrument(final Instrument instrument) {
        this.instrument = instrument;
    }

}
