package xyz.tehbrian.floatyplugin.listeners;

import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.config.ConfigConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;

import java.time.Duration;

@SuppressWarnings("unused")
public final class VoidLoopListener implements Listener {

    private static final Title.Times INSTANT_IN_TIMES = Title.Times.of(
            Duration.ZERO,
            Duration.ofSeconds(4),
            Duration.ofSeconds(1)
    );

    private static final Title.Times FLASHING_TIMES = Title.Times.of(
            Duration.ofSeconds(1),
            Duration.ofSeconds(3),
            Duration.ofSeconds(1)
    );

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

    public void startTeleportationTask() {
        final Server server = this.floatyPlugin.getServer();
        final BukkitScheduler scheduler = server.getScheduler();

        scheduler.scheduleSyncRepeatingTask(
                this.floatyPlugin,
                () -> {
                    for (final Player player : server.getOnlinePlayers()) {
                        final World.Environment environment = player.getWorld().getEnvironment();
                        final Location location = player.getLocation();

                        if (location.getY() < this.lowEngage(environment)) { // they too low
                            Bukkit.getScheduler().runTask(this.floatyPlugin, () -> {
                                location.setY(this.lowTeleport(environment));
                                final var oldVelocity = player.getVelocity();
                                player.teleport(location);
                                player.setVelocity(oldVelocity);
                            });
                        } else if (location.getY() > this.highEngage(environment)) { // they too high
                            Bukkit.getScheduler().runTask(this.floatyPlugin, () -> {
                                location.setY(this.highTeleport(environment));
                                final var oldVelocity = player.getVelocity();
                                player.teleport(location);
                                player.setVelocity(oldVelocity);
                            });
                        }  // they're in the non-epic zone
                    }
                },
                0, 20
        );

        scheduler.scheduleSyncRepeatingTask(
                this.floatyPlugin,
                () -> {
                    for (final Player player : server.getOnlinePlayers()) {
                        final float fallDistance = player.getFallDistance();

                        if (fallDistance >= 4000) {
                            final Location spawnLocation = this.spawnLocation(player.getWorld().getEnvironment());

                            player.setFallDistance(0);
                            player.teleport(spawnLocation);

                            player.getWorld().strikeLightningEffect(spawnLocation);
                            player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, spawnLocation, 1);
                            player.getWorld().playSound(spawnLocation, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 4, 1);

                            player.showTitle(Title.title(
                                    this.langConfig.c(NodePath.path("warp", "max")),
                                    this.langConfig.c(NodePath.path("warp", "max_sub")),
                                    INSTANT_IN_TIMES
                            ));
                        } else if (fallDistance >= 3800) {
                            player.showTitle(Title.title(
                                    Component.empty(),
                                    this.langConfig.c(NodePath.path("warp", "fourth")),
                                    FLASHING_TIMES
                            ));

                            final Location location = player.getLocation();
                            for (int i = 0; i < 100; i = i + 2) {
                                scheduler.scheduleSyncDelayedTask(this.floatyPlugin,
                                        () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2), i
                                );
                            }
                        } else if (fallDistance >= 3400) {
                            player.showTitle(Title.title(
                                    Component.empty(),
                                    this.langConfig.c(NodePath.path("warp", "third")),
                                    FLASHING_TIMES
                            ));

                            final Location location = player.getLocation();
                            for (int i = 0; i < 100; i = i + 5) {
                                scheduler.scheduleSyncDelayedTask(this.floatyPlugin,
                                        () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 1.6F), i
                                );
                            }
                        } else if (fallDistance >= 3000) {
                            player.showTitle(Title.title(
                                    Component.empty(),
                                    this.langConfig.c(NodePath.path("warp", "second")),
                                    FLASHING_TIMES
                            ));

                            final Location location = player.getLocation();
                            for (int i = 0; i < 100; i = i + 10) {
                                scheduler.scheduleSyncDelayedTask(this.floatyPlugin,
                                        () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 0.9F), i
                                );
                            }
                        } else if (fallDistance >= 2000) {
                            player.showTitle(Title.title(
                                    Component.empty(),
                                    this.langConfig.c(NodePath.path("warp", "first")),
                                    FLASHING_TIMES
                            ));

                            final Location location = player.getLocation();
                            for (int i = 0; i < 100; i = i + 20) {
                                scheduler.scheduleSyncDelayedTask(this.floatyPlugin,
                                        () -> player.playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 0.5F), i
                                );
                            }
                        }
                    }
                },
                0, 100
        );
    }

    /**
     * Ensures that no entity will be damaged due to true void damage, meaning
     * void damage actually caused by the void. Additionally, offers a low-engage-only
     * void loop for all entities; whereas the scheduler void loop is player-only.
     *
     * @param event the event
     */
    @EventHandler
    public void onEntityDamageByVoid(final EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        final Location location = entity.getLocation();

        if (event.getCause() != EntityDamageEvent.DamageCause.VOID
                || location.getY() > -50) {
            return;
        }

        event.setCancelled(true);

        if (entity instanceof Player) {
            return; // players will be handled by the task; no need to handle them twice
        }

        final World.Environment environment = entity.getWorld().getEnvironment();

        if (location.getY() > this.lowEngage(environment)) {
            return;
        }

        Bukkit.getScheduler().runTask(this.floatyPlugin, () -> {
            location.setY(this.lowTeleport(environment));
            final var oldVelocity = entity.getVelocity();
            entity.teleport(location);
            entity.setVelocity(oldVelocity);
        });
    }

    private Location spawnLocation(final World.Environment environment) {
        return switch (environment) {
            case THE_END -> this.configConfig.spawn().end();
            case NETHER -> this.configConfig.spawn().nether();
            default -> this.configConfig.spawn().overworld();
        };
    }

    // Trouble understanding? No worries, I gotchu.
    // ibb.co/BtP8YZT

    private int lowEngage(final World.Environment environment) {
        return switch (environment) {
            case NETHER -> -100;
            case THE_END -> -170;
            default -> -260;
        };
    }

    private int lowTeleport(final World.Environment environment) {
        return this.highEngage(environment) - 10;
    }

    private int highEngage(final World.Environment environment) {
        return switch (environment) {
            case THE_END -> 460;
            case NETHER -> 350;
            default -> 470;
        };
    }

    private int highTeleport(final World.Environment environment) {
        return this.lowEngage(environment) + 10;
    }

}
