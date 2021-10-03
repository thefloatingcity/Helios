package xyz.tehbrian.floatyplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("unused")
public final class MilkListener implements Listener {

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
            for (final PotionEffect activeEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(activeEffect.getType());
            }
        }
    }

}
