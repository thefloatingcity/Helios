package xyz.tehbrian.floatyplugin.tag;

import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;

@SuppressWarnings("ClassCanBeRecord")
public final class TagListener implements Listener {

  private final TagGame tagService;
  private final LangConfig langConfig;

  @Inject
  public TagListener(
      final TagGame tagService,
      final LangConfig langConfig
  ) {
    this.tagService = tagService;
    this.langConfig = langConfig;
  }

  @EventHandler
  public void onPotionEffect(final EntityPotionEffectEvent event) {
    if (!(event.getEntity() instanceof Player player)
        || !this.tagService.isPlaying(player)
        || event.getNewEffect() == null) {
      return;
    }

    if (event.getNewEffect().getType().equals(PotionEffectType.BLINDNESS)
        && event.getEntity().getWorld().getEnvironment() == World.Environment.NETHER) {
      return;
    }

    if (!(event.getNewEffect().getType().equals(PotionEffectType.DAMAGE_RESISTANCE))
        && !(event.getNewEffect().getType().equals(PotionEffectType.SATURATION))) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPunch(final EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Player damager
        && event.getEntity() instanceof Player victim
        && this.tagService.isPlaying(damager)
        && this.tagService.isPlaying(victim)
        && this.tagService.it().equals(damager)) {
      if (this.tagService.noTagBacks() && victim.equals(this.tagService.lastIt())) {
        damager.sendMessage(this.langConfig.c(NodePath.path("tag", "no_tag_backs")));
        damager.playSound(damager.getEyeLocation(), Sound.ITEM_SHIELD_BREAK, 1, 0.9F);
        return;
      }

      this.tagService.it(victim);
      this.tagService.lastIt(damager);
      victim.sendMessage(this.langConfig.c(NodePath.path("tag", "now_it")));
      victim.playSound(victim.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5F);
      damager.sendMessage(this.langConfig.c(
          NodePath.path("tag", "tagged_player"),
          Placeholder.component("player", victim.displayName())
      ));
      damager.playSound(damager.getEyeLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 0.7F);
    }
  }

  @EventHandler
  public void onGameModeChange(final PlayerGameModeChangeEvent event) {
    if (this.tagService.isPlaying(event.getPlayer())
        && event.getNewGameMode() != GameMode.ADVENTURE) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(this.langConfig.c(NodePath.path("tag", "adventure_only")));
    }
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    this.tagService.setPlaying(event.getPlayer(), false);
  }

}
