package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.Constants;
import xyz.tehbrian.floatyplugin.config.LangConfig;

import java.util.ArrayList;
import java.util.List;

public class FunCommands extends PaperCloudCommand<CommandSender> {

    private final LangConfig langConfig;

    @Inject
    public FunCommands(
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
        final var stringWithPlayerSuggestionsArgument = StringArgument
                .<CommandSender>newBuilder("text")
                .greedy()
                .withSuggestionsProvider((c, i) -> this.onlinePlayerNames(c.getSender().getServer()));

        final var unreadable = commandManager.commandBuilder("unreadable")
                .meta(CommandMeta.DESCRIPTION, "Untransparent. Is that a word? Opaque?")
                .permission(Constants.Permissions.UNREADABLE)
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("fun", "unreadable"),
                        PlaceholderResolver.placeholders(Placeholder.miniMessage("player", c.getSender().getName()))
                )));

        final var shrug = commandManager.commandBuilder("shrug")
                .meta(CommandMeta.DESCRIPTION, "You don't know. They don't know.")
                .permission(Constants.Permissions.SHRUG)
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("fun", "shrug"),
                        PlaceholderResolver.placeholders(Placeholder.miniMessage("player", c.getSender().getName()))
                )));

        final var spook = commandManager.commandBuilder("spook")
                .meta(CommandMeta.DESCRIPTION, "OoooOOooOoOOoOOoo")
                .permission(Constants.Permissions.SPOOK)
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("fun", "spook"),
                        PlaceholderResolver.placeholders(Placeholder.miniMessage("player", c.getSender().getName()))
                )));

        final var hug = commandManager.commandBuilder("hug")
                .meta(CommandMeta.DESCRIPTION, "D'aww that's so cute!")
                .permission(Constants.Permissions.HUG)
                .argument(stringWithPlayerSuggestionsArgument.build())
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("fun", "hug"),
                        PlaceholderResolver.placeholders(
                                Placeholder.miniMessage("player", c.getSender().getName()),
                                Placeholder.miniMessage("text", c.get("text"))
                        )
                )));

        final var smooch = commandManager.commandBuilder("smooch")
                .meta(CommandMeta.DESCRIPTION, "Give 'em a smooch.")
                .permission(Constants.Permissions.SMOOCH)
                .argument(stringWithPlayerSuggestionsArgument.build())
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("fun", "smooch"),
                        PlaceholderResolver.placeholders(
                                Placeholder.miniMessage("player", c.getSender().getName()),
                                Placeholder.miniMessage("text", c.get("text"))
                        )
                )));

        final var blame = commandManager.commandBuilder("blame")
                .meta(CommandMeta.DESCRIPTION, "It's their fault, not yours.")
                .permission(Constants.Permissions.BLAME)
                .argument(stringWithPlayerSuggestionsArgument.build())
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("fun", "blame"),
                        PlaceholderResolver.placeholders(
                                Placeholder.miniMessage("player", c.getSender().getName()),
                                Placeholder.miniMessage("text", c.get("text"))
                        )
                )));

        final var highfive = commandManager.commandBuilder("highfive")
                .meta(CommandMeta.DESCRIPTION, "Up high! Down low! Too slow!")
                .permission(Constants.Permissions.HIGHFIVE)
                .argument(stringWithPlayerSuggestionsArgument.build())
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("fun", "highfive"),
                        PlaceholderResolver.placeholders(
                                Placeholder.miniMessage("player", c.getSender().getName()),
                                Placeholder.miniMessage("text", c.get("text"))
                        )
                )));

        final var sue = commandManager.commandBuilder("sue")
                .permission(Constants.Permissions.SUE)
                .meta(CommandMeta.DESCRIPTION, "Court fixes everything.. right?")
                .argument(stringWithPlayerSuggestionsArgument.asOptional().build())
                .handler(c -> c.<String>getOptional("text").ifPresentOrElse(
                        (text) -> c
                                .getSender()
                                .getServer()
                                .sendMessage(this.langConfig.c(
                                        NodePath.path("fun", "sue_extra"),
                                        PlaceholderResolver.placeholders(
                                                Placeholder.miniMessage("player", c.getSender().getName()),
                                                Placeholder.miniMessage("text", text)
                                        )
                                )),
                        () -> c
                                .getSender()
                                .getServer()
                                .sendMessage(this.langConfig.c(
                                        NodePath.path("fun", "sue"),
                                        PlaceholderResolver.placeholders(Placeholder.miniMessage("player", c.getSender().getName()))
                                ))
                ));

        commandManager.command(unreadable);
        commandManager.command(shrug);
        commandManager.command(spook);
        commandManager.command(hug);
        commandManager.command(smooch);
        commandManager.command(blame);
        commandManager.command(highfive);
        commandManager.command(sue);
    }

    private @NonNull List<@NonNull String> onlinePlayerNames(final Server server) {
        final List<String> output = new ArrayList<>();

        for (final Player player : server.getOnlinePlayers()) {
            output.add(player.getName());
        }

        return output;
    }

}
