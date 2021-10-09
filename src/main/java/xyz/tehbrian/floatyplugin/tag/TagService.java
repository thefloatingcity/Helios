package xyz.tehbrian.floatyplugin.tag;

import com.google.inject.Inject;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class TagService {

    private final LangConfig langConfig;

    private final Map<Player, GameMode> previousGameModes = new HashMap<>();
    private final Set<Player> playing = new HashSet<>();
    private Player it;

    @Inject
    public TagService(final @NonNull LangConfig langConfig) {
        this.langConfig = langConfig;
    }

    public Player getIt() {
        return this.it;
    }

    public void setIt(final @Nullable Player it) {
        if (this.it != null) {
            this.it.setGlowing(false);
        }

        this.it = it;
        if (this.it != null) {
            this.it.setGlowing(true);
        }
    }

    public Set<Player> playing() {
        return this.playing;
    }

    public void setPlaying(final @NonNull Player player, final boolean value) {
        if (value) {
            this.playing.add(player);
            this.setGameMode(player, GameMode.ADVENTURE);
        } else {
            this.playing.remove(player);
            this.setPreviousGameMode(player);

            if (player.equals(this.it)) {
                if (this.playing.iterator().hasNext()) {
                    this.setIt(this.playing.iterator().next());
                    this.it.sendMessage(this.langConfig.c(NodePath.path("tag", "now_it_because_leave")));
                } else {
                    this.setIt(null);
                }
            }
        }
    }

    public boolean isPlaying(final Player player) {
        return this.playing.contains(player);
    }

    public boolean togglePlaying(final Player player) {
        this.setPlaying(player, !this.isPlaying(player));
        return this.isPlaying(player);
    }

    public void setGameMode(final Player player, final GameMode gameMode) {
        this.previousGameModes.put(player, player.getGameMode());
        player.setGameMode(gameMode);
    }

    public void setPreviousGameMode(final Player player) {
        player.setGameMode(Optional.ofNullable(this.previousGameModes.get(player)).orElse(player.getServer().getDefaultGameMode()));
    }

}
