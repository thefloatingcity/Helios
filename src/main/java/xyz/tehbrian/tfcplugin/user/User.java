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
        return uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean hasFlyBypassEnabled() {
        return flyBypassEnabled;
    }

    public void setFlyBypassEnabled(boolean flyBypassEnabled) {
        this.flyBypassEnabled = flyBypassEnabled;

        if (hasFlyBypassEnabled()) {
            enableFlight();
        } else {
            disableFlight();
        }
    }

    public boolean toggleFlyBypassEnabled() {
        setFlyBypassEnabled(!hasFlyBypassEnabled());
        return hasFlyBypassEnabled();
    }

    public void enableFlight() {
        Player player = getPlayer();
        if (player.hasPermission("tfcplugin.core.fly") && hasFlyBypassEnabled()) {
            Bukkit.getScheduler().runTask(TFCPlugin.getInstance(), () -> {
                player.setAllowFlight(true);
                player.setFlying(true);
            });
        }
    }

    public void disableFlight() {
        Player player = getPlayer();
        if (!player.hasPermission("tfcplugin.core.fly") || !hasFlyBypassEnabled()) {
            Bukkit.getScheduler().runTask(TFCPlugin.getInstance(), () -> {
                player.setAllowFlight(false);
                player.setFlying(false);
            });
        }
    }

    public boolean hasPianoEnabled() {
        return pianoEnabled;
    }

    public void setPianoEnabled(boolean pianoEnabled) {
        this.pianoEnabled = pianoEnabled;
    }

    public boolean togglePianoEnabled() {
        setPianoEnabled(!hasPianoEnabled());
        return hasPianoEnabled();
    }

    public PianoSound getPianoSound() {
        return pianoSound;
    }

    public void setPianoSound(PianoSound pianoSound) {
        this.pianoSound = pianoSound;
    }
}
