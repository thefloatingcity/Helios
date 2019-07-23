package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.outlook.tehbrian.tfcplugin.util.MsgBuilder;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("gm|gamemode")
@Description("Change your gamemode.")
public class GamemodeCommand extends BaseCommand {

    @Subcommand("survival|s|0")
    @CommandAlias("gms")
    @CommandPermission("tfcplugin.gamemode")
    @Description("Change gamemode to Survival.")
    @CommandCompletion("@players")
    public void onSurvival(Player player, @Optional @CommandPermission("tfcplugin.gamemodeother") OnlinePlayer target) {
        if (target == null) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(new MsgBuilder().def("msg.gamemode_changed").replace("Survival").build());
        } else {
            Player targetPlayer = target.getPlayer();
            targetPlayer.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(new MsgBuilder().def("msg.gamemode_changed_other").replace(targetPlayer.getDisplayName(), "Survival").build());
            targetPlayer.sendMessage(new MsgBuilder().def("msg.gamemode_changed").replace("Survival").build());
        }
    }

    @Subcommand("creative|c|1")
    @CommandAlias("gmc")
    @CommandPermission("tfcplugin.gamemode")
    @Description("Change gamemode to Creative.")
    @CommandCompletion("@players")
    public void onCreative(Player player, @Optional @CommandPermission("tfcplugin.gamemodeother") OnlinePlayer target) {
        if (target == null) {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage(new MsgBuilder().def("msg.gamemode_changed").replace("Creative").build());
        } else {
            Player targetPlayer = target.getPlayer();
            targetPlayer.setGameMode(GameMode.CREATIVE);
            player.sendMessage(new MsgBuilder().def("msg.gamemode_changed_other").replace(targetPlayer.getDisplayName(), "Creative").build());
            targetPlayer.sendMessage(new MsgBuilder().def("msg.gamemode_changed").replace("Creative").build());
        }
    }

    @Subcommand("adventure|a|2")
    @CommandAlias("gma")
    @CommandPermission("tfcplugin.gamemode")
    @Description("Change gamemode to Adventure.")
    @CommandCompletion("@players")
    public void onAdventure(Player player, @Optional @CommandPermission("tfcplugin.gamemodeother") OnlinePlayer target) {
        if (target == null) {
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(new MsgBuilder().def("msg.gamemode_changed").replace("Adventure").build());
        } else {
            Player targetPlayer = target.getPlayer();
            targetPlayer.setGameMode(GameMode.ADVENTURE);
            player.sendMessage(new MsgBuilder().def("msg.gamemode_changed_other").replace(targetPlayer.getDisplayName(), "Creative").build());
            targetPlayer.sendMessage(new MsgBuilder().def("msg.gamemode_changed").replace("Adventure").build());
        }
    }

    @CatchUnknown
    public void onUnknown(CommandSender sender) {
        sender.sendMessage(new MsgBuilder().def("msg.gamemode_unknown").build());
    }
}
