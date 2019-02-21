package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HatCommand implements CommandExecutor {
    private final Main plugin;

    public HatCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                if (player.getInventory().getHelmet() == null) {
                    player.sendMessage(plugin.formatChat("msg_no_hat"));
                } else {
                    player.sendMessage(plugin.formatChat("msg_removed_hat"));
                    player.getInventory().setHelmet(new ItemStack(Material.AIR));
                }
            } else {
                player.sendMessage(plugin.formatChat("msg_set_hat"));
                player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
            }
        } else {
            sender.sendMessage(plugin.formatChat("msg_player_only"));
        }
        return true;
    }
}
