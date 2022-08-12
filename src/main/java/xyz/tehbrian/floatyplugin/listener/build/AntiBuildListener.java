package xyz.tehbrian.floatyplugin.listener.build;

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
import xyz.tehbrian.floatyplugin.Permissions;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.world.WorldService;

@SuppressWarnings({"unused", "ClassCanBeRecord"})
public final class AntiBuildListener implements Listener {

  private final LangConfig langConfig;
  private final WorldService worldService;

  @Inject
  public AntiBuildListener(final LangConfig langConfig, final WorldService worldService) {
    this.langConfig = langConfig;
    this.worldService = worldService;
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onBlockPlace(final BlockPlaceEvent event) {
    this.onAntiBuild(event, event.getPlayer());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onBlockBreak(final BlockBreakEvent event) {
    this.onAntiBuild(event, event.getPlayer());
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onHangingPlace(final HangingPlaceEvent event) {
    if (event.getPlayer() != null) {
      this.onAntiBuild(event, event.getPlayer());
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onHangingBreak(final HangingBreakByEntityEvent event) {
    if (event.getRemover() instanceof Player player) {
      this.onAntiBuild(event, player);
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onBucketFill(final PlayerBucketFillEvent event) {
    this.onAntiBuild(event);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
    this.onAntiBuild(event);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onItemPickup(final PlayerAttemptPickupItemEvent event) {
    this.onAntiBuild(event, false);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onItemDrop(final PlayerDropItemEvent event) {
    this.onAntiBuild(event, false);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onInteract(final PlayerInteractEvent event) {
    this.onAntiBuild(event, event.getAction() != Action.PHYSICAL);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
    if (event.getDamager() instanceof Player player) {
      this.onAntiBuild(event, player);
    }
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onArmorStandManipulate(final PlayerArmorStandManipulateEvent event) {
    this.onAntiBuild(event);
  }

  @EventHandler(priority = EventPriority.LOW)
  public void onInteractEntity(final PlayerInteractEntityEvent event) {
    this.onAntiBuild(event);
  }

  private <T extends PlayerEvent & Cancellable> void onAntiBuild(final T event, final boolean sendMessage) {
    this.onAntiBuild(event, event.getPlayer(), sendMessage);
  }

  private <T extends PlayerEvent & Cancellable> void onAntiBuild(final T event) {
    this.onAntiBuild(event, true);
  }

  private <T extends Cancellable> void onAntiBuild(final T event, final Player player, final boolean sendMessage) {
    final var permission = switch (this.worldService.getFloatingWorld(player.getWorld())) {
      case MADLANDS -> Permissions.BUILD_MADLANDS;
      case OVERWORLD -> Permissions.BUILD_OVERWORLD;
      case NETHER -> Permissions.BUILD_NETHER;
      case END -> Permissions.BUILD_END;
      case BACKROOMS -> Permissions.BUILD_BACKROOMS;
    };

    if (!player.hasPermission(permission)) {
      if (sendMessage) {
        player.sendMessage(this.langConfig.c(NodePath.path("no_build")));
      }
      event.setCancelled(true);
    }
  }

  private <T extends Cancellable> void onAntiBuild(final T event, final Player player) {
    this.onAntiBuild(event, player, true);
  }

}
