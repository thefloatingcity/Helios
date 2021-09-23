package xyz.tehbrian.tfcplugin.command;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.util.FormatUtil;

public class BroadcastCommand extends PaperCloudCommand<CommandSender> {

    /**
     * Register the command.
     *
     * @param commandManager the command manager
     */
    @Override
    public void register(@NonNull final PaperCommandManager<CommandSender> commandManager) {
        final var broadcast = commandManager.commandBuilder("broadcast")
                .meta(CommandMeta.DESCRIPTION, "Broadcast a server message.")
                .permission("tfcplugin.broadcast")
                .argument(StringArgument.greedy("message"))
                .handler((c) -> c.getSender().getServer().sendMessage(FormatUtil.miniMessage(c.<String>get("message"))));

        commandManager.command(broadcast);
    }

}
