package xyz.tehbrian.floatyplugin.transportation;

import com.google.inject.Inject;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.world.FloatingWorld;
import xyz.tehbrian.floatyplugin.world.WorldService;

/**
 * Guides players to commands when trying to use traditional methods
 * of world transportation.
 * <p>
 * <b>This listener functions under the assumption that allow-end and
 * allow-nether are false in bukkit.yml and server.properties respectively.</b>
 */
@SuppressWarnings("ClassCanBeRecord")
public final class PortalListener implements Listener {

    private final LangConfig langConfig;
    private final WorldService worldService;

    @Inject
    public PortalListener(
            final @NonNull LangConfig langConfig,
            final @NonNull WorldService worldService
    ) {
        this.langConfig = langConfig;
        this.worldService = worldService;
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent e) {
        final PlayerTeleportEvent.TeleportCause cause = e.getCause();
        final Player player = e.getPlayer();
        final World.Environment environment = player.getWorld().getEnvironment();

        switch (cause) {
            case NETHER_PORTAL -> { // player is teleporting to/from nether but NOT making a new portal
                if (environment == World.Environment.NETHER) {
                    e.setTo(this.worldService.getPlayerSpawnLocation(FloatingWorld.OVERWORLD));
                } else {
                    e.setTo(this.worldService.getPlayerSpawnLocation(FloatingWorld.NETHER));
                }
            }
            case END_PORTAL -> { // player is teleporting to/from the end via end portal
                if (environment == World.Environment.THE_END) {
                    e.setTo(this.worldService.getPlayerSpawnLocation(FloatingWorld.OVERWORLD));
                } else {
                    e.setTo(this.worldService.getPlayerSpawnLocation(FloatingWorld.END));
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
                        player.teleport(this.worldService.getPlayerSpawnLocation(FloatingWorld.OVERWORLD));
                    } else {
                        player.teleport(this.worldService.getPlayerSpawnLocation(FloatingWorld.NETHER));
                    }
                }
                case END_PLATFORM -> e.setCancelled(true); // player is teleporting to the end and making the platform in the process
                default -> {
                }
            }
        }
    }

}
