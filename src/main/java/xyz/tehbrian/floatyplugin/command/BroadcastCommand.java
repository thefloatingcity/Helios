package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.Permissions;
import xyz.tehbrian.floatyplugin.util.FormatUtil;

public class BroadcastCommand extends PaperCloudCommand<CommandSender> {

  @Override
  public void register(final @NonNull PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("broadcast")
        .meta(CommandMeta.DESCRIPTION, "Broadcast a server message.")
        .permission(Permissions.BROADCAST)
        .argument(StringArgument.greedy("message"))
        .handler(c -> c.getSender().getServer().sendMessage(FormatUtil.miniMessage(c.<String>get("message"))));

    commandManager.command(main);
  }

}
