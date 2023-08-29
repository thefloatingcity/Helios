package city.thefloating.floatyplugin.ascension;

import city.thefloating.floatyplugin.DurationFormatter;
import city.thefloating.floatyplugin.LuckPermsService;
import city.thefloating.floatyplugin.config.LangConfig;
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

import java.util.concurrent.TimeUnit;

public final class AscendCommand extends PaperCloudCommand<CommandSender> {

  private final LangConfig langConfig;
  private final LuckPermsService luckPermsService;

  @Inject
  public AscendCommand(
      final LangConfig langConfig,
      final LuckPermsService luckPermsService
  ) {
    this.langConfig = langConfig;
    this.luckPermsService = luckPermsService;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("ascend")
        .meta(CommandMeta.DESCRIPTION, "Arise to a higher level.")
        .senderType(Player.class)
        .handler(c -> {
          final var sender = (Player) c.getSender();

          final Group nextGroup = this.luckPermsService.getNextGroupInTrack(sender, "player");
          if (nextGroup == null) {
            sender.sendMessage(this.langConfig.c(NodePath.path("ascend", "max")));
            return;
          }
          final Rank nextRank = Rank.from(nextGroup.getName());

          if (this.isEligibleForRank(sender, nextRank)) {
            this.luckPermsService.promoteInTrack(sender, "player");
            sender.sendMessage(this.langConfig.c(
                NodePath.path("ascend", "ascended"),
                Placeholder.unparsed("group", nextGroup.getName())
            ));
          } else {
            final var timeRequired = nextRank.playtimeRequired();

            if (timeRequired == null) {
              sender.sendMessage(this.langConfig.c(NodePath.path("ascend", "unattainable")));
              return;
            }

            final String fancyTime = DurationFormatter.fancifyTime(timeRequired, TimeUnit.HOURS);
            sender.sendMessage(this.langConfig.c(
                NodePath.path("ascend", "ineligible"),
                TagResolver.resolver(
                    Placeholder.unparsed("group", nextGroup.getName()),
                    Placeholder.unparsed("time", fancyTime)
                )
            ));
          }
        });

    commandManager.command(main);
  }

  private boolean isEligibleForRank(final Player player, final Rank rank) {
    final var timePlayed = Playtime.getTimePlayed(player);
    final var timeRequired = rank.playtimeRequired();
    if (timeRequired == null) {
      return false; // cannot be attained through time.
    }
    return timePlayed.compareTo(timeRequired) > 0;
  }

}
