package city.thefloating.floatyplugin.nextbot;

import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ObungaCommand {

  private final Nate nate;

  @Inject
  public ObungaCommand(
      final Nate nate
  ) {
    this.nate = nate;
  }

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var obunga = commandManager.commandBuilder("obunga")
        .senderType(Player.class)
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.nate.createNextbot(Nextbot.Type.OBUNGA, sender.getLocation());
        });

    commandManager.command(obunga);
  }

}
