package city.thefloating.floatyplugin.milk;

import city.thefloating.floatyplugin.realm.Realm;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;
import city.thefloating.floatyplugin.config.LangConfig;

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

    if (newEffect.getType().equals(PotionEffectType.DOLPHINS_GRACE)
        && newEffect.getAmplifier() == 5
        && event.getEntity() instanceof Player player) {
      event.setCancelled(true);
      player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);

      // if potion effects are cleared the same tick a player starts sprinting,
      // it can allow them to sprint in the nether by bypassing blindness.
      // we prevent that by killing them. :)
      if (Realm.from(player.getWorld()) == Realm.NETHER
          && player.isSprinting()) {
        player.setHealth(0.0D);
        player.sendMessage(this.langConfig.c(NodePath.path("no_sprint_potion")));
        return;
      }

      for (final PotionEffect activeEffect : player.getActivePotionEffects()) {
        player.removePotionEffect(activeEffect.getType());
      }
    }
  }

}