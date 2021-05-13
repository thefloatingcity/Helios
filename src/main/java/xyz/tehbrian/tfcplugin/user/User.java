package xyz.tehbrian.tfcplugin.user;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.tehbrian.tfcplugin.PianoSound;
import xyz.tehbrian.tfcplugin.TFCPlugin;

import java.util.UUID;

public class User {

    private final UUID uuid;

    private boolean flyBypassEnabled = false;
    private boolean pianoEnabled = false;

    private PianoSound pianoSound = PianoSound.HARP;

    public User(final UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public boolean hasFlyBypassEnabled() {
        return this.flyBypassEnabled;
    }

    public void setFlyBypassEnabled(boolean flyBypassEnabled) {
        this.flyBypassEnabled = flyBypassEnabled;

        if (this.hasFlyBypassEnabled()) {
            this.enableFlight();
        } else {
            this.disableFlight();
        }
    }

    public boolean toggleFlyBypassEnabled() {
        this.setFlyBypassEnabled(!this.hasFlyBypassEnabled());
        return this.hasFlyBypassEnabled();
    }

    public void enableFlight() {
        Player player = this.getPlayer();
        if (player.hasPermission("tfcplugin.core.fly") && this.hasFlyBypassEnabled()) {
            Bukkit.getScheduler().runTask(TFCPlugin.getInstance(), () -> {
                player.setAllowFlight(true);
                player.setFlying(true);
            });
        }
    }

    public void disableFlight() {
        Player player = this.getPlayer();
        if (!player.hasPermission("tfcplugin.core.fly") || !this.hasFlyBypassEnabled()) {
            Bukkit.getScheduler().runTask(TFCPlugin.getInstance(), () -> {
                player.setAllowFlight(false);
                player.setFlying(false);
            });
        }
    }

    public boolean hasPianoEnabled() {
        return this.pianoEnabled;
    }

    public void setPianoEnabled(boolean pianoEnabled) {
        this.pianoEnabled = pianoEnabled;
    }

    public boolean togglePianoEnabled() {
        this.setPianoEnabled(!this.hasPianoEnabled());
        return this.hasPianoEnabled();
    }

    public PianoSound getPianoSound() {
        return this.pianoSound;
    }

    public void setPianoSound(PianoSound pianoSound) {
        this.pianoSound = pianoSound;
    }
}
