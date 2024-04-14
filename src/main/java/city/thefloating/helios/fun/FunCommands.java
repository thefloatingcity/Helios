package city.thefloating.helios.fun;

import city.thefloating.helios.Permission;
import city.thefloating.helios.config.LangConfig;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;

import java.util.ArrayList;
import java.util.List;

public final class FunCommands {

  private final LangConfig langConfig;

  @Inject
  public FunCommands(
      final LangConfig langConfig
  ) {
    this.langConfig = langConfig;
  }

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var stringWithPlayerSuggestionsArgument = StringArgument
        .<CommandSender>builder("text")
        .greedy()
        .withSuggestionsProvider((c, i) -> this.onlinePlayerNames(c.getSender().getServer()));

    final var unreadable = commandManager.commandBuilder("unreadable")
        .meta(CommandMeta.DESCRIPTION, "Untransparent. Is that a word? Opaque?")
        .permission(Permission.UNREADABLE)
        .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
            NodePath.path("fun", "unreadable"),
            Placeholder.unparsed("player", c.getSender().getName())
        )));

    final var shrug = commandManager.commandBuilder("shrug")
        .meta(CommandMeta.DESCRIPTION, "You don't know. They don't know.")
        .permission(Permission.SHRUG)
        .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
            NodePath.path("fun", "shrug"),
            Placeholder.unparsed("player", c.getSender().getName())
        )));

    final var spook = commandManager.commandBuilder("spook")
        .meta(CommandMeta.DESCRIPTION, "OoooOOooOoOOoOOoo")
        .permission(Permission.SPOOK)
        .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
            NodePath.path("fun", "spook"),
            Placeholder.unparsed("player", c.getSender().getName())
        )));

    final var hug = commandManager.commandBuilder("hug")
        .meta(CommandMeta.DESCRIPTION, "D'aww that's so cute!")
        .permission(Permission.HUG)
        .argument(stringWithPlayerSuggestionsArgument.build())
        .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
            NodePath.path("fun", "hug"),
            TagResolver.resolver(
                Placeholder.unparsed("player", c.getSender().getName()),
                Placeholder.unparsed("text", c.get("text"))
            )
        )));

    final var smooch = commandManager.commandBuilder("smooch")
        .meta(CommandMeta.DESCRIPTION, "Give 'em a smooch.")
        .permission(Permission.SMOOCH)
        .argument(stringWithPlayerSuggestionsArgument.build())
        .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
            NodePath.path("fun", "smooch"),
            TagResolver.resolver(
                Placeholder.unparsed("player", c.getSender().getName()),
                Placeholder.unparsed("text", c.get("text"))
            )
        )));

    final var blame = commandManager.commandBuilder("blame")
        .meta(CommandMeta.DESCRIPTION, "It's their fault, not yours.")
        .permission(Permission.BLAME)
        .argument(stringWithPlayerSuggestionsArgument.build())
        .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
            NodePath.path("fun", "blame"),
            TagResolver.resolver(
                Placeholder.unparsed("player", c.getSender().getName()),
                Placeholder.unparsed("text", c.get("text"))
            )
        )));

    final var highfive = commandManager.commandBuilder("highfive")
        .meta(CommandMeta.DESCRIPTION, "Up high! Down low! Too slow!")
        .permission(Permission.HIGHFIVE)
        .argument(stringWithPlayerSuggestionsArgument.build())
        .handler(c -> c.getSender().getServer().sendMessage(this.langConfig.c(
            NodePath.path("fun", "highfive"),
            TagResolver.resolver(
                Placeholder.unparsed("player", c.getSender().getName()),
                Placeholder.unparsed("text", c.get("text"))
            )
        )));

    final var sue = commandManager.commandBuilder("sue")
        .permission(Permission.SUE)
        .meta(CommandMeta.DESCRIPTION, "Court fixes everything.. right?")
        .argument(stringWithPlayerSuggestionsArgument.asOptional().build())
        .handler(c -> c.<String>getOptional("text").ifPresentOrElse(
            (text) -> c
                .getSender()
                .getServer()
                .sendMessage(this.langConfig.c(
                    NodePath.path("fun", "sue-extra"),
                    TagResolver.resolver(
                        Placeholder.unparsed("player", c.getSender().getName()),
                        Placeholder.unparsed("text", text)
                    )
                )),
            () -> c
                .getSender()
                .getServer()
                .sendMessage(this.langConfig.c(
                    NodePath.path("fun", "sue"),
                    Placeholder.unparsed("player", c.getSender().getName())
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

  private List<String> onlinePlayerNames(final Server server) {
    final List<String> output = new ArrayList<>();

    for (final Player player : server.getOnlinePlayers()) {
      output.add(player.getName());
    }

    return output;
  }

}
