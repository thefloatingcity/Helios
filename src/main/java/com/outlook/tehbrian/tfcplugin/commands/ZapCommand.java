package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ZapCommand implements CommandExecutor {
    private final Main plugin;

    public ZapCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                player.getWorld().strikeLightning(player.getLocation());

                Bukkit.broadcastMessage(plugin.formatChat("msg_zap_themself", sender.getName()));
            } else {
                sender.sendMessage(plugin.formatChat("msg_player_only"));
            }
        } else if (args.length >= 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (args[0].equals("everyone")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.getWorld().strikeLightning(player.getLocation());
                }
                Bukkit.broadcastMessage(plugin.formatChat("msg_zap_everyone", sender.getName()));
            } else if (target != null) {
                target.getWorld().strikeLightning(target.getLocation());

                Bukkit.broadcastMessage(plugin.formatChat("msg_zap", sender.getName(), target.getName()));
            } else {
                sender.sendMessage(plugin.formatChat("msg_not_online"));
            }
        }

        return true;
    }
}

