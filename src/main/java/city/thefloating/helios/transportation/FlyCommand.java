package city.thefloating.helios.transportation;

import city.thefloating.helios.Permission;
import city.thefloating.helios.config.LangConfig;
import city.thefloating.helios.soul.Charon;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;

public final class FlyCommand {

  private final Charon charon;
  private final LangConfig langConfig;
  private final FlightService flightService;

  @Inject
  public FlyCommand(
      final Charon charon,
      final LangConfig langConfig,
      final FlightService flightService
  ) {
    this.charon = charon;
    this.langConfig = langConfig;
    this.flightService = flightService;
  }

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("fly")
        .meta(CommandMeta.DESCRIPTION, "Bends the space-time continuum.")
        .permission(Permission.FLY)
        .senderType(Player.class)
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          if (this.charon.grab(sender).toggleFlyBypassEnabled()) {
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
