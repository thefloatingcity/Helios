package city.thefloating.floatyplugin.tag;

import city.thefloating.floatyplugin.config.LangConfig;
import city.thefloating.floatyplugin.realm.Realm;
import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.NodePath;

public final class TagListener implements Listener {

  private final TagGame tagGame;
  private final LangConfig langConfig;

  @Inject
  public TagListener(
      final TagGame tagGame,
      final LangConfig langConfig
  ) {
    this.tagGame = tagGame;
    this.langConfig = langConfig;
  }

  /**
   * Prevents potion effects other than damage resistance and saturation during tag.
   * <p>
   * Grants an exception for blindness in the nether.
   */
  @EventHandler
  public void onPotionEffect(final EntityPotionEffectEvent event) {
    if (!(event.getEntity() instanceof Player player)
        || !this.tagGame.isPlaying(player)
        || event.getNewEffect() == null) {
      return;
    }

    if (event.getNewEffect().getType().equals(PotionEffectType.BLINDNESS)
        && Realm.from(event.getEntity().getWorld()) == Realm.NETHER) {
      return;
    }

    if (!(event.getNewEffect().getType().equals(PotionEffectType.DAMAGE_RESISTANCE))
        && !(event.getNewEffect().getType().equals(PotionEffectType.SATURATION))) {
      event.setCancelled(true);
    }
  }

  /**
   * Handles "it" transfer.
   */
  @EventHandler
  public void onPunch(final EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Player damager
        && event.getEntity() instanceof Player victim
        && this.tagGame.isPlaying(damager)
        && this.tagGame.isPlaying(victim)
        && damager.equals(this.tagGame.it())) {
      if (this.tagGame.noTagBacks() && victim.equals(this.tagGame.lastIt())) {
        damager.sendMessage(this.langConfig.c(NodePath.path("tag", "no-tag-backs")));
        damager.playSound(damager.getEyeLocation(), Sound.ITEM_SHIELD_BREAK, 1, 0.9F);
        return;
      }

      this.tagGame.it(victim);
      victim.sendMessage(this.langConfig.c(NodePath.path("tag", "now-it")));
      victim.playSound(victim.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5F);
      damager.sendMessage(this.langConfig.c(
          NodePath.path("tag", "tagged-player"),
          Placeholder.component("player", victim.displayName())
      ));
      damager.playSound(damager.getEyeLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 0.7F);
    }
  }

  /**
   * Prevents game modes other than adventure mode during tag.
   */
  @EventHandler
  public void onGameModeChange(final PlayerGameModeChangeEvent event) {
    if (this.tagGame.isPlaying(event.getPlayer())
        && event.getNewGameMode() != GameMode.ADVENTURE) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(this.langConfig.c(NodePath.path("tag", "adventure-only")));
    }
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    this.tagGame.removePlayer(event.getPlayer());
  }

}
