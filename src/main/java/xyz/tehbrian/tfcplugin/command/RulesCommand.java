package xyz.tehbrian.tfcplugin.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.serialize.SerializationException;
import xyz.tehbrian.tfcplugin.LuckPermsService;
import xyz.tehbrian.tfcplugin.config.BooksConfig;
import xyz.tehbrian.tfcplugin.config.LangConfig;
import xyz.tehbrian.tfcplugin.util.ConfigDeserializers;

import java.util.Objects;

public class RulesCommand extends PaperCloudCommand<CommandSender> {

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

    /**
     * Register the command.
     *
     * @param commandManager the command manager
     */
    @Override
    public void register(@NonNull final PaperCommandManager<CommandSender> commandManager) {
        final var rules = commandManager.commandBuilder("rules")
                .meta(CommandMeta.DESCRIPTION, "Um, the rules.");

        final var rulesPage = rules
                .argument(IntegerArgument.<CommandSender>newBuilder("page")
                        .withMin(1)
                        .withMax(9)
                        .asOptionalWithDefault(1)
                        .build())
                .handler((c) -> {
                    try {
                        for (final Component line : ConfigDeserializers.deserializePage(Objects
                                .requireNonNull(this.booksConfig.rootNode())
                                .node("books"), c.<Integer>get("page"))) {
                            c.getSender().sendMessage(line);
                        }
                    } catch (final SerializationException e) {
                        e.printStackTrace();
                    }
                });

        final var rulesAccept = rules.literal("accept", ArgumentDescription.of("Whew, that was a lot of reading."))
                .senderType(Player.class)
                .handler((c) -> {
                    final var player = (Player) c.getSender();
                    if (player.hasPermission("tfcplugin.build")) {
                        player.sendMessage(this.langConfig.c(NodePath.path("rules", "already_accepted")));
                    } else {
                        this.luckPermsService.addPlayerGroup(player, "passenger");
                        player.sendMessage(this.langConfig.c(NodePath.path("rules", "accept")));
                    }
                });

        commandManager.command(rules);
        commandManager.command(rulesPage);
        commandManager.command(rulesAccept);
    }

}
