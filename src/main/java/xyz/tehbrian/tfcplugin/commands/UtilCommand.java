package xyz.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.tehbrian.tfcplugin.util.ConfigUtils;
import xyz.tehbrian.tfcplugin.util.MiscUtils;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

@SuppressWarnings("unused")
@CommandAlias("util|utils")
@Description("Various utilities.")
public class UtilCommand extends BaseCommand {

    @CommandAlias("blocks")
    @CommandPermission("tfcplugin.util.blocks")
    @Description("Useful building blocks.")
    public void onBlocks(Player player) {
        player.openInventory(ConfigUtils.getInventory("inventories.blocks"));
    }

    @CommandAlias("broadcast")
    @CommandPermission("tfcplugin.util.broadcast")
    @Description("Broadcast a message to the server.")
    public void onBroadcast(CommandSender sender, String text) {
        Bukkit.broadcastMessage(MiscUtils.color(text));
    }

    @CommandAlias("hat")
    @CommandPermission("tfcplugin.util.hat")
    @Description("Put things on your head!")
    public void onHat(Player player) {
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            if (player.getInventory().getHelmet() == null) {
                player.sendMessage(new MsgBuilder().def("msg.hat.none").build());
            } else {
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
                player.sendMessage(new MsgBuilder().def("msg.hat.removed").build());
            }
        } else {
            player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
            player.sendMessage(new MsgBuilder().def("msg.hat.set").build());
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
