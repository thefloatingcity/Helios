package xyz.tehbrian.floatyplugin.listeners;

import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.config.ConfigConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;

@SuppressWarnings("unused")
public class VoidLoopListener implements Listener {

    private final FloatyPlugin floatyPlugin;
    private final ConfigConfig configConfig;
    private final LangConfig langConfig;

    @Inject
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
        final Entity entity = event.getEntity();
        final Location location = entity.getLocation();

        if (location.getY() > -50) {
            return;
        }
        event.setCancelled(true);


        final World.Environment environment = entity.getWorld().getEnvironment();

        final var engageY = switch (environment) {
            case THE_END -> -200;
            case NETHER -> -100;
            default -> -500;
        };

        if (location.getY() > engageY) {
            return;
        }

        Bukkit.getScheduler().runTask(this.floatyPlugin, () -> {
            final var teleportY = switch (environment) {
                case THE_END -> 500;
                case NETHER -> 400;
                default -> 650;
            };

            location.setY(teleportY);
            final var oldVelocity = entity.getVelocity();
            entity.teleport(location);
            entity.setVelocity(oldVelocity);

            if (!(entity instanceof final Player player)) {
                return;
            }

            final var spawnLocation = switch (environment) {
                case THE_END -> this.configConfig.spawn().end();
                case NETHER -> this.configConfig.spawn().nether();
                default -> this.configConfig.spawn().overworld();
            };

            if (player.getFallDistance() >= 3000) {
                player.sendMessage(this.langConfig.c(NodePath.path("warp", "max")));
                player.setFallDistance(0);
                player.teleport(spawnLocation);
                player.getWorld().strikeLightningEffect(spawnLocation);
                player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, spawnLocation, 1);
                player.getWorld().playSound(spawnLocation, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 4, 1);
            } else if (player.getFallDistance() >= 2000) {
                player.sendMessage(this.langConfig.c(NodePath.path("warp", "second")));
            } else if (player.getFallDistance() >= 1000) {
                player.sendMessage(this.langConfig.c(NodePath.path("warp", "first")));
            }
        });
    }

}
