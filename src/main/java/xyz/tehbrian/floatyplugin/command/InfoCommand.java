package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.spongepowered.configurate.CommentedConfigurationNode;
import xyz.tehbrian.floatyplugin.config.BooksConfig;
import xyz.tehbrian.floatyplugin.util.config.BookDeserializer;

public final class InfoCommand extends PaperCloudCommand<CommandSender> {

  private final BooksConfig booksConfig;

  @Inject
  public InfoCommand(
      final BooksConfig booksConfig
  ) {
    this.booksConfig = booksConfig;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("info")
        .meta(CommandMeta.DESCRIPTION, "Might wanna read this first.")
        .argument(IntegerArgument.<CommandSender>newBuilder("page")
            .withMin(1)
            .withMax(BookDeserializer.pageCount(this.getBookNode())) // .withMax doesn't take a supplier, cri every day
            .asOptionalWithDefault(1)
            .build())
        .handler(c -> c.getSender().sendMessage(
            BookDeserializer.deserializePage(this.getBookNode(), c.<Integer>get("page"))
        ));

    commandManager.command(main);
  }

  public CommentedConfigurationNode getBookNode() {
    return this.booksConfig.rootNode().node("info");
  }

}
