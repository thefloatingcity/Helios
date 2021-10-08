package xyz.tehbrian.floatyplugin.tag;

import com.google.inject.Inject;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.FloatyPlugin;

import java.util.HashSet;
import java.util.Set;

public class TagService {

    private final FloatyPlugin floatyPlugin;

    private final Set<Player> playing = new HashSet<>();
    private Player it;

    @Inject
    public TagService(final @NonNull FloatyPlugin floatyPlugin) {
        this.floatyPlugin = floatyPlugin;
    }

    public Player it() {
        return it;
    }

    public void it(final Player it) {
        if (this.it != null) {
            this.it.setGlowing(false);
        }

        this.it = it;
        this.it.setGlowing(true);
    }

    public Set<Player> playing() {
        return playing;
    }

    public void setPlaying(final Player player, final boolean value) {
        if (value) {
            playing.add(player);
            player.setGameMode(GameMode.SURVIVAL);
        } else {
            playing.remove(player);
        }
    }

    public boolean isPlaying(final Player player) {
        return playing.contains(player);
    }

    public boolean togglePlaying(final Player player) {
        this.setPlaying(player, !this.isPlaying(player));
        return this.isPlaying(player);
    }

}
