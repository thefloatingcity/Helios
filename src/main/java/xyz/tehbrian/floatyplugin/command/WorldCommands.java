package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.Permissions;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.world.FloatingWorld;
import xyz.tehbrian.floatyplugin.world.WorldService;

import javax.annotation.Nullable;

public final class WorldCommands extends PaperCloudCommand<CommandSender> {

  private final FloatyPlugin plugin;
  private final WorldService worldService;
  private final LangConfig langConfig;

  @Inject
  public WorldCommands(
      final FloatyPlugin plugin,
      final WorldService worldService,
      final LangConfig langConfig
  ) {
    this.plugin = plugin;
    this.worldService = worldService;
    this.langConfig = langConfig;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var overworld = commandManager.commandBuilder("overworld")
        .meta(CommandMeta.DESCRIPTION, "Transpose to the overworld.")
        .permission(Permissions.WORLD_OVERWORLD)
        .senderType(Player.class)
        .handler(c -> this.transport((Player) c.getSender(), FloatingWorld.OVERWORLD));

    final var nether = commandManager.commandBuilder("nether")
        .meta(CommandMeta.DESCRIPTION, "Transpose to the nether.")
        .permission(Permissions.WORLD_NETHER)
        .senderType(Player.class)
        .handler(c -> this.transport((Player) c.getSender(), FloatingWorld.NETHER));

    final var end = commandManager.commandBuilder("end")
        .meta(CommandMeta.DESCRIPTION, "Transpose to the end.")
        .permission(Permissions.WORLD_END)
        .senderType(Player.class)
        .handler(c -> this.transport((Player) c.getSender(), FloatingWorld.END));

    final var madlands = commandManager.commandBuilder("madlands")
        .meta(CommandMeta.DESCRIPTION, "Transpose to the madlands.")
        .permission(Permissions.WORLD_MADLANDS)
        .senderType(Player.class)
        .handler(c -> this.transport((Player) c.getSender(), FloatingWorld.MADLANDS));

    final var backrooms = commandManager.commandBuilder("backrooms")
        .meta(CommandMeta.DESCRIPTION, "Transpose to the backrooms.")
        .permission(Permissions.WORLD_BACKROOMS)
        .senderType(Player.class)
        .handler(c -> this.transport((Player) c.getSender(), FloatingWorld.BACKROOMS));

    commandManager.command(overworld);
    commandManager.command(nether);
    commandManager.command(end);
    commandManager.command(madlands);
    commandManager.command(backrooms);
  }

  private void transport(final Player sender, final FloatingWorld destinationWorld) {
    final FloatingWorld currentWorld = this.worldService.getFloatingWorld(sender.getWorld());

    if (currentWorld == destinationWorld) {
      sender.sendMessage(this.langConfig.c(NodePath.path("transport", "already_there")));
      return;
    }

    this.setPreviousLocation(sender, currentWorld);

    final @Nullable Location destination = this.getPreviousLocation(sender, destinationWorld);
    if (destination != null) {
      sender.teleport(destination);
    } else {
      sender.teleport(this.worldService.getPlayerSpawnLocation(destinationWorld));
    }
  }

  private @Nullable Location getPreviousLocation(final Player player, final FloatingWorld world) {
    final WorldlessLocation wLoc = this.getLocation(player.getPersistentDataContainer(), this.dataKey(world));
    if (wLoc == null) {
      return null;
    }
    return new Location(
        this.worldService.getWorld(world),
        wLoc.x(), wLoc.y(), wLoc.z(),
        wLoc.yaw(), wLoc.pitch()
    );
  }

  private @Nullable WorldlessLocation getLocation(
      final PersistentDataContainer mainPdc,
      final NamespacedKey locPdcKey
  ) {
    if (!mainPdc.has(locPdcKey)) {
      return null;
    }
    final PersistentDataContainer dataPdc = mainPdc.get(locPdcKey, PersistentDataType.TAG_CONTAINER);
    assert dataPdc != null;
    return this.getLocation(dataPdc);
  }

  private @Nullable WorldlessLocation getLocation(final PersistentDataContainer pdc) {
    final Double x = pdc.get(this.key("x"), PersistentDataType.DOUBLE);
    final Double y = pdc.get(this.key("y"), PersistentDataType.DOUBLE);
    final Double z = pdc.get(this.key("z"), PersistentDataType.DOUBLE);
    final Float yaw = pdc.get(this.key("yaw"), PersistentDataType.FLOAT);
    final Float pitch = pdc.get(this.key("pitch"), PersistentDataType.FLOAT);

    if (x == null || y == null || z == null || yaw == null || pitch == null) {
      return null;
    }

    return new WorldlessLocation(x, y, z, yaw, pitch);
  }

  private void setPreviousLocation(final Player player, final FloatingWorld world) {
    this.setLocation(player.getPersistentDataContainer(), this.dataKey(world), player.getLocation());
  }

  private void setLocation(final PersistentDataContainer mainPdc, final NamespacedKey locPdcKey, final Location loc) {
    final PersistentDataContainer dataPdc = mainPdc.getAdapterContext().newPersistentDataContainer();
    this.setLocation(dataPdc, loc);
    mainPdc.set(locPdcKey, PersistentDataType.TAG_CONTAINER, dataPdc);
  }

  private void setLocation(final PersistentDataContainer pdc, final Location loc) {
    pdc.set(this.key("x"), PersistentDataType.DOUBLE, loc.getX());
    pdc.set(this.key("y"), PersistentDataType.DOUBLE, loc.getY());
    pdc.set(this.key("z"), PersistentDataType.DOUBLE, loc.getZ());
    pdc.set(this.key("yaw"), PersistentDataType.FLOAT, loc.getYaw());
    pdc.set(this.key("pitch"), PersistentDataType.FLOAT, loc.getPitch());
  }

  private NamespacedKey dataKey(final FloatingWorld world) {
    return this.key("previous-location-" + world.bukkitName());
  }

  private NamespacedKey key(final String key) {
    return new NamespacedKey(this.plugin, key);
  }

  private record WorldlessLocation(
      double x, double y, double z,
      float yaw, float pitch
  ) {

  }

}
