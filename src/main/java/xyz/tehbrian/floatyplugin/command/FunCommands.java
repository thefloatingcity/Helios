package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.minimessage.Template;
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
    public void register(@NonNull final PaperCommandManager<CommandSender> commandManager) {
        final var stringWithPlayerSuggestionsArgument = StringArgument
                .<CommandSender>newBuilder("text")
                .greedy()
                .withSuggestionsProvider((c, i) -> this.onlinePlayerNames(c.getSender().getServer()))
                .build();

        final var optionalStringWithPlayerSuggestionsArgument = StringArgument
                .<CommandSender>newBuilder("text")
                .greedy()
                .withSuggestionsProvider((c, i) -> this.onlinePlayerNames(c.getSender().getServer()))
                .asOptional()
                .build();

        final var unreadable = commandManager.commandBuilder("unreadable")
                .meta(CommandMeta.DESCRIPTION, "Untransparent. Is that a word? Opaque?")
                .permission(Constants.Permissions.UNREADABLE)
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("emote", "unreadable"),
                        Template.of("player", c.getSender().getName())
                )));

        final var shrug = commandManager.commandBuilder("shrug")
                .meta(CommandMeta.DESCRIPTION, "You don't know. They don't know.")
                .permission(Constants.Permissions.SHRUG)
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("emote", "shrug"),
                        Template.of("player", c.getSender().getName())
                )));

        final var spook = commandManager.commandBuilder("spook")
                .meta(CommandMeta.DESCRIPTION, "OoooOOooOoOOoOOoo")
                .permission(Constants.Permissions.SPOOK)
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("emote", "spook"),
                        Template.of("player", c.getSender().getName())
                )));

        final var hug = commandManager.commandBuilder("hug")
                .meta(CommandMeta.DESCRIPTION, "D'aww that's so cute!")
                .permission(Constants.Permissions.HUG)
                .argument(stringWithPlayerSuggestionsArgument.copy())
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("emote", "hug"),
                        Template.of("player", c.getSender().getName()),
                        Template.of("text", c.<String>get("text"))
                )));

        final var kith = commandManager.commandBuilder("kith")
                .meta(CommandMeta.DESCRIPTION, "It's kiss, with a lisp.")
                .permission(Constants.Permissions.KITH)
                .argument(stringWithPlayerSuggestionsArgument.copy())
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("emote", "kith"),
                        Template.of("player", c.getSender().getName()),
                        Template.of("text", c.<String>get("text"))
                )));

        final var blame = commandManager.commandBuilder("blame")
                .meta(CommandMeta.DESCRIPTION, "It's their fault, not yours.")
                .permission(Constants.Permissions.BLAME)
                .argument(stringWithPlayerSuggestionsArgument.copy())
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("emote", "blame"),
                        Template.of("player", c.getSender().getName()),
                        Template.of("text", c.<String>get("text"))
                )));

        final var highfive = commandManager.commandBuilder("highfive")
                .meta(CommandMeta.DESCRIPTION, "Up high! Down low! Too slow!")
                .permission(Constants.Permissions.HIGHFIVE)
                .argument(stringWithPlayerSuggestionsArgument.copy())
                .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
                        NodePath.path("emote", "highfive"),
                        Template.of("player", c.getSender().getName()),
                        Template.of("text", c.<String>get("text"))
                )));

        final var sue = commandManager.commandBuilder("sue")
                .permission(Constants.Permissions.SUE)
                .meta(CommandMeta.DESCRIPTION, "Court fixes everything.. right?")
                .argument(optionalStringWithPlayerSuggestionsArgument.copy())
                .handler(c -> c.<String>getOptional("text").ifPresentOrElse(
                        (text) -> c
                                .getSender()
                                .getServer()
                                .sendMessage(this.langConfig.c(
                                        NodePath.path("emote", "sue_extra"),
                                        Template.of("player", c.getSender().getName()),
                                        Template.of("text", text)
                                )),
                        () -> c
                                .getSender()
                                .getServer()
                                .sendMessage(this.langConfig.c(
                                        NodePath.path("emote", "sue"),
                                        Template.of("player", c.getSender().getName())
                                ))
                ));

        commandManager.command(unreadable);
        commandManager.command(shrug);
        commandManager.command(spook);
        commandManager.command(hug);
        commandManager.command(kith);
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
