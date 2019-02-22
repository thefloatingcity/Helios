package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {
    private final Main plugin;

    public GamemodeCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName() == "gms") {
                player.sendMessage(plugin.formatChat("msg_gamemode_change", "survival"));
                player.setGameMode(GameMode.SURVIVAL);
            } else if (command.getName() == "gmc") {
                player.sendMessage(plugin.formatChat("msg_gamemode_change", "creative"));
                player.setGameMode(GameMode.CREATIVE);
            } else if (command.getName() == "gma") {
                player.sendMessage(plugin.formatChat("msg_gamemode_change", "adventure"));
                player.setGameMode(GameMode.ADVENTURE);
            }
        } else {
            sender.sendMessage(plugin.formatChat("player_only"));
        }
        return true;
    }
}
