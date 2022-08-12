package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.LuckPermsService;
import xyz.tehbrian.floatyplugin.Permissions;
import xyz.tehbrian.floatyplugin.config.BooksConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.util.config.BookDeserializer;

public final class RulesCommand extends PaperCloudCommand<CommandSender> {

  private final BooksConfig booksConfig;
  private final LuckPermsService luckPermsService;
  private final LangConfig langConfig;

  @Inject
  public RulesCommand(
      final @NonNull BooksConfig booksConfig,
      final @NonNull LuckPermsService luckPermsService,
      final @NonNull LangConfig langConfig
  ) {
    this.booksConfig = booksConfig;
    this.luckPermsService = luckPermsService;
    this.langConfig = langConfig;
  }

  @Override
  public void register(final @NonNull PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("rules")
        .meta(CommandMeta.DESCRIPTION, "Um, the rules.");

    final var page = main
        .argument(IntegerArgument.<CommandSender>newBuilder("page")
            .withMin(1)
            .withMax(BookDeserializer.pageCount(this.getBookNode())) // FIXME: won't work with /reload
            .asOptionalWithDefault(1)
            .build())
        .handler(c -> c.getSender().sendMessage(
            BookDeserializer.deserializePage(this.getBookNode(), c.<Integer>get("page"))
        ));

    final var accept = main.literal("accept", ArgumentDescription.of("Whew, that was a lot of reading."))
        .senderType(Player.class)
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          if (sender.hasPermission(Permissions.BUILD_MADLANDS)) {
            sender.sendMessage(this.langConfig.c(NodePath.path("rules", "already_accepted")));
          } else {
            this.luckPermsService.promoteInTrack(sender, "player");
            sender.sendMessage(this.langConfig.c(NodePath.path("rules", "accept")));
          }
        });

    commandManager.command(main);
    commandManager.command(page);
    commandManager.command(accept);
  }

  public CommentedConfigurationNode getBookNode() {
    return this.booksConfig.rootNode().node("rules");
  }

}
