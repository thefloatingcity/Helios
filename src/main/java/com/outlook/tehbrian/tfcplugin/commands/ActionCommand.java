package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.outlook.tehbrian.tfcplugin.TFCPlugin;
import com.outlook.tehbrian.tfcplugin.util.MsgBuilder;
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

    private final TFCPlugin main;

    public ActionCommand(TFCPlugin main) {
        this.main = main;
    }

    @CommandAlias("launch")
    @CommandPermission("tfcplugin.action.launch")
    @Description("Like a rocket!")
    @CommandCompletion("@player")
    public void onLaunch(Player player, @Optional @CommandPermission("tfcplugin.action.launchother") OnlinePlayer target) {
        Player targetPlayer = target == null ? player : target.getPlayer();

        targetPlayer.setVelocity(new Vector(0, 10, 0));
        targetPlayer.getWorld().playSound(targetPlayer.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);

        if (target == null) {
            Bukkit.broadcastMessage(new MsgBuilder().def("msg.launch").replace(player.getDisplayName()).build());
        } else {
            Bukkit.broadcastMessage(new MsgBuilder().def("msg.launch_other").replace(player.getDisplayName(), targetPlayer.getDisplayName()).build());
        }
    }

    @CommandAlias("boost")
    @CommandPermission("tfcplugin.action.boost")
    @Description("Useful for annoying others.")
    @CommandCompletion("@players")
    public void onBoost(Player player, @Optional @CommandPermission("tfcplugin.action.boostother") OnlinePlayer target) {
        Player targetPlayer = target == null ? player : target.getPlayer();

        targetPlayer.setVelocity(targetPlayer.getLocation().getDirection().multiply(3));
        targetPlayer.getWorld().playSound(targetPlayer.getEyeLocation(), Sound.ENTITY_FIREWORK_LAUNCH, SoundCategory.MASTER, 5, 0.75F);

        if (target == null) {
            Bukkit.broadcastMessage(new MsgBuilder().def("msg.boost").replace(player.getDisplayName()).build());
        } else {
            Bukkit.broadcastMessage(new MsgBuilder().def("msg.boost_other").replace(player.getDisplayName(), targetPlayer.getDisplayName()).build());
        }
    }

    @CommandAlias("zap")
    @CommandPermission("tfcplugin.action.zap")
    @Description("Kentucky Fried Player")
    @CommandCompletion("@players")
    public void onZap(Player player, @Optional @CommandPermission("tfcplugin.action.zapother") OnlinePlayer target) {
        Player targetPlayer = target == null ? player : target.getPlayer();

        targetPlayer.getWorld().strikeLightning(targetPlayer.getLocation());

        if (target == null) {
            Bukkit.broadcastMessage(new MsgBuilder().def("msg.zap").replace(player.getDisplayName()).build());
        } else {

            Bukkit.broadcastMessage(new MsgBuilder().def("msg.zap_other").replace(player.getDisplayName(), targetPlayer.getDisplayName()).build());
        }
    }

    @CommandAlias("poke")
    @CommandPermission("tfcplugin.action.poke")
    @Description("Just a little push.")
    @CommandCompletion("@players")
    public void onPoke(Player player, @Optional @CommandPermission("tfcplugin.action.pokeother") OnlinePlayer target) {
        Player targetPlayer = target == null ? player : target.getPlayer();

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

        if (target == null) {
            Bukkit.broadcastMessage(new MsgBuilder().def("msg.poke").replace(player.getDisplayName()).build());
        } else {
            Bukkit.broadcastMessage(new MsgBuilder().def("msg.poke_other").replace(player.getDisplayName(), targetPlayer.getDisplayName()).build());
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
