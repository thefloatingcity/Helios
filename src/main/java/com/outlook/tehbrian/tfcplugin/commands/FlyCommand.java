package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    private final Main plugin;

    public FlyCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.getPlayerCanFly(player)) {
                plugin.removePlayerCanFly(player);
                player.setAllowFlight(false);
                sender.sendMessage(plugin.formatChat("msg_fly_disabled"));
            } else {
                plugin.addPlayerCanFly(player);
                player.setAllowFlight(true);
                sender.sendMessage(plugin.formatChat("msg_fly_enabled"));
            }
        } else {
            sender.sendMessage(plugin.formatChat("msg_player_only"));
        }
        return true;
    }
}
