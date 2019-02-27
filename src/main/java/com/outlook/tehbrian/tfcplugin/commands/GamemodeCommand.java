package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
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
            if (label.equalsIgnoreCase("gm")) {
                player.sendMessage(Misc.formatConfig("msg_gamemode"));
            } else if (label.equalsIgnoreCase("gms")) {
                player.sendMessage(Misc.formatConfig("msg_gamemode_change", "Survival"));
                player.setGameMode(GameMode.SURVIVAL);
            } else if (label.equalsIgnoreCase("gmc")) {
                player.sendMessage(Misc.formatConfig("msg_gamemode_change", "Creative"));
                Misc.disableFlight(player);
                player.setGameMode(GameMode.CREATIVE);
            } else if (label.equalsIgnoreCase("gma")) {
                player.sendMessage(Misc.formatConfig("msg_gamemode_change", "Adventure"));
                player.setGameMode(GameMode.ADVENTURE);
            }
        } else {
            sender.sendMessage(Misc.formatConfig("player_only"));
        }
        return true;
    }
}
