package xyz.tehbrian.floatyplugin.transportation;

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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.Permissions;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.user.User;
import xyz.tehbrian.floatyplugin.user.UserService;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public final class TransportationListener implements Listener {

    private final LangConfig langConfig;
    private final FloatyPlugin floatyPlugin;
    private final FlightService flightService;
    private final UserService userService;

    @Inject
    public TransportationListener(
            final @NonNull LangConfig langConfig,
            final @NonNull FloatyPlugin floatyPlugin,
            final @NonNull FlightService flightService,
            final @NonNull UserService userService
    ) {
        this.langConfig = langConfig;
        this.floatyPlugin = floatyPlugin;
        this.flightService = flightService;
        this.userService = userService;
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
            default -> {
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
                case 0, 2, 5, 10 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "1")));
                case 20 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "2")));
                case 30 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "3")));
                case 40 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "4")));
                case 50 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "5")));
                case 60 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "6")));
                case 70 -> player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "7")));
                case 90 -> {
                    player.sendMessage(this.langConfig.c(NodePath.path("no_sprint", "8")));

                    final BundleBuilder bundleBuilder = BundleBuilder.ofBundle()
                            .name(Component.text("Nether Watcher's Gift").color(NamedTextColor.RED))
                            .lore(List.of(Component.text("Maybe you should open it.").color(NamedTextColor.GRAY)))
                            .addItem(
                                    BookBuilder.ofType(Material.WRITTEN_BOOK)
                                            .title(Component.text("A Letter"))
                                            .author(Component.text("The Nether Watcher"))
                                            .addPage(Component
                                                    .text("listen, i appreciate ya givin' me company, but holy frik, the whole point of the nether is *not* to sprint, yet you somehow managed to do it upwards of 50 times!?? 'ave ya got somethin' wrong in the head??? love ya, but frik off")
                                                    .color(NamedTextColor.DARK_GRAY))
                                            .build(),
                                    PaperItemBuilder
                                            .ofType(Material.SLIME_BALL)
                                            .name(Component.text("Ball of Slime"))
                                            .lore(List.of(Component.text("It's uh.. a ball of slime.").color(NamedTextColor.GRAY)))
                                            .build(),
                                    PaperItemBuilder
                                            .ofType(Material.GOLD_NUGGET)
                                            .name(Component.text("Gold Medal"))
                                            .lore(List.of(
                                                    Component.text("There's an inscription on").color(NamedTextColor.GRAY),
                                                    Component.text("the back. It says \"#1 Idiot\".").color(NamedTextColor.GRAY)
                                            ))
                                            .build()
                            );

                    if (player.hasPermission(Permissions.MILK)) {
                        bundleBuilder.addItem(PotionBuilder.ofType(Material.POTION)
                                .name(Component.text("Femboy Hooters Sauce").color(NamedTextColor.LIGHT_PURPLE))
                                .lore(List.of(Component.text("It doesn't smell very good..").color(NamedTextColor.GRAY)))
                                .addFlag(ItemFlag.HIDE_POTION_EFFECTS)
                                .color(Color.WHITE)
                                .build());
                    }

                    player.getInventory().addItem(bundleBuilder.build());
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
