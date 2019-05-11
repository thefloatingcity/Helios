package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.utils.MsgBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;

@SuppressWarnings("unused")
@CommandAlias("action|actions")
@Description("Various world-changing actions.")
public class ActionCommand extends BaseCommand {

    private final Main main;

    public ActionCommand(Main main) {
        this.main = main;
    }

    @CommandAlias("launch")
    @CommandPermission("tfcplugin.launch")
    @Description("Up you go!")
    public void onLaunch(Player player) {
        player.setVelocity(new Vector(0, 10, 0));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
        Bukkit.broadcastMessage(new MsgBuilder().def("msg_launch_themself").replace(player.getDisplayName()).build());
    }

    @CommandAlias("launch")
    @CommandPermission("tfcplugin.launchother")
    @Description("Launch someone sky-high!")
    @CommandCompletion("@players")
    public void onLaunchOther(Player player, OnlinePlayer target) {
        Player targetPlayer = target.getPlayer();
        targetPlayer.setVelocity(new Vector(0, 10, 0));
        targetPlayer.getWorld().playSound(targetPlayer.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
        Bukkit.broadcastMessage(new MsgBuilder().def("msg_launch").replace(player.getDisplayName(), targetPlayer.getDisplayName()).build());
    }

    @CommandAlias("zap")
    @CommandPermission("tfcplugin.zap")
    @Description("Zap.. yourself?")
    public void onZap(Player player) {
        player.getWorld().strikeLightning(player.getLocation());
        Bukkit.broadcastMessage(new MsgBuilder().def("msg_zap_themself").replace(player.getDisplayName()).build());
    }

    @CommandAlias("zap")
    @CommandPermission("tfcplugin.zapother")
    @Description("Zap someone! Like Zeus!")
    @CommandCompletion("@players")
    public void onZapOther(Player player, OnlinePlayer target) {
        Player targetPlayer = target.getPlayer();
        targetPlayer.getWorld().strikeLightning(targetPlayer.getLocation());
        Bukkit.broadcastMessage(new MsgBuilder().def("msg_zap").replace(player.getDisplayName(), targetPlayer.getDisplayName()).build());
    }

    @CommandAlias("boost")
    @CommandPermission("tfcplugin.boost")
    @Description("Zoom zoom!")
    public void onBoost(Player player) {
        player.setVelocity(player.getLocation().getDirection().multiply(3));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
        Bukkit.broadcastMessage(new MsgBuilder().def("msg_boost_themself").replace(player.getDisplayName()).build());
    }

    @CommandAlias("boost")
    @CommandPermission("tfcplugin.boostother")
    @Description("Useful for annoying others.")
    @CommandCompletion("@players")
    public void onBoostOther(Player player, OnlinePlayer target) {
        Player targetPlayer = target.getPlayer();
        targetPlayer.setVelocity(targetPlayer.getLocation().getDirection().multiply(3));
        targetPlayer.getWorld().playSound(targetPlayer.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
        Bukkit.broadcastMessage(new MsgBuilder().def("msg_boost").replace(player.getDisplayName(), targetPlayer.getDisplayName()).build());
    }

    @CommandAlias("poke")
    @CommandPermission("tfcplugin.poke")
    @Description("Poke thineself.")
    public void onPoke(Player player) {

        double maxY = main.getConfig().getDouble("poke_force.maxY");
        double minY = main.getConfig().getDouble("poke_force.minY");
        double maxXZ = main.getConfig().getDouble("poke_force.maxXZ");
        double minXZ = main.getConfig().getDouble("poke_force.minXZ");
        Random random = new Random();
        double randX = minXZ + random.nextDouble() * (maxXZ - minXZ);
        double randY = minY + random.nextDouble() * (maxY - minY);
        double randZ = minXZ + random.nextDouble() * (maxXZ - minXZ);
        Vector randomVector = new Vector(randX, randY, randZ);

        player.setVelocity(randomVector);
        Bukkit.broadcastMessage(new MsgBuilder().def("msg_poke_themself").replace(player.getDisplayName()).build());
    }

    @CommandAlias("poke")
    @CommandPermission("tfcplugin.pokeother")
    @Description("Just a little push.")
    @CommandCompletion("@players")
    public void onPokeOther(Player player, OnlinePlayer target) {
        Player targetPlayer = target.getPlayer();

        double maxY = main.getConfig().getDouble("poke_force.maxY");
        double minY = main.getConfig().getDouble("poke_force.minY");
        double maxXZ = main.getConfig().getDouble("poke_force.maxXZ");
        double minXZ = main.getConfig().getDouble("poke_force.minXZ");
        Random random = new Random();
        double randX = minXZ + random.nextDouble() * (maxXZ - minXZ);
        double randY = minY + random.nextDouble() * (maxY - minY);
        double randZ = minXZ + random.nextDouble() * (maxXZ - minXZ);
        Vector randomVector = new Vector(randX, randY, randZ);

        targetPlayer.setVelocity(randomVector);
        Bukkit.broadcastMessage(new MsgBuilder().def("msg_poke").replace(player.getDisplayName(), targetPlayer.getDisplayName()).build());
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
