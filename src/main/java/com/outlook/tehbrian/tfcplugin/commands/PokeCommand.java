package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

public class PokeCommand implements CommandExecutor {
    private final Main plugin;

    public PokeCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        double maxY = plugin.getConfig().getDouble("poke_force.maxY");
        double minY = plugin.getConfig().getDouble("poke_force.minY");
        double maxXZ = plugin.getConfig().getDouble("poke_force.maxXZ");
        double minXZ = plugin.getConfig().getDouble("poke_force.minXZ");
        Random random = new Random();
        double randX = minXZ + random.nextDouble() * (maxXZ - minXZ);
        double randY = minY + random.nextDouble() * (maxY - minY);
        double randZ = minXZ + random.nextDouble() * (maxXZ - minXZ);
        Vector randomVector = new Vector(randX, randY, randZ);

        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                player.setVelocity(randomVector);

                Bukkit.broadcastMessage(plugin.formatChat("msg_poke_themself", sender.getName()));
            } else {
                sender.sendMessage(plugin.formatChat("msg_player_only"));
            }
        } else if (args.length >= 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (args[0].equals("everyone")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setVelocity(randomVector);
                }
                Bukkit.broadcastMessage(plugin.formatChat("msg_poke_everyone", sender.getName()));
            } else if (target != null) {
                target.setVelocity(randomVector);

                Bukkit.broadcastMessage(plugin.formatChat("msg_poke", sender.getName(), target.getName()));
            } else {
                sender.sendMessage(plugin.formatChat("msg_not_online"));
            }
        }

        return true;
    }
}

