package xyz.tehbrian.floatyplugin.transportation;

import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.Permission;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.user.UserService;

public final class FlyCommand extends PaperCloudCommand<CommandSender> {

  private final UserService userService;
  private final LangConfig langConfig;
  private final FlightService flightService;

  @Inject
  public FlyCommand(
      final UserService userService,
      final LangConfig langConfig,
      final FlightService flightService
  ) {
    this.userService = userService;
    this.langConfig = langConfig;
    this.flightService = flightService;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("fly")
        .meta(CommandMeta.DESCRIPTION, "Bends the space/time continuum.")
        .permission(Permission.FLY)
        .senderType(Player.class)
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          if (this.userService.getUser(sender).toggleFlyBypassEnabled()) {
            sender.sendMessage(this.langConfig.c(NodePath.path("fly", "enabled")));
            this.flightService.enableFlight(sender);
          } else {
            sender.sendMessage(this.langConfig.c(NodePath.path("fly", "disabled")));
            this.flightService.disableFlight(sender);
          }
        });

    commandManager.command(main);
  }

}
