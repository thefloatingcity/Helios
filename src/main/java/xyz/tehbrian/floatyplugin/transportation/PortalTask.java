package xyz.tehbrian.floatyplugin.transportation;

import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.Permissions;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.world.FloatingWorld;
import xyz.tehbrian.floatyplugin.world.WorldService;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>This task functions under the assumption that allow-end and
 * allow-nether are false in bukkit.yml and server.properties respectively.</b>
 */
@SuppressWarnings("ClassCanBeRecord")
public final class PortalTask {

  private static final Duration MESSAGE_COOLDOWN = Duration.ofSeconds(15);
  private static final Map<Player, Instant> LAST_MESSAGE_TIME = new HashMap<>();

  private final FloatyPlugin floatyPlugin;
  private final WorldService worldService;
  private final LangConfig langConfig;

  @Inject
  public PortalTask(
      final FloatyPlugin floatyPlugin,
      final WorldService worldService,
      final LangConfig langConfig
  ) {
    this.floatyPlugin = floatyPlugin;
    this.worldService = worldService;
    this.langConfig = langConfig;
  }

  /**
   * Attempts to send player the component, but if the last attempt was within
   * {@link #MESSAGE_COOLDOWN}, the message won't be sent.
   *
   * @param player    the player
   * @param component the component to send
   */
  private void sendRateLimitedMessage(final Player player, final Component component) {
    final var now = Instant.now();
    final var lastMessageTime = LAST_MESSAGE_TIME.get(player);
    if (lastMessageTime == null || Duration.between(lastMessageTime, now).compareTo(MESSAGE_COOLDOWN) > 0) {
      player.sendMessage(component);
      LAST_MESSAGE_TIME.put(player, now);
    }
  }

  public void start() {
    final Server server = this.floatyPlugin.getServer();

    server.getScheduler().scheduleSyncRepeatingTask(this.floatyPlugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        final @NotNull Material blockType = player.getLocation().getBlock().getType();
        if (blockType == Material.NETHER_PORTAL) {
          this.onNetherPortal(player);
        } else if (blockType == Material.END_PORTAL) {
          this.onEndPortal(player);
        }
      }
    }, 20, 4);
  }

  private void onNetherPortal(final Player player) {
    final FloatingWorld floatingWorld = this.worldService.getFloatingWorld(player.getWorld());

    switch (floatingWorld) {
      case MADLANDS -> {
        if (player.hasPermission(Permissions.WORLD_NETHER)) {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "wrong_world")));
        } else {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "no_permission")));
        }
      }
      case NETHER -> player.teleport(this.worldService.getPlayerSpawnLocation(FloatingWorld.OVERWORLD));
      default -> player.teleport(this.worldService.getPlayerSpawnLocation(FloatingWorld.NETHER));
    }
  }

  private void onEndPortal(final Player player) {
    final FloatingWorld floatingWorld = this.worldService.getFloatingWorld(player.getWorld());

    switch (floatingWorld) {
      case MADLANDS -> {
        if (player.hasPermission(Permissions.WORLD_END)) {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "wrong_world")));
        } else {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "no_permission")));
        }
      }
      case END -> { // vanilla behavior takes over, player goes to overworld
      }
      default -> player.teleport(this.worldService.getPlayerSpawnLocation(FloatingWorld.END));
    }
  }

}
