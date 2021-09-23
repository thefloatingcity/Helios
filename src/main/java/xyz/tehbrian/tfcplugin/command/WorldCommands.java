package xyz.tehbrian.tfcplugin.command;

import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.config.ConfigConfig;

public class WorldCommands extends PaperCloudCommand<CommandSender> {

    private final ConfigConfig configConfig;

    @Inject
    public WorldCommands(final @NonNull ConfigConfig configConfig) {
        this.configConfig = configConfig;
    }

    /**
     * Register the command.
     *
     * @param commandManager the command manager
     */
    @Override
    public void register(@NonNull final PaperCommandManager<CommandSender> commandManager) {
        final var overworld = commandManager.commandBuilder("overworld")
                .meta(CommandMeta.DESCRIPTION, "Go to the overworld.")
                .senderType(Player.class)
                .handler((c) -> ((Player) c.getSender()).teleport(this.configConfig.spawn().overworld()));

        final var nether = commandManager.commandBuilder("nether")
                .meta(CommandMeta.DESCRIPTION, "Go to the nether.")
                .senderType(Player.class)
                .handler((c) -> ((Player) c.getSender()).teleport(this.configConfig.spawn().nether()));

        final var end = commandManager.commandBuilder("end")
                .meta(CommandMeta.DESCRIPTION, "Go to the end.")
                .senderType(Player.class)
                .handler((c) -> ((Player) c.getSender()).teleport(this.configConfig.spawn().end()));


        commandManager.command(overworld);
        commandManager.command(nether);
        commandManager.command(end);
    }

}
