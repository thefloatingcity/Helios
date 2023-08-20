package city.thefloating.floatyplugin.transportation;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.realm.Realm;
import city.thefloating.floatyplugin.realm.WorldService;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;
import city.thefloating.floatyplugin.Permission;
import city.thefloating.floatyplugin.config.LangConfig;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Sends players messages regarding portal functionality on attempted use if
 * they're in the madlands. Sends them to the correct realms if they are not.
 * <p>
 * <b>This task functions under the assumption that allow-end and
 * allow-nether are false in bukkit.yml and server.properties respectively.</b>
 */
public final class PortalUseTask {

  private static final Duration MESSAGE_COOLDOWN = Duration.ofSeconds(15);
  private static final Map<Player, Instant> LAST_MESSAGE_TIME = new HashMap<>();

  private final FloatyPlugin plugin;
  private final WorldService worldService;
  private final LangConfig langConfig;

  @Inject
  public PortalUseTask(
      final FloatyPlugin plugin,
      final WorldService worldService,
      final LangConfig langConfig
  ) {
    this.plugin = plugin;
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
    final Server server = this.plugin.getServer();

    server.getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
      for (final Player player : server.getOnlinePlayers()) {
        final Material blockType = player.getLocation().getBlock().getType();
        if (blockType == Material.NETHER_PORTAL) {
          this.onNetherPortal(player);
        } else if (blockType == Material.END_PORTAL) {
          this.onEndPortal(player);
        }
      }
    }, 20, 4);
  }

  private void onNetherPortal(final Player player) {
    final Realm realm = Realm.from(player.getWorld());

    switch (realm) {
      case MADLANDS -> {
        if (player.hasPermission(Permission.WORLD_NETHER)) {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "wrong_world")));
        } else {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "no_permission")));
        }
      }
      case BACKROOMS -> this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "backrooms")));
      case NETHER -> player.teleport(this.worldService.getSpawnPoint(Realm.OVERWORLD));
      default -> player.teleport(this.worldService.getSpawnPoint(Realm.NETHER));
    }
  }

  private void onEndPortal(final Player player) {
    final Realm realm = Realm.from(player.getWorld());

    switch (realm) {
      case MADLANDS -> {
        if (player.hasPermission(Permission.WORLD_END)) {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "wrong_world")));
        } else {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "no_permission")));
        }
      }
      case BACKROOMS -> this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "backrooms")));
      case END -> { // vanilla behavior takes over, player goes to overworld.
      }
      default -> player.teleport(this.worldService.getSpawnPoint(Realm.END));
    }
  }

}
