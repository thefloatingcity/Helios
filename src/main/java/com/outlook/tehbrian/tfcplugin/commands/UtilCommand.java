package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import com.outlook.tehbrian.tfcplugin.Flight;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Utils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("util|utils")
@Description("Various utilities.")
public class UtilCommand extends BaseCommand {

    private final Main main;

    public UtilCommand(Main main) {
        this.main = main;
    }

    @CommandAlias("fly")
    @CommandPermission("tfcplugin.fly")
    @Description("Fly, like the birds in the sky.")
    public void onFly(Player player) {
        if (Flight.getCanBypassFly(player)) {
            Flight.setCanBypassFly(player, false);
            player.sendMessage(Utils.format("msg_fly_disabled"));
        } else {
            Flight.setCanBypassFly(player, true);
            player.sendMessage(Utils.format("msg_fly_enabled"));
        }
    }

    @CommandAlias("hat")
    @CommandPermission("tfcplugin.hat")
    @Description("Put things on your head!")
    public void onHat(Player player) {
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            if (player.getInventory().getHelmet() == null) {
                player.sendMessage(Utils.format("msg_hat_none"));
            } else {
                player.sendMessage(Utils.format("msg_hat_removed"));
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
            }
        } else {
            player.sendMessage(Utils.format("msg_hat_set"));
            player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Utils.format("msg_util_help"));
    }
}
