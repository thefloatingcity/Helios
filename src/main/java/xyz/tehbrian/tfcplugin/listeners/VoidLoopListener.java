package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.tfcplugin.FloatyPlugin;
import xyz.tehbrian.tfcplugin.config.ConfigConfig;
import xyz.tehbrian.tfcplugin.config.LangConfig;

@SuppressWarnings("unused")
public class VoidLoopListener implements Listener {

    private final FloatyPlugin floatyPlugin;
    private final ConfigConfig configConfig;
    private final LangConfig langConfig;

    public VoidLoopListener(
            final @NonNull FloatyPlugin floatyPlugin,
            final @NonNull ConfigConfig configConfig,
            final @NonNull LangConfig langConfig
    ) {
        this.floatyPlugin = floatyPlugin;
        this.configConfig = configConfig;
        this.langConfig = langConfig;
    }

    @EventHandler
    public void onEntityDamageByVoid(final EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
            return;
        }
        final Location location = event.getEntity().getLocation();

        if (location.getY() > -50) {
            return;
        }
        event.setCancelled(true);

        if (location.getY() > -450) {
            return;
        }
        Bukkit.getScheduler().runTask(this.floatyPlugin, () -> {
            location.setY(650);
            event.getEntity().teleport(location);

            if (!(event.getEntity() instanceof final Player player)) {
                return;
            }

            if (player.getFallDistance() >= 3000) {
                player.sendMessage(this.langConfig.c(NodePath.path("warp", "max")));
                player.setFallDistance(0);
                player.teleport(configConfig.spawn());
                player.getWorld().strikeLightningEffect(configConfig.spawn());
                player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, configConfig.spawn(), 1);
                player.getWorld().playSound(
                        configConfig.spawn(),
                        Sound.ENTITY_GENERIC_EXPLODE,
                        SoundCategory.MASTER,
                        4,
                        1
                );
            } else if (player.getFallDistance() >= 2000) {
                player.sendMessage(this.langConfig.c(NodePath.path("warp", "second")));
            } else if (player.getFallDistance() >= 1000) {
                player.sendMessage(this.langConfig.c(NodePath.path("warp", "first")));
            }
        });
    }

}
