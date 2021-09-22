package xyz.tehbrian.tfcplugin.user;

import dev.tehbrian.tehlib.paper.user.PaperUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.piano.Instrument;

import java.util.UUID;

public class User extends PaperUser {

    private final @NonNull Piano piano = new Piano(false, Instrument.HARP);
    private boolean flyBypassEnabled = false;

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

    public @NonNull Piano piano() {
        return piano;
    }

    public static class Piano {

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
