package city.thefloating.floatyplugin.milk;

import city.thefloating.floatyplugin.Permission;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MilkCommand extends PaperCloudCommand<CommandSender> {

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("milk")
        .meta(CommandMeta.DESCRIPTION, "Milk.")
        .permission(Permission.MILK)
        .senderType(Player.class)
        .handler(c -> {
          final var sender = (Player) c.getSender();
          sender.getInventory().addItem(MilkProvider.regular());
        });

    commandManager.command(main);
  }

}
