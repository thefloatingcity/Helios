package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.Permissions;
import xyz.tehbrian.floatyplugin.world.FloatingWorld;
import xyz.tehbrian.floatyplugin.world.WorldService;

public class WorldCommands extends PaperCloudCommand<CommandSender> {

  private final WorldService worldService;

  @Inject
  public WorldCommands(
      final @NonNull WorldService worldService
  ) {
    this.worldService = worldService;
  }

  @Override
  public void register(final @NonNull PaperCommandManager<CommandSender> commandManager) {
    final var overworld = commandManager.commandBuilder("overworld")
        .meta(CommandMeta.DESCRIPTION, "Go to the overworld.")
        .permission(Permissions.WORLD_OVERWORLD)
        .senderType(Player.class)
        .handler(c -> ((Player) c.getSender()).teleport(this.worldService.getPlayerSpawnLocation(FloatingWorld.OVERWORLD)));

    final var nether = commandManager.commandBuilder("nether")
        .meta(CommandMeta.DESCRIPTION, "Go to the nether.")
        .permission(Permissions.WORLD_NETHER)
        .senderType(Player.class)
        .handler(c -> ((Player) c.getSender()).teleport(this.worldService.getPlayerSpawnLocation(FloatingWorld.NETHER)));

    final var end = commandManager.commandBuilder("end")
        .meta(CommandMeta.DESCRIPTION, "Go to the end.")
        .permission(Permissions.WORLD_END)
        .senderType(Player.class)
        .handler(c -> ((Player) c.getSender()).teleport(this.worldService.getPlayerSpawnLocation(FloatingWorld.END)));

    final var madlands = commandManager.commandBuilder("madlands")
        .meta(CommandMeta.DESCRIPTION, "Go to the madlands.")
        .permission(Permissions.WORLD_MADLANDS)
        .senderType(Player.class)
        .handler(c -> ((Player) c.getSender()).teleport(this.worldService.getPlayerSpawnLocation(FloatingWorld.MADLANDS)));

    commandManager.command(overworld);
    commandManager.command(nether);
    commandManager.command(end);
    commandManager.command(madlands);
  }

}
