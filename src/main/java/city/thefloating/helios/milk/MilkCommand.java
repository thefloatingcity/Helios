package city.thefloating.helios.milk;

import city.thefloating.helios.Permission;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MilkCommand {

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("milk")
        .meta(CommandMeta.DESCRIPTION, "Milk.")
        .permission(Permission.MILK)
        .senderType(Player.class)
        .handler(c -> {
          final var sender = (Player) c.getSender();
          sender.getInventory().addItem(Milk.regular());
        });

    commandManager.command(main);
  }

}
