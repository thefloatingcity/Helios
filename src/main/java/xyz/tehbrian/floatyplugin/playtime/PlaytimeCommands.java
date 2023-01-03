package xyz.tehbrian.floatyplugin.playtime;

import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.luckperms.api.model.group.Group;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.LuckPermsService;
import xyz.tehbrian.floatyplugin.config.LangConfig;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class PlaytimeCommands extends PaperCloudCommand<CommandSender> {

  private final LangConfig langConfig;
  private final LuckPermsService luckPermsService;

  @Inject
  public PlaytimeCommands(
      final LangConfig langConfig,
      final LuckPermsService luckPermsService
  ) {
    this.langConfig = langConfig;
    this.luckPermsService = luckPermsService;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var playtime = commandManager.commandBuilder("playtime")
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
                      PlaytimeUtil.fancifyTime(PlaytimeUtil.getTimePlayed(target), TimeUnit.HOURS)
                  ),
                  Placeholder.unparsed("time", PlaytimeUtil.fancifyTime(PlaytimeUtil.getTimePlayed(target))),
                  Placeholder.unparsed("player", target.getName())
              )
          )), () -> sender.sendMessage(this.langConfig.c(
              NodePath.path("playtime", "self"),
              TagResolver.resolver(
                  Placeholder.unparsed(
                      "time_in_hours",
                      PlaytimeUtil.fancifyTime(PlaytimeUtil.getTimePlayed(sender), TimeUnit.HOURS)
                  ),
                  Placeholder.unparsed("time", PlaytimeUtil.fancifyTime(PlaytimeUtil.getTimePlayed(sender)))
              )
          )));
        });

    final var ascend = commandManager.commandBuilder("ascend")
        .meta(CommandMeta.DESCRIPTION, "Get fancy new perks!")
        .senderType(Player.class)
        .handler(c -> {
          final var sender = (Player) c.getSender();

          final Group nextGroup = this.luckPermsService.getNextGroupInTrack(sender, "player");
          if (nextGroup == null) {
            sender.sendMessage(this.langConfig.c(NodePath.path("ascend", "max")));
            return;
          }

          if (this.isEligibleForPlayerGroup(sender, nextGroup.getName())) {
            this.luckPermsService.promoteInTrack(sender, "player");
            sender.sendMessage(this.langConfig.c(
                NodePath.path("ascend", "ascended"),
                Placeholder.unparsed("group", nextGroup.getName())
            ));
          } else {
            sender.sendMessage(this.langConfig.c(
                NodePath.path("ascend", "ineligible"),
                TagResolver.resolver(
                    Placeholder.unparsed("group", nextGroup.getName()),
                    Placeholder.unparsed(
                        "time",
                        PlaytimeUtil.fancifyTime(this.getTimeRequired(nextGroup.getName()), TimeUnit.HOURS)
                    )
                )
            ));
          }
        });

    commandManager.command(playtime);
    commandManager.command(ascend);
  }

  private boolean isEligibleForPlayerGroup(final Player player, final String groupName) {
    final var timePlayed = PlaytimeUtil.getTimePlayed(player);
    return timePlayed.compareTo(this.getTimeRequired(groupName)) > 0;
  }

  private Duration getTimeRequired(final String groupName) {
    return switch (groupName) {
      case "boarding", "passenger" -> Duration.ofHours(10000000);
      case "navigator" -> Duration.ofHours(1);
      case "pilot" -> Duration.ofHours(5);
      case "captain" -> Duration.ofHours(25);
      case "astronaut" -> Duration.ofHours(75);
      default -> throw new IllegalStateException("Unexpected value: " + groupName);
    };
  }

}
