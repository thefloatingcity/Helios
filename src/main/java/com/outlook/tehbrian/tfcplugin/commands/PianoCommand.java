package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PianoCommand implements CommandExecutor {
    private final Main plugin;

    public PianoCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.getPlayerPlaysPiano(player)) {
                plugin.setPlayerPlaysPiano(player, false);
                player.sendMessage(plugin.formatChat("msg_piano_disabled"));
            } else {
                plugin.setPlayerPlaysPiano(player, true);
                player.sendMessage(plugin.formatChat("msg_piano_enabled"));
            }
        } else {
            sender.sendMessage(plugin.formatChat("msg_player_only"));
        }

        return true;
    }
}
