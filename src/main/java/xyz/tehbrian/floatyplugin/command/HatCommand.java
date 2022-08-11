package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.Permissions;
import xyz.tehbrian.floatyplugin.config.LangConfig;

public final class HatCommand extends PaperCloudCommand<CommandSender> {

  private final LangConfig langConfig;

  @Inject
  public HatCommand(
      final @NonNull LangConfig langConfig
  ) {
    this.langConfig = langConfig;
  }

  @Override
  public void register(final @NonNull PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("hat")
        .meta(CommandMeta.DESCRIPTION, "Put fancy things on your head!")
        .permission(Permissions.HAT)
        .senderType(Player.class)
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          final PlayerInventory inventory = sender.getInventory();
          final ItemStack heldItem = inventory.getItemInMainHand();

          if (heldItem.getType() == Material.AIR) {
            if (inventory.getHelmet() == null) {
              sender.sendMessage(this.langConfig.c(NodePath.path("hat", "none")));
            } else {
              inventory.setHelmet(new ItemStack(Material.AIR));
              sender.sendMessage(this.langConfig.c(NodePath.path("hat", "removed")));
            }
          } else {
            inventory.setHelmet(heldItem);
            sender.sendMessage(this.langConfig.c(NodePath.path("hat", "set")));
          }
        });

    commandManager.command(main);
  }

}
