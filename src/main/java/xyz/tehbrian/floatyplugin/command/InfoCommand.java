package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import xyz.tehbrian.floatyplugin.config.BooksConfig;
import xyz.tehbrian.floatyplugin.util.BookDeserializer;
import xyz.tehbrian.floatyplugin.util.SendMessage;

public final class InfoCommand extends PaperCloudCommand<CommandSender> {

    private final BooksConfig booksConfig;

    @Inject
    public InfoCommand(
            final @NonNull BooksConfig booksConfig
    ) {
        this.booksConfig = booksConfig;
    }

    /**
     * Register the command.
     *
     * @param commandManager the command manager
     */
    @Override
    public void register(@NonNull final PaperCommandManager<CommandSender> commandManager) {
        final var main = commandManager.commandBuilder("info")
                .meta(CommandMeta.DESCRIPTION, "Might wanna read this first.")
                .argument(IntegerArgument.<CommandSender>newBuilder("page")
                        .withMin(1)
                        .withMax(BookDeserializer.pageCount(this.getBookNode())) // .withMax doesn't take a supplier, cri every day
                        .asOptionalWithDefault(1)
                        .build())
                .handler(c -> SendMessage.s(c.getSender(), BookDeserializer.deserializePage(this.getBookNode(), c.<Integer>get("page"))));

        commandManager.command(main);
    }

    public CommentedConfigurationNode getBookNode() {
        return this.booksConfig.rootNode().node("info");
    }

}
