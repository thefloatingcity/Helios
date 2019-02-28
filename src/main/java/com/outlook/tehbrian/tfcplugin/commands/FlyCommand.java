package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Flight;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
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
            if (Flight.getPlayerCanFly(player)) {
                Flight.setPlayerCanFly(player, false);
                player.sendMessage(Misc.formatConfig("msg_fly_disabled"));
            } else {
                Flight.setPlayerCanFly(player, true);
                player.sendMessage(Misc.formatConfig("msg_fly_enabled"));
            }
        } else {
            sender.sendMessage(Misc.formatConfig("msg_player_only"));
        }
        return true;
    }
}
