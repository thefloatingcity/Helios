package city.thefloating.floatyplugin.realm;

import city.thefloating.floatyplugin.Permission;
import city.thefloating.floatyplugin.config.LangConfig;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.spongepowered.configurate.NodePath;

/**
 * Prevents players from building in worlds if they lack the relevant permission.
 */
public final class WorldProtectionListener implements Listener {

  private final LangConfig langConfig;

  @Inject
  public WorldProtectionListener(
      final LangConfig langConfig
  ) {
    this.langConfig = langConfig;
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onBlockPlace(final BlockPlaceEvent event) {
    this.handle(event, event.getPlayer());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onBlockBreak(final BlockBreakEvent event) {
    this.handle(event, event.getPlayer());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onHangingPlace(final HangingPlaceEvent event) {
    if (event.getPlayer() != null) {
      this.handle(event, event.getPlayer());
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onHangingBreak(final HangingBreakByEntityEvent event) {
    if (event.getRemover() instanceof Player player) {
      this.handle(event, player);
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onBucketFill(final PlayerBucketFillEvent event) {
    this.handle(event);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
    this.handle(event);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onItemPickup(final PlayerAttemptPickupItemEvent event) {
    this.handle(event, false);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onItemDrop(final PlayerDropItemEvent event) {
    this.handle(event, false);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onInteract(final PlayerInteractEvent event) {
    this.handle(event, event.getAction() != Action.PHYSICAL);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Player player) {
      this.handle(event, player);
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onArmorStandManipulate(final PlayerArmorStandManipulateEvent event) {
    this.handle(event);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onInteractEntity(final PlayerInteractEntityEvent event) {
    this.handle(event);
  }

  private <T extends PlayerEvent & Cancellable> void handle(final T event, final boolean sendMessage) {
    this.handle(event, event.getPlayer(), sendMessage);
  }

  private <T extends Cancellable> void handle(final T event, final Player player) {
    this.handle(event, player, true);
  }

  private <T extends PlayerEvent & Cancellable> void handle(final T event) {
    this.handle(event, true);
  }

  private <T extends Cancellable> void handle(final T event, final Player player, final boolean sendMessage) {
    final var permission = switch (Realm.of(player)) {
      case MADLANDS -> Permission.REALM_MADLANDS;
      case OVERWORLD -> Permission.REALM_OVERWORLD;
      case NETHER -> Permission.REALM_NETHER;
      case END -> Permission.REALM_END;
      case BACKROOMS -> Permission.REALM_BACKROOMS;
    };

    if (!player.hasPermission(permission)) {
      if (sendMessage) {
        player.sendMessage(this.langConfig.c(NodePath.path("no-build")));
      }
      event.setCancelled(true);
    }
  }

}
