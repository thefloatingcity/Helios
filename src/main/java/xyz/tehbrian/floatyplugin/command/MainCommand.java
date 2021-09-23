package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.config.LangConfig;

public class MainCommand extends PaperCloudCommand<CommandSender> {

    private final FloatyPlugin floatyPlugin;
    private final LangConfig langConfig;

    @Inject
    public MainCommand(
            final @NonNull FloatyPlugin floatyPlugin,
            final @NonNull LangConfig langConfig
    ) {
        this.floatyPlugin = floatyPlugin;
        this.langConfig = langConfig;
    }

    /**
     * Register the command.
     *
     * @param commandManager the command manager
     */
    @Override
    public void register(@NonNull final PaperCommandManager<CommandSender> commandManager) {
        final var main = commandManager.commandBuilder("tfc")
                .meta(CommandMeta.DESCRIPTION, "Core commands for TFC.");

        final var reload = main.literal("reload", ArgumentDescription.of("Reload the plugin's configs."))
                .permission("floatyplugin.reload")
                .handler((c) -> {
                    this.floatyPlugin.loadConfigs();
                    c.getSender().sendMessage(this.langConfig.c(NodePath.path("core", "reload")));
                });

        commandManager.command(main);
        commandManager.command(reload);
    }

}
