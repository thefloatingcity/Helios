package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import com.outlook.tehbrian.tfcplugin.Flight;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.utils.ItemBuilder;
import com.outlook.tehbrian.tfcplugin.utils.MiscUtils;
import com.outlook.tehbrian.tfcplugin.utils.MsgBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
@CommandAlias("util|utils")
@Description("Various utilities.")
public class UtilCommand extends BaseCommand {

    private final Main main;

    public UtilCommand(Main main) {
        this.main = main;
    }

    @CommandAlias("fly")
    @CommandPermission("tfcplugin.fly")
    @Description("Fly. Like the birds in the sky.")
    public void onFly(Player player) {
        if (Flight.getPlayerCanBypassFly(player)) {
            Flight.setPlayerCanBypassFly(player, false);
            player.sendMessage(new MsgBuilder().def("msg_fly_disabled").build());
        } else {
            Flight.setPlayerCanBypassFly(player, true);
            player.sendMessage(new MsgBuilder().def("msg_fly_enabled").build());
        }
    }

    @CommandAlias("blocks")
    @CommandPermission("tfcplugin.blocks")
    @Description("Useful bulding blocks.")
    public void onBlocks(Player player) {
        Inventory blocksInventory = Bukkit.createInventory(null, 9, MiscUtils.color(main.getConfig().getString("blocks_inventory_name")));
        for (String key : main.getConfig().getConfigurationSection("blocks").getKeys(false)) {
            ConfigurationSection block = main.getConfig().getConfigurationSection("blocks." + key);
            blocksInventory.addItem(new ItemBuilder(Material.matchMaterial(block.getString("material")))
                    .name(block.getString("name"))
                    .build());
        }
        player.openInventory(blocksInventory);
    }

    @CommandAlias("broadcast")
    @CommandPermission("tfcplugin.broadcast")
    @Description("Broadcast a message to the server.")
    public void onBroadcast(CommandSender sender, String text) {
        Bukkit.broadcastMessage(MiscUtils.color(text));
    }

    @CommandAlias("hat")
    @CommandPermission("tfcplugin.hat")
    @Description("Put things on your head!")
    public void onHat(Player player) {
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            if (player.getInventory().getHelmet() == null) {
                player.sendMessage(new MsgBuilder().def("msg_hat_none").build());
            } else {
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
                player.sendMessage(new MsgBuilder().def("msg_hat_removed").build());
            }
        } else {
            player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
            player.sendMessage(new MsgBuilder().def("msg_hat_set").build());
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
