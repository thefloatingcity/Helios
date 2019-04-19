package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

@CommandAlias("action|actions")
@Description("Various world-changing actions.")
public class ActionCommand extends BaseCommand {

    private final Main main;

    public ActionCommand(Main main) {
        this.main = main;
    }

    @CommandAlias("launch")
    @CommandPermission("tfcplugin.launch")
    @Description("Launch someone sky-high!")
    @CommandCompletion("@players")
    public void onLaunch(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                player.setVelocity(new Vector(0, 10, 0));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
                Bukkit.broadcastMessage(Utils.format("msg_launch_themself", sender.getName()));
            } else {
                sender.sendMessage(Utils.format("msg_player_only"));
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                target.setVelocity(new Vector(0, 10, 0));
                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
                Bukkit.broadcastMessage(Utils.format("msg_launch", sender.getName(), target.getName()));
            } else {
                sender.sendMessage(Utils.format("msg_not_online"));
            }
        }
    }

    @CommandAlias("zap")
    @CommandPermission("tfcplugin.zap")
    @Description("Zap someone! Like Zeus!")
    @CommandCompletion("@players")
    public void onZap(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                player.getWorld().strikeLightning(player.getLocation());
                Bukkit.broadcastMessage(Utils.format("msg_zap_themself", sender.getName()));
            } else {
                sender.sendMessage(Utils.format("msg_player_only"));
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                target.getWorld().strikeLightning(target.getLocation());
                Bukkit.broadcastMessage(Utils.format("msg_zap", sender.getName(), target.getName()));
            } else {
                sender.sendMessage(Utils.format("msg_not_online"));
            }
        }
    }

    @CommandAlias("boost")
    @CommandPermission("tfcplugin.boost")
    @Description("Zoom zoom!")
    @CommandCompletion("@players")
    public void onBoost(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                player.setVelocity(player.getLocation().getDirection().multiply(3));
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
                Bukkit.broadcastMessage(Utils.format("msg_boost_themself", sender.getName()));
            } else {
                sender.sendMessage(Utils.format("msg_player_only"));
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                target.setVelocity(target.getLocation().getDirection().multiply(3));
                target.getWorld().playSound(target.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
                Bukkit.broadcastMessage(Utils.format("msg_boost", sender.getName(), target.getName()));
            } else {
                sender.sendMessage(Utils.format("msg_not_online"));
            }
        }
    }

    @CommandAlias("poke")
    @CommandPermission("tfcplugin.poke")
    @Description("Just a little push.")
    @CommandCompletion("@players")
    public void onPoke(CommandSender sender, String[] args) {

        double maxY = main.getConfig().getDouble("poke_force.maxY");
        double minY = main.getConfig().getDouble("poke_force.minY");
        double maxXZ = main.getConfig().getDouble("poke_force.maxXZ");
        double minXZ = main.getConfig().getDouble("poke_force.minXZ");
        Random random = new Random();
        double randX = minXZ + random.nextDouble() * (maxXZ - minXZ);
        double randY = minY + random.nextDouble() * (maxY - minY);
        double randZ = minXZ + random.nextDouble() * (maxXZ - minXZ);
        Vector randomVector = new Vector(randX, randY, randZ);

        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                player.setVelocity(randomVector);
                Bukkit.broadcastMessage(Utils.format("msg_poke_themself", sender.getName()));
            } else {
                sender.sendMessage(Utils.format("msg_player_only"));
            }
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                target.setVelocity(randomVector);
                Bukkit.broadcastMessage(Utils.format("msg_poke", sender.getName(), target.getName()));
            } else {
                sender.sendMessage(Utils.format("msg_not_online"));
            }
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Utils.format("msg_action_help"));
    }
}
