package xyz.tehbrian.floatyplugin.listeners;

import com.google.inject.Inject;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FlightService;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.config.LangConfig;

/**
 * Ensures the following:
 * - Elytra nowhere but the end
 * - No sprinting, fun stuff in the nether
 * - No flying absolutely anywhere
 */
public class TransportationListener implements Listener {

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
        this.flightService.checkFlight(event.getPlayer());
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        if (player.getWorld().getEnvironment() != World.Environment.THE_END) {
            player.setGliding(false);
        }
        if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
            player.setSprinting(false);
        }
    }

    @EventHandler
    public void onGameMode(final PlayerGameModeChangeEvent event) {
        final Player player = event.getPlayer();
        if (event.getNewGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            player.setGameMode(GameMode.ADVENTURE);
            player.setFireTicks(100);
            player.getWorld().strikeLightning(player.getLocation());
            player.sendMessage(this.langConfig.c(NodePath.path("no_spectator")));
        }
    }

    @EventHandler
    public void onElytra(final EntityToggleGlideEvent event) {
        if (event.getEntity().getWorld().getEnvironment() != World.Environment.THE_END) {
            if (event.getEntity() instanceof Player player) {
                player.setGliding(false);
            }
        }
    }

    @EventHandler
    public void onWorldChange(final PlayerChangedWorldEvent event) {
        event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
    }

    @EventHandler
    public void onSprint(final PlayerToggleSprintEvent event) {
        final Player player = event.getPlayer();
        if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
            if (!event.isSprinting()) {
                return;
            }

            player.sendMessage(this.langConfig.c(NodePath.path("no_sprint")));

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4, true, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1000000000, 1, true, false, false));
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, SoundCategory.MASTER, 100, 0);
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, SoundCategory.MASTER, 100, 0);

            player.getServer().getScheduler().scheduleSyncDelayedTask(this.floatyPlugin, () -> player.setSprinting(false), 3);
        }
    }

}
