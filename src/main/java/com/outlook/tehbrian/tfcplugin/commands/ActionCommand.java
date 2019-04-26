package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Utils;
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
        Bukkit.broadcastMessage(Utils.format("msg_launch_themself", player.getDisplayName()));
    }

    @CommandAlias("launch")
    @CommandPermission("tfcplugin.launchother")
    @Description("Launch someone sky-high!")
    @CommandCompletion("@players")
    @Syntax("<target>")
    public void onLaunchOther(Player player, OnlinePlayer targetWrapper) {
        Player target = targetWrapper.getPlayer();
        target.setVelocity(new Vector(0, 10, 0));
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
        Bukkit.broadcastMessage(Utils.format("msg_launch", player.getDisplayName(), target.getDisplayName()));
    }

    @CommandAlias("zap")
    @CommandPermission("tfcplugin.zap")
    @Description("Zap.. yourself?")
    public void onZap(Player player) {
        player.getWorld().strikeLightning(player.getLocation());
        Bukkit.broadcastMessage(Utils.format("msg_zap_themself", player.getDisplayName()));
    }


    @CommandAlias("zap")
    @CommandPermission("tfcplugin.zapother")
    @Description("Zap someone! Like Zeus!")
    @CommandCompletion("@players")
    @Syntax("<target>")
    public void onZapOther(Player player, OnlinePlayer targetWrapper) {
        Player target = targetWrapper.getPlayer();
        target.getWorld().strikeLightning(target.getLocation());
        Bukkit.broadcastMessage(Utils.format("msg_zap", player.getDisplayName(), target.getDisplayName()));
    }

    @CommandAlias("boost")
    @CommandPermission("tfcplugin.boost")
    @Description("Zoom zoom!")
    public void onBoost(Player player) {
        player.setVelocity(player.getLocation().getDirection().multiply(3));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
        Bukkit.broadcastMessage(Utils.format("msg_boost_themself", player.getDisplayName()));
    }

    @CommandAlias("boost")
    @CommandPermission("tfcplugin.boostother")
    @Description("Useful for annoying others.")
    @CommandCompletion("@players")
    @Syntax("<target>")
    public void onBoostOther(Player player, OnlinePlayer targetWrapper) {
        Player target = targetWrapper.getPlayer();
        target.setVelocity(target.getLocation().getDirection().multiply(3));
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
        Bukkit.broadcastMessage(Utils.format("msg_boost", player.getDisplayName(), target.getDisplayName()));
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
        Bukkit.broadcastMessage(Utils.format("msg_poke_themself", player.getDisplayName()));
    }

    @CommandAlias("poke")
    @CommandPermission("tfcplugin.pokeother")
    @Description("Just a little push.")
    @CommandCompletion("@players")
    @Syntax("<target>")
    public void onPokeOther(Player player, OnlinePlayer targetWrapper) {
        Player target = targetWrapper.getPlayer();

        double maxY = main.getConfig().getDouble("poke_force.maxY");
        double minY = main.getConfig().getDouble("poke_force.minY");
        double maxXZ = main.getConfig().getDouble("poke_force.maxXZ");
        double minXZ = main.getConfig().getDouble("poke_force.minXZ");
        Random random = new Random();
        double randX = minXZ + random.nextDouble() * (maxXZ - minXZ);
        double randY = minY + random.nextDouble() * (maxY - minY);
        double randZ = minXZ + random.nextDouble() * (maxXZ - minXZ);
        Vector randomVector = new Vector(randX, randY, randZ);

        target.setVelocity(randomVector);
        Bukkit.broadcastMessage(Utils.format("msg_poke", player.getDisplayName(), target.getDisplayName()));
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
