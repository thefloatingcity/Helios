package xyz.tehbrian.floatyplugin.tag;

import com.google.inject.Inject;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class TagGame {

  private final LangConfig langConfig;

  private final Set<Player> playing = new HashSet<>();
  private final Map<Player, GameMode> previousGameModes = new HashMap<>();
  private Player it;

  private boolean noTagBacks;
  private Player lastIt;

  @Inject
  public TagGame(
      final LangConfig langConfig
  ) {
    this.langConfig = langConfig;
  }

  private static void removeAllPotionEffects(final Player player) {
    for (final PotionEffect effect : player.getActivePotionEffects()) {
      player.removePotionEffect(effect.getType());
    }
  }

  private void setGameMode(final Player player, final GameMode gameMode) {
    this.previousGameModes.put(player, player.getGameMode());
    player.setGameMode(gameMode);
  }

  private void setPreviousGameMode(final Player player) {
    player.setGameMode(Optional
        .ofNullable(this.previousGameModes.get(player))
        .orElse(player.getServer().getDefaultGameMode()));
  }

  public Set<Player> players() {
    return this.playing;
  }

  public void setPlaying(final Player player, final boolean value) {
    if (value) {
      this.playing.add(player);
      this.setGameMode(player, GameMode.ADVENTURE);

      removeAllPotionEffects(player);

      player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 100000, 100, true, false));
      player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100000, 100, true, false));
    } else {
      this.playing.remove(player);
      this.setPreviousGameMode(player);

      removeAllPotionEffects(player);

      if (player.equals(this.it)) {
        if (this.playing.iterator().hasNext()) {
          this.it(this.playing.iterator().next());
          this.it.sendMessage(this.langConfig.c(NodePath.path("tag", "now_it_because_leave")));
        } else {
          this.it(null);
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

  public Player it() {
    return this.it;
  }

  public void it(final @Nullable Player it) {
    if (this.it != null) {
      this.it.setGlowing(false);
    }

    this.it = it;
    if (this.it != null) {
      this.it.setGlowing(true);
    }
  }

  public boolean noTagBacks() {
    return this.noTagBacks;
  }

  public void noTagBacks(final boolean noTagBacks) {
    this.noTagBacks = noTagBacks;
  }

  public boolean toggleNoTagBacks() {
    this.noTagBacks(!this.noTagBacks());
    return this.noTagBacks();
  }

  public Player lastIt() {
    return this.lastIt;
  }

  public void lastIt(final Player lastIt) {
    this.lastIt = lastIt;
  }

}
