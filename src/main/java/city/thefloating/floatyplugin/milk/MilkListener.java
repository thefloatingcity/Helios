package city.thefloating.floatyplugin.milk;

import city.thefloating.floatyplugin.config.LangConfig;
import city.thefloating.floatyplugin.realm.Milieu;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;

public final class MilkListener implements Listener {

  private final LangConfig langConfig;

  @Inject
  public MilkListener(
      final LangConfig langConfig
  ) {
    this.langConfig = langConfig;
  }

  @EventHandler
  public void onMilkSplashPotion(final EntityPotionEffectEvent event) {
    final @Nullable PotionEffect newEffect = event.getNewEffect();
    if (newEffect == null) {
      return;
    }

    if (newEffect.getType() == Milk.EFFECT
        && newEffect.getAmplifier() == Milk.AMPLIFIER
        && event.getEntity() instanceof final Player player) {
      event.setCancelled(true);
      player.removePotionEffect(Milk.EFFECT);

      // if potion effects are cleared the same tick a player starts sprinting,
      // it can allow them to sprint in the nether by bypassing blindness.
      // we prevent that by killing them. :)
      if (Milieu.of(player) == Milieu.ONEROUS && player.isSprinting()) {
        player.setHealth(0.0D);
        player.sendMessage(this.langConfig.c(NodePath.path("no-sprint-potion")));
        return;
      }

      for (final PotionEffect activeEffect : player.getActivePotionEffects()) {
        player.removePotionEffect(activeEffect.getType());
      }
    }
  }

}
