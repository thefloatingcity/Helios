package city.thefloating.helios.ascension;

import city.thefloating.helios.DurationFormat;
import city.thefloating.helios.config.LangConfig;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;

import java.util.concurrent.TimeUnit;

public final class PlaytimeCommand {

  private final LangConfig langConfig;

  @Inject
  public PlaytimeCommand(
      final LangConfig langConfig
  ) {
    this.langConfig = langConfig;
  }

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("playtime")
        .meta(CommandMeta.DESCRIPTION, "Check how long you've played.")
        .senderType(Player.class)
        .argument(PlayerArgument.optional("player"))
        .handler(c -> {
          final var sender = (Player) c.getSender();

          c.<Player>getOptional("player").ifPresentOrElse((target) -> sender.sendMessage(this.langConfig.c(
              NodePath.path("playtime", "other"),
              TagResolver.resolver(
                  Placeholder.unparsed(
                      "time_in_hours",
                      DurationFormat.fancifyTime(Playtime.getTimePlayed(target), TimeUnit.HOURS)
                  ),
                  Placeholder.unparsed("time", DurationFormat.fancifyTime(Playtime.getTimePlayed(target))),
                  Placeholder.unparsed("player", target.getName())
              )
          )), () -> sender.sendMessage(this.langConfig.c(
              NodePath.path("playtime", "self"),
              TagResolver.resolver(
                  Placeholder.unparsed(
                      "time_in_hours",
                      DurationFormat.fancifyTime(Playtime.getTimePlayed(sender), TimeUnit.HOURS)
                  ),
                  Placeholder.unparsed("time", DurationFormat.fancifyTime(Playtime.getTimePlayed(sender)))
              )
          )));
        });

    commandManager.command(main);
  }

}
