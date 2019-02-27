package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LaunchCommand implements CommandExecutor {
    private final Main plugin;

    public LaunchCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                player.setVelocity(new Vector(0, 10, 0));
                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 100000, 0.75F);

                Bukkit.broadcastMessage(Misc.formatConfig("msg_launch_themself", sender.getName()));
            } else {
                sender.sendMessage(Misc.formatConfig("msg_player_only"));
            }
        } else if (args.length >= 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (args[0].equals("everyone")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setVelocity(new Vector(0, 10, 0));
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 100000, 0.75F);
                }
                Bukkit.broadcastMessage(Misc.formatConfig("msg_launch_everyone", sender.getName()));
            } else if (target != null) {
                target.setVelocity(new Vector(0, 10, 0));
                target.playSound(target.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 100000, 0.75F);

                Bukkit.broadcastMessage(Misc.formatConfig("msg_launch", sender.getName(), target.getName()));
            } else {
                sender.sendMessage(Misc.formatConfig("msg_not_online"));
            }
        }
        return true;
    }
}
