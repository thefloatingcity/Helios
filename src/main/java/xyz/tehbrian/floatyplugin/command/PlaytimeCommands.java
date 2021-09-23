package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.util.TimeFormatter;

import java.util.Map;

public class PlaytimeCommands extends PaperCloudCommand<CommandSender> {

    private final LangConfig langConfig;

    @Inject
    public PlaytimeCommands(
            final @NonNull LangConfig langConfig
    ) {
        this.langConfig = langConfig;
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
                .handler((c) -> {
                    final var sender = (Player) c.getSender();

                    c.<Player>getOptional("player").ifPresentOrElse((target) -> sender.sendMessage(this.langConfig.c(
                            NodePath.path("playtime", "other"),
                            Template.of("time", TimeFormatter.fancifyTime(getMillisPlayed(target))),
                            Template.of("player", target.getName())
                    )), () -> sender.sendMessage(this.langConfig.c(
                            NodePath.path("playtime", "self"),
                            Map.of("time", TimeFormatter.fancifyTime(getMillisPlayed(sender)))
                    )));
                });

        final var ascend = commandManager.commandBuilder("ascend")
                .meta(CommandMeta.DESCRIPTION, "Get fancy new perks!")
                .senderType(Player.class)
                .handler((c) -> c.getSender().sendMessage(Component.text("coming soon..").color(NamedTextColor.GRAY)));

        commandManager.command(playtime);
        commandManager.command(ascend);
    }

    private long getMillisPlayed(final Player player) {
        // liars. why was it changed from PLAY_ONE_TICK to PLAY_ONE_MINUTE? it's incremented every tick, not minute
        return (player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) * 1000L;
    }

}
