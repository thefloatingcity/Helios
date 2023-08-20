package city.thefloating.floatyplugin.staff;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import city.thefloating.floatyplugin.Format;
import city.thefloating.floatyplugin.Permission;

public final class BroadcastCommand extends PaperCloudCommand<CommandSender> {

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("broadcast")
        .meta(CommandMeta.DESCRIPTION, "Broadcast a message to the server.")
        .permission(Permission.BROADCAST)
        .argument(StringArgument.greedy("message"))
        .handler(c -> c.getSender().getServer().sendMessage(Format.miniMessage(c.<String>get("message"))));

    commandManager.command(main);
  }

}
