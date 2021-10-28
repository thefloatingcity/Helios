package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.Constants;
import xyz.tehbrian.floatyplugin.config.LangConfig;

import java.util.Map;

public class GamemodeCommands extends PaperCloudCommand<CommandSender> {

    private final LangConfig langConfig;

    @Inject
    public GamemodeCommands(
            final @NonNull LangConfig langConfig
    ) {
        this.langConfig = langConfig;
    }

    /**
     * Register the command.
     *
     * @param commandManager the command manager
     */
    @Override
    public void register(final @NonNull PaperCommandManager<CommandSender> commandManager) {
        final var main = commandManager.commandBuilder("gamemode", "gm")
                .permission(Constants.Permissions.GAMEMODE)
                .meta(CommandMeta.DESCRIPTION, "Change gamemodes.");

        final var survival = main.literal("survival", ArgumentDescription.of("Change to survival."), "s", "0")
                .handler(c -> {
                    final Player sender = (Player) c.getSender();
                    sender.setGameMode(GameMode.SURVIVAL);
                    sender.sendMessage(this.langConfig.c(
                            NodePath.path("gamemode", "change"),
                            TemplateResolver.pairs(Map.of("gamemode", "survival"))
                    ));
                });

        final var creative = main.literal("creative", ArgumentDescription.of("Change to creative."), "c", "1")
                .handler(c -> {
                    final Player sender = (Player) c.getSender();
                    sender.setGameMode(GameMode.CREATIVE);
                    sender.sendMessage(this.langConfig.c(
                            NodePath.path("gamemode", "change"),
                            TemplateResolver.pairs(Map.of("gamemode", "creative"))
                    ));
                });

        final var adventure = main.literal("adventure", ArgumentDescription.of("Change to adventure."), "a", "2")
                .handler(c -> {
                    final Player sender = (Player) c.getSender();
                    sender.setGameMode(GameMode.ADVENTURE);
                    sender.sendMessage(this.langConfig.c(
                            NodePath.path("gamemode", "change"),
                            TemplateResolver.pairs(Map.of("gamemode", "adventure"))
                    ));
                });

        commandManager.command(main);
        commandManager.command(survival);
        commandManager.command(creative);
        commandManager.command(adventure);
    }

}
