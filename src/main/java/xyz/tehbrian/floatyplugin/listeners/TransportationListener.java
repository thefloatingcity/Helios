package xyz.tehbrian.floatyplugin.listeners;

import com.google.inject.Inject;
import org.bukkit.GameMode;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FlightService;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.config.LangConfig;

/**
 * Ensures the following:
 * - no elytra anywhere but the end
 * - no sprinting in the nether
 * - no flying anywhere
 * - no spectator
 */
public final class TransportationListener implements Listener {

    private final LangConfig langConfig;
    private final FloatyPlugin floatyPlugin;
    private final FlightService flightService;

    @Inject
    public TransportationListener(
            final @NonNull LangConfig langConfig,
            final @NonNull FloatyPlugin floatyPlugin,
            final @NonNull FlightService flightService
    ) {
        this.langConfig = langConfig;
        this.floatyPlugin = floatyPlugin;
        this.flightService = flightService;
    }

    public void startRedundancyCheckTasks() {
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
                            player.setSprinting(false);
                            if (player.isInsideVehicle()) {
                                player.leaveVehicle();
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
    public void onVehicleEnter(final VehicleEnterEvent e) {
        if (e.getEntered().getWorld().getEnvironment() != World.Environment.NETHER) {
            return;
        }

        if (e.getEntered() instanceof Player player) {
            final Vehicle vehicle = e.getVehicle();
            player.sendMessage(this.langConfig.c(NodePath.path("no_vehicle")));
            vehicle.getWorld().createExplosion(vehicle, 2, true, false);
            vehicle.remove();
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
                || type.equals(PotionEffectType.SLOW_FALLING)) {
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

        if (event.isGliding()) {
            player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.MASTER, 100, 2);

            final PlayerInventory inventory = player.getInventory();
            if (inventory.getChestplate() != null && inventory.getChestplate().getType() == Material.ELYTRA) {
                inventory.setChestplate(new ItemStack(Material.AIR));
            }
        }

        player.setGliding(false);
    }

    @EventHandler
    public void onSprint(final PlayerToggleSprintEvent event) {
        final Player player = event.getPlayer();
        if (player.getWorld().getEnvironment() != World.Environment.NETHER
                || !event.isSprinting()) {
            return;
        }

        if (event.isSprinting()) {
            player.sendMessage(this.langConfig.c(NodePath.path("no_sprint")));

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4, true, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000000, 1, true, false, false));
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, SoundCategory.MASTER, 100, 0);
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, SoundCategory.MASTER, 100, 0);
            player.playSound(player.getLocation(), Sound.AMBIENT_WARPED_FOREST_MOOD, SoundCategory.MASTER, 100, 1);
        }

        player.setSprinting(false);
    }

    @EventHandler
    public void onWorldChange(final PlayerChangedWorldEvent event) {
        event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
    }

}
