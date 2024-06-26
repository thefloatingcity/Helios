package city.thefloating.helios.tag;

import city.thefloating.helios.PotEff;
import city.thefloating.helios.config.LangConfig;
import com.google.inject.Inject;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;

import java.util.HashSet;
import java.util.Set;

public final class TagGame {

  private final LangConfig langConfig;

  private final MemoriousGameMode gameMode = new MemoriousGameMode();

  private final Set<Player> playing = new HashSet<>();
  private Player it;

  private GlowSetting glowSetting = GlowSetting.IT;
  private boolean noTagBacks = false;
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

  private void setGlowing(final Player player) {
    if (!this.isPlaying(player)) {
      player.setGlowing(false);
      return;
    }

    switch (this.glowSetting) {
      case ALL -> player.setGlowing(true);
      case NOT_IT -> player.setGlowing(!player.equals(this.it()));
      case IT -> player.setGlowing(player.equals(this.it()));
      case NONE -> player.setGlowing(false);
      default -> {
      }
    }
  }

  public Set<Player> players() {
    return this.playing;
  }

  public void addPlayer(final Player player) {
    this.playing.add(player);
    this.gameMode.set(player, GameMode.ADVENTURE);

    removeAllPotionEffects(player);
    this.setGlowing(player);

    player.addPotionEffect(PotEff.hidden(PotionEffectType.SATURATION, PotEff.INF, 100));
    player.addPotionEffect(PotEff.hidden(PotionEffectType.DAMAGE_RESISTANCE, PotEff.INF, 100));
  }

  public void removePlayer(final Player player) {
    this.playing.remove(player);
    this.gameMode.setPrevious(player);

    removeAllPotionEffects(player);
    this.setGlowing(player);

    if (player.equals(this.it())) {
      if (this.playing.iterator().hasNext()) {
        this.it(this.playing.iterator().next());
        this.it().sendMessage(this.langConfig.c(NodePath.path("tag", "now-it-because-leave")));
      } else {
        this.it(null);
      }
    }
  }

  public boolean isPlaying(final Player player) {
    return this.playing.contains(player);
  }

  public boolean togglePlaying(final Player player) {
    if (this.isPlaying(player)) {
      this.removePlayer(player);
    } else {
      this.addPlayer(player);
    }
    return this.isPlaying(player);
  }

  public @Nullable Player it() {
    return this.it;
  }

  public void it(final @Nullable Player it) {
    this.lastIt = this.it;
    this.it = it;

    if (this.lastIt != null) {
      this.setGlowing(this.lastIt);
    }
    if (this.it != null) {
      this.setGlowing(this.it);
    }
  }

  public Player lastIt() {
    return this.lastIt;
  }

  public GlowSetting glowSetting() {
    return this.glowSetting;
  }

  public void glowSetting(final GlowSetting glowSetting) {
    this.glowSetting = glowSetting;
    for (final var player : this.playing) {
      this.setGlowing(player);
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

}
