package xyz.tehbrian.floatyplugin.milk;

import broccolai.corn.paper.item.special.PotionBuilder;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.tehbrian.floatyplugin.Permission;

public final class MilkCommand extends PaperCloudCommand<CommandSender> {

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("milk")
        .meta(CommandMeta.DESCRIPTION, "Milk.")
        .permission(Permission.MILK)
        .senderType(Player.class)
        .handler(c -> {
          final var sender = (Player) c.getSender();
          sender.getInventory().addItem(PotionBuilder.ofType(Material.POTION)
              .name(Component.text("Potion of Milk"))
              .loreList(
                  Component.text("Milk XXXIV").color(NamedTextColor.BLUE),
                  Component.empty(),
                  Component.text("Bottled at your local femboy hooters.").color(NamedTextColor.GRAY)
              )
              .addFlag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS)
              .addCustomEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 100, 5), true)
              .color(Color.WHITE)
              .build());
        });

    commandManager.command(main);
  }

}