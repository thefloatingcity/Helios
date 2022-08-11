package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.tag.TagService;

public class TagCommand extends PaperCloudCommand<CommandSender> {

  private final LangConfig langConfig;
  private final TagService tagService;

  @Inject
  public TagCommand(
      final @NonNull LangConfig langConfig,
      final @NonNull TagService tagService
  ) {
    this.langConfig = langConfig;
    this.tagService = tagService;
  }

  @Override
  public void register(final @NonNull PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("tag")
        .meta(CommandMeta.DESCRIPTION, "Joins/leaves the game of tag.")
        .senderType(Player.class)
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          if (this.tagService.togglePlaying(sender)) {
            if (this.tagService.playing().size() <= 1) {
              sender.sendMessage(this.langConfig.c(NodePath.path("tag", "join_first")));
              this.tagService.setIt(sender);
            } else {
              sender.sendMessage(this.langConfig.c(NodePath.path("tag", "join")));
            }
          } else {
            sender.sendMessage(this.langConfig.c(NodePath.path("tag", "leave")));
          }
        });

    final var ntb = main.literal("ntb", ArgumentDescription.of("Toggles no tag backs."))
        .handler(c -> {
          if (this.tagService.toggleNoTagBacks()) {
            c.getSender().sendMessage(this.langConfig.c(NodePath.path("tag", "no_tag_backs_enabled")));
          } else {
            c.getSender().sendMessage(this.langConfig.c(NodePath.path("tag", "no_tag_backs_disabled")));
          }
        });

    commandManager.command(main);
    commandManager.command(ntb);
  }

}
