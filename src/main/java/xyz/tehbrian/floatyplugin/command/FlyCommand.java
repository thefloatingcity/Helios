package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FlightService;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.user.UserService;

public class FlyCommand extends PaperCloudCommand<CommandSender> {

    private final UserService userService;
    private final LangConfig langConfig;
    private final FlightService flightService;

    @Inject
    public FlyCommand(
            final @NonNull UserService userService,
            final @NonNull LangConfig langConfig,
            final @NonNull FlightService flightService
    ) {
        this.userService = userService;
        this.langConfig = langConfig;
        this.flightService = flightService;
    }

    /**
     * Register the command.
     *
     * @param commandManager the command manager
     */
    @Override
    public void register(@NonNull final PaperCommandManager<CommandSender> commandManager) {
        final var main = commandManager.commandBuilder("fly")
                .meta(CommandMeta.DESCRIPTION, "Bends the space/time continuum.")
                .permission("floatyplugin.fly")
                .senderType(Player.class)
                .handler(c -> {
                    final Player player = (Player) c.getSender();
                    if (this.userService.getUser(player).toggleFlyBypassEnabled()) {
                        player.sendMessage(this.langConfig.c(NodePath.path("fly", "enabled")));
                        this.flightService.enableFlight(player);
                    } else {
                        player.sendMessage(this.langConfig.c(NodePath.path("fly", "disabled")));
                        this.flightService.disableFlight(player);
                    }
                });

        commandManager.command(main);
    }

}
