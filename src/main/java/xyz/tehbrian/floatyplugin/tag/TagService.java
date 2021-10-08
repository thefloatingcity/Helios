package xyz.tehbrian.floatyplugin.tag;

import com.google.inject.Inject;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;

import java.util.HashSet;
import java.util.Set;

public class TagService {

    private final LangConfig langConfig;

    private final Set<Player> playing = new HashSet<>();
    private Player it;

    @Inject
    public TagService(final @NonNull LangConfig langConfig) {
        this.langConfig = langConfig;
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

            if (this.it == player) {
                player.setGlowing(false);

                if (this.playing.iterator().hasNext()) {
                    this.it = this.playing.iterator().next();
                    this.it.sendMessage(this.langConfig.c(NodePath.path("tag", "now_it_because_leave")));
                }
            }
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
