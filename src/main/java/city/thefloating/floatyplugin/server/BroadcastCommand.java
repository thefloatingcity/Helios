package city.thefloating.floatyplugin.server;

import city.thefloating.floatyplugin.Format;
import city.thefloating.floatyplugin.Permission;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;

public final class BroadcastCommand {

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("broadcast")
        .meta(CommandMeta.DESCRIPTION, "Broadcast a message to the server.")
        .permission(Permission.BROADCAST)
        .argument(StringArgument.greedy("message"))
        .handler(c -> c.getSender().getServer().sendMessage(Format.miniMessage(c.<String>get("message"))));

    commandManager.command(main);
  }

}
