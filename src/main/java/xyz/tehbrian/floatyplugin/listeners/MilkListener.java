package xyz.tehbrian.floatyplugin.listeners;

import com.google.inject.Inject;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;

@SuppressWarnings({"unused", "ClassCanBeRecord"})
public final class MilkListener implements Listener {

    private final LangConfig langConfig;

    @Inject
    public MilkListener(final @NonNull LangConfig langConfig) {
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

            // can be abused if potion effects are cleared at the same time
            // you sprint. sprint + blindness go brrrrr
            if (player.getWorld().getEnvironment() == World.Environment.NETHER
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
