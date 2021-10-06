package xyz.tehbrian.floatyplugin.listeners;

import broccolai.corn.paper.item.PaperItemBuilder;
import broccolai.corn.paper.item.special.BookBuilder;
import broccolai.corn.paper.item.special.BundleBuilder;
import broccolai.corn.paper.item.special.PotionBuilder;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FlightService;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.config.ConfigConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.user.User;
import xyz.tehbrian.floatyplugin.user.UserService;

/**
 * Ensures the following:
 * - no elytra anywhere but the end
 * - no sprinting in the nether
 * - no flying anywhere
 * - no spectator
 */
@SuppressWarnings("ClassCanBeRecord")
public final class TransportationListener implements Listener {

    private final LangConfig langConfig;
    private final FloatyPlugin floatyPlugin;
    private final FlightService flightService;
    private final ConfigConfig configConfig;
    private final UserService userService;

    @Inject
    public TransportationListener(
            final @NonNull LangConfig langConfig,
            final @NonNull FloatyPlugin floatyPlugin,
            final @NonNull FlightService flightService,
            final @NonNull ConfigConfig configConfig,
            final @NonNull UserService userService
    ) {
        this.langConfig = langConfig;
        this.floatyPlugin = floatyPlugin;
        this.flightService = flightService;
        this.configConfig = configConfig;
        this.userService = userService;
    }

    public void startTasks() {
        final Server server = this.floatyPlugin.getServer();

        server.getScheduler().scheduleSyncRepeatingTask(
                this.floatyPlugin,
                () -> {
                    for (final Player player : server.getOnlinePlayers()) {
                        final World.Environment environment = player.getWorld().getEnvironment();

                        this.flightService.checkFlight(player);

                        if (environment != World.Environment.THE_END) {
                            player.setGliding(false);
                        }

                        if (environment == World.Environment.NETHER) {
                            if (player.isSprinting()) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000000, 1, true, false, false));
                            }
                            player.setSprinting(false);

                            player.leaveVehicle();

                            switch (player.getLocation().add(0, -0.8, 0).getBlock().getType()) {
                                case ICE, PACKED_ICE, BLUE_ICE, FROSTED_ICE -> player.addPotionEffect(new PotionEffect(
                                        PotionEffectType.SLOW, 40, 3, true, false, false
                                ));
                                case SOUL_SAND, SOUL_SOIL -> player.addPotionEffect(new PotionEffect(
                                        PotionEffectType.SLOW, 40, 120, true, false, false
                                ));
                                default -> {
                                }
                            }
                        }
                    }
                }, 1, 10
        );
    }

    @EventHandler
    public void onToggleFlight(final PlayerToggleFlightEvent event) {
        this.flightService.checkFlight(event.getPlayer());
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        this.flightService.checkFlight(event.getPlayer());
    }

    @EventHandler
    public void onGameModeChange(final PlayerGameModeChangeEvent event) {
        final Player player = event.getPlayer();

        this.flightService.checkFlight(player);

        if (event.getNewGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            player.setGameMode(GameMode.ADVENTURE);
            player.setFireTicks(100);
            player.getWorld().strikeLightning(player.getLocation());
            player.sendMessage(this.langConfig.c(NodePath.path("no_spectator")));
        }
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent e) {
        final PlayerTeleportEvent.TeleportCause cause = e.getCause();
        final Player player = e.getPlayer();
        final World.Environment environment = player.getWorld().getEnvironment();

        switch (cause) {
            case ENDER_PEARL, CHORUS_FRUIT -> {
                if (environment == World.Environment.THE_END) {
                    return;
                }

                e.setCancelled(true);
                e.getTo().getWorld().spawnParticle(Particle.SMOKE_NORMAL, e.getTo(), 200, 0.1, 0.1, 0.1, 0.1);
            }
            case NETHER_PORTAL -> { // player is teleporting to/from nether but NOT making a new portal
                if (environment == World.Environment.NETHER) {
                    e.setTo(this.configConfig.playerSpawn().overworld());
                } else {
                    e.setTo(this.configConfig.playerSpawn().nether());
                }
            }
            case END_PORTAL -> { // player is teleporting to/from the end via end portal
                if (environment == World.Environment.THE_END) {
                    e.setTo(this.configConfig.playerSpawn().overworld());
                } else {
                    e.setTo(this.configConfig.playerSpawn().end());
                }
            }
            default -> {
            }
        }
    }

    @EventHandler
    public void onPortalCreate(final PortalCreateEvent e) {
        if (e.getEntity() instanceof Player player) {
            final PortalCreateEvent.CreateReason reason = e.getReason();
            final World.Environment environment = player.getWorld().getEnvironment();

            switch (reason) {
                case FIRE -> player.sendMessage(this.langConfig.c(NodePath.path("use_nether"))); // player made nether portal
                case NETHER_PAIR -> { // player is teleporting to/from the nether and making a new portal in the process
                    e.setCancelled(true); // no make the portal frame
                    if (environment == World.Environment.NETHER) {
                        player.teleport(this.configConfig.playerSpawn().overworld());
                    } else {
                        player.teleport(this.configConfig.playerSpawn().nether());
                    }
                }
                case END_PLATFORM -> e.setCancelled(true); // player is teleporting to the end and making the platform in the process
                default -> {
                }
            }
        }
    }

    @EventHandler
    public void onVehicleEnter(final VehicleEnterEvent e) {
        if (e.getEntered().getWorld().getEnvironment() != World.Environment.NETHER) {
            return;
        }

        if (e.getEntered() instanceof Player player) {
            final Vehicle vehicle = e.getVehicle();
            vehicle.getWorld().createExplosion(vehicle, 2, true, false);
            vehicle.remove();
            player.sendMessage(this.langConfig.c(NodePath.path("no_vehicle")));
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPotionEffect(final EntityPotionEffectEvent e) {
        if (!(e.getEntity() instanceof Player player)
                || player.getWorld().getEnvironment() != World.Environment.NETHER
                || e.getNewEffect() == null) {
            return;
        }

        final PotionEffectType type = e.getNewEffect().getType();
        if (type.equals(PotionEffectType.SPEED)
                || type.equals(PotionEffectType.JUMP)
                || type.equals(PotionEffectType.SLOW_FALLING)
                || type.equals(PotionEffectType.LEVITATION)) {
            e.setCancelled(true);
            player.setGameMode(GameMode.SURVIVAL);
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10000, 100, false, true, true));
        }
    }

    @EventHandler
    public void onElytra(final EntityToggleGlideEvent event) {
        if (event.getEntity().getWorld().getEnvironment() == World.Environment.THE_END
                || !(event.getEntity() instanceof Player player)) {
            return;
        }

        player.setGliding(false);

        if (event.isGliding()) {
            player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.MASTER, 100, 2);

            final PlayerInventory inventory = player.getInventory();
            if (inventory.getChestplate() != null && inventory.getChestplate().getType() == Material.ELYTRA) {
                inventory.setChestplate(new ItemStack(Material.AIR));
            }
        }
    }

    @EventHandler
    public void onSprint(final PlayerToggleSprintEvent event) {
        final Player player = event.getPlayer();
        if (player.getWorld().getEnvironment() != World.Environment.NETHER
                || !event.isSprinting()) {
            return;
        }

        player.setSprinting(false);

        player.getServer().getScheduler().scheduleSyncDelayedTask(this.floatyPlugin, () -> player.setSprinting(false), 5);

        final User user = this.userService.getUser(player);
        final var netherBlindnessCount = user.netherBlindnessCount();

        if (event.isSprinting()) {
            switch (netherBlindnessCount) {
                case 0, 2, 5, 10, 15, 20 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "1")));
                case 30 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "2")));
                case 50 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "3")));
                case 70 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "4")));
                case 100 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "5")));
                case 140 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "6")));
                case 180 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "7")));
                case 250 -> {
                    player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "8")));
                    player.getInventory().addItem(BundleBuilder.ofBundle()
                            .name(Component.text("Nether Watcher's Gift").color(NamedTextColor.RED))
                            .lore(Component.text("Maybe you should open it.").color(NamedTextColor.GRAY))
                            .addItem(
                                    PotionBuilder.ofType(Material.POTION)
                                            .name(Component.text("Femboy Hooters Sauce").color(NamedTextColor.LIGHT_PURPLE))
                                            .lore(Component.text("It doesn't smell very good..").color(NamedTextColor.GRAY))
                                            .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                                            .color(Color.WHITE)
                                            .build(),
                                    BookBuilder.ofType(Material.WRITTEN_BOOK)
                                            .title(Component.text("A Letter"))
                                            .author(Component.text("The Nether Watcher"))
                                            .addPage(Component
                                                    .text("listen, i appreciate ya givin' me company, but holy frik, the whole point of the nether is *not* to sprint, yet you somehow managed to do it upwards of 200 times!?? 'ave ya got somethin' wrong in the head??? love ya, but frik off")
                                                    .color(NamedTextColor.DARK_GRAY))
                                            .build(),
                                    PaperItemBuilder
                                            .ofType(Material.SLIME_BALL)
                                            .name(Component.text("Ball of Slime"))
                                            .lore(Component.text("It's uh.. a ball of slime.").color(NamedTextColor.GRAY))
                                            .build()
                            )
                            .build());
                }
                default -> {
                }
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4, true, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000000, 1, true, false, false));
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, SoundCategory.MASTER, 100, 0);
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, SoundCategory.MASTER, 100, 0);
            player.playSound(player.getLocation(), Sound.AMBIENT_WARPED_FOREST_MOOD, SoundCategory.MASTER, 100, 1);
        }

        user.netherBlindnessCount(netherBlindnessCount + 1);
    }

    @EventHandler
    public void onWorldChange(final PlayerChangedWorldEvent event) {
        event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
    }

}
