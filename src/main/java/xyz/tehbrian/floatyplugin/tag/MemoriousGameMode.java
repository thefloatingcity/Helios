package xyz.tehbrian.floatyplugin.tag;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class MemoriousGameMode {

  private final Map<Player, GameMode> previousGameModes = new HashMap<>();

  public void set(final Player player, final GameMode gameMode) {
    this.previousGameModes.put(player, player.getGameMode());
    player.setGameMode(gameMode);
  }

  public void setPrevious(final Player player) {
    player.setGameMode(Optional
        .ofNullable(this.previousGameModes.get(player))
        .orElse(player.getServer().getDefaultGameMode()));
  }

}
