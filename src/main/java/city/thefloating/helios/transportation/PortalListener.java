package city.thefloating.helios.transportation;

import city.thefloating.helios.Helios;
import city.thefloating.helios.Permission;
import city.thefloating.helios.config.LangConfig;
import city.thefloating.helios.realm.Realm;
import city.thefloating.helios.realm.Transposer;
import com.google.inject.Inject;
import io.papermc.paper.event.entity.EntityInsideBlockEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.spongepowered.configurate.NodePath;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Sends players to the correct realms upon entering a portal.
 * <p>
 * Messages players in the madlands, backrooms, or other disconnected realms
 * information regarding their circumstance.
 * <p>
 * <b>This task functions under the assumption that allow-end and
 * allow-nether are false in bukkit.yml and server.properties respectively.</b>
 */
public final class PortalListener implements Listener {

  private static final Duration MESSAGE_COOLDOWN = Duration.ofSeconds(15);

  private static final Random RANDOM = new Random();

  private final Map<Player, Instant> lastMessageTime = new HashMap<>();
  private final Map<Player, Integer> lastPortalAttempt = new HashMap<>();

  private final Helios helios;
  private final Transposer transposer;
  private final LangConfig langConfig;

  @Inject
  public PortalListener(
      final Helios helios,
      final Transposer transposer,
      final LangConfig langConfig
  ) {
    this.helios = helios;
    this.transposer = transposer;
    this.langConfig = langConfig;
  }

  public void attemptPortal(final Player player) {
    this.lastPortalAttempt.put(player, this.helios.getServer().getCurrentTick());
  }

  @EventHandler
  public void onPortal(final EntityInsideBlockEvent event) {
    final Material portal = event.getBlock().getType();
    if (portal == Material.NETHER_PORTAL || portal == Material.END_PORTAL) {
      event.setCancelled(true);
    }

    if (!(event.getEntity() instanceof final Player player)) {
      return;
    }

    if (this.lastPortalAttempt.containsKey(player)
        && this.helios.getServer().getCurrentTick() - this.lastPortalAttempt.get(player) < 5) {
      this.attemptPortal(player);
      return;
    }
    this.attemptPortal(player);

    if (portal == Material.NETHER_PORTAL) {
      this.onNetherPortal(player);
    } else if (portal == Material.END_PORTAL) {
      this.onEndPortal(player);
    }
  }

  @EventHandler
  public void onNetherPortalCreate(final PlayerPortalEvent event) {
    // this event is called when a nether portal would be created in the overworld.
    event.setCancelled(true);
  }

  private void onNetherPortal(final Player player) {
    switch (Realm.of(player)) {
      case MADLANDS -> {
        if (player.hasPermission(Permission.REALM_NETHER)) {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "in-madlands")));
        } else {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "in-madlands-no-permission")));
        }
      }
      case BACKROOMS -> this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "in-backrooms")));
      case NETHER -> this.fuzzyTranspose(player, Realm.OVERWORLD);
      default -> this.fuzzyTranspose(player, Realm.NETHER);
    }
  }

  private void onEndPortal(final Player player) {
    switch (Realm.of(player)) {
      case MADLANDS -> {
        if (player.hasPermission(Permission.REALM_END)) {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "in-madlands")));
        } else {
          this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "in-madlands-no-permission")));
        }
      }
      case BACKROOMS -> this.sendRateLimitedMessage(player, this.langConfig.c(NodePath.path("portal", "in-backrooms")));
      case END -> this.fuzzyTranspose(player, Realm.OVERWORLD);
      default -> this.fuzzyTranspose(player, Realm.END);
    }
  }

  /**
   * Transpose player with random chance to transpose to the backrooms.
   *
   * @param player the player to transpose
   * @param realm  the realm to possibly transpose player to
   */
  private void fuzzyTranspose(final Player player, final Realm realm) {
    // random chance to noclip into the backrooms. 5% chance.
    if (RANDOM.nextFloat() < 0.05) {
      this.transposer.noclipIntoBackrooms(player);
    } else {
      this.transposer.transpose(player, realm);
    }
  }

  /**
   * Attempts to send player the message. If the last message was sent within
   * {@link #MESSAGE_COOLDOWN}, the message won't be sent.
   *
   * @param player  the player
   * @param message the component to send
   */
  private void sendRateLimitedMessage(final Player player, final Component message) {
    final var now = Instant.now();
    final var lastMessage = this.lastMessageTime.get(player);
    if (lastMessage == null || Duration.between(lastMessage, now).compareTo(MESSAGE_COOLDOWN) > 0) {
      player.sendMessage(message);
      this.lastMessageTime.put(player, now);
    }
  }

}
