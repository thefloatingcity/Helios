package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.minimessage.Template;
import net.luckperms.api.model.group.Group;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.LuckPermsService;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.util.PlaytimeUtil;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlaytimeCommands extends PaperCloudCommand<CommandSender> {

    private final LangConfig langConfig;
    private final LuckPermsService luckPermsService;

    @Inject
    public PlaytimeCommands(
            final @NonNull LangConfig langConfig,
            final @NonNull LuckPermsService luckPermsService
    ) {
        this.langConfig = langConfig;
        this.luckPermsService = luckPermsService;
    }

    /**
     * Register the command.
     *
     * @param commandManager the command manager
     */
    @Override
    public void register(@NonNull final PaperCommandManager<CommandSender> commandManager) {
        final var playtime = commandManager.commandBuilder("playtime")
                .meta(CommandMeta.DESCRIPTION, "Check how long you've played.")
                .senderType(Player.class)
                .argument(PlayerArgument.optional("player"))
                .handler(c -> {
                    final var sender = (Player) c.getSender();

                    c.<Player>getOptional("player").ifPresentOrElse((target) -> sender.sendMessage(this.langConfig.c(
                            NodePath.path("playtime", "other"),
                            Template.of("time", PlaytimeUtil.fancifyTime(PlaytimeUtil.getTimePlayed(target))),
                            Template.of("player", target.getName())
                    )), () -> sender.sendMessage(this.langConfig.c(
                            NodePath.path("playtime", "self"),
                            Template.of("time", PlaytimeUtil.fancifyTime(PlaytimeUtil.getTimePlayed(sender)))
                    )));
                });

        final var ascend = commandManager.commandBuilder("ascend")
                .meta(CommandMeta.DESCRIPTION, "Get fancy new perks!")
                .senderType(Player.class)
                .handler(c -> {
                    final var sender = (Player) c.getSender();
                    final @Nullable Group nextGroup = this.luckPermsService.getNextGroup(sender, "player");

                    if (nextGroup == null) {
                        sender.sendMessage(this.langConfig.c(NodePath.path("ascend", "max")));
                        return;
                    }

                    if (isEligibleForPlayerGroup(sender, nextGroup.getName())) {
                        this.luckPermsService.promote(sender, "player");
                        sender.sendMessage(this.langConfig.c(NodePath.path("ascend", "ascended"), Map.of("group", nextGroup.getName())));
                    } else {
                        sender.sendMessage(this.langConfig.c(
                                NodePath.path("ascend", "ineligible"),
                                Map.of(
                                        "group", nextGroup.getName(),
                                        "time", PlaytimeUtil.fancifyTime(getTimeRequired(nextGroup.getName()), TimeUnit.HOURS)
                                )
                        ));
                    }
                });

        commandManager.command(playtime);
        commandManager.command(ascend);
    }

    private boolean isEligibleForPlayerGroup(final Player player, final String groupName) {
        final var timePlayed = PlaytimeUtil.getTimePlayed(player);
        return timePlayed.compareTo(getTimeRequired(groupName)) > 0;
    }

    private Duration getTimeRequired(final String groupName) {
        return switch (groupName) {
            case "passenger" -> Duration.ofHours(10000000);
            case "navigator" -> Duration.ofHours(1);
            case "pilot" -> Duration.ofHours(5);
            case "captain" -> Duration.ofHours(25);
            case "astronaut" -> Duration.ofHours(75);
            default -> throw new IllegalStateException("Unexpected value: " + groupName);
        };
    }

}
