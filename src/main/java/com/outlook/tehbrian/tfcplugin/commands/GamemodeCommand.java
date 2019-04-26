package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Utils;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("gm|gamemode")
@Description("Change your gamemode.")
public class GamemodeCommand extends BaseCommand {

    private final Main main;

    public GamemodeCommand(Main main) {
        this.main = main;
    }

    @Subcommand("survival|s|0")
    @CommandAlias("gms")
    @CommandPermission("tfcplugin.gamemode")
    @Description("Change your gamemode to Survival.")
    public void onSurvival(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage(Utils.format("msg_gamemode_changed", "Survival"));
    }

    @Subcommand("survival|s|0")
    @CommandAlias("gms")
    @CommandPermission("tfcplugin.gamemodeother")
    @Description("Change someone's gamemode to Survival.")
    @CommandCompletion("@players")
    @Syntax("<target>")
    public void onSurvivalOther(CommandSender sender, OnlinePlayer targetWrapper) {
        Player target = targetWrapper.getPlayer();
        target.setGameMode(GameMode.SURVIVAL);
        sender.sendMessage(Utils.format("msg_gamemode_changed_other", target.getDisplayName(), "Survival"));
        target.sendMessage(Utils.format("msg_gamemode_changed", "Survival"));
    }

    @Subcommand("creative|c|1")
    @CommandAlias("gmc")
    @CommandPermission("tfcplugin.gamemode")
    @Description("Change your gamemode to Creative.")
    public void onCreative(Player player) {
        player.setGameMode(GameMode.CREATIVE);
        player.sendMessage(Utils.format("msg_gamemode_changed", "Creative"));
    }

    @Subcommand("creative|c|1")
    @CommandAlias("gmc")
    @CommandPermission("tfcplugin.gamemodeother")
    @Description("Change someone's gamemode to Creative.")
    @CommandCompletion("@players")
    @Syntax("<target>")
    public void onCreativeOther(CommandSender sender, OnlinePlayer targetWrapper) {
        Player target = targetWrapper.getPlayer();
        target.setGameMode(GameMode.CREATIVE);
        sender.sendMessage(Utils.format("msg_gamemode_changed_other", target.getDisplayName(), "Creative"));
        target.sendMessage(Utils.format("msg_gamemode_changed", "Creative"));
    }

    @Subcommand("adventure|a|2")
    @CommandAlias("gma")
    @CommandPermission("tfcplugin.gamemode")
    @Description("Change your gamemode to Adventure.")
    public void onAdventure(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(Utils.format("msg_gamemode_changed", "Adventure"));
    }

    @Subcommand("adventure|a|2")
    @CommandAlias("gma")
    @CommandPermission("tfcplugin.gamemode")
    @Description("Change someone's gamemode to Adventure.")
    @CommandCompletion("@players")
    @Syntax("<target>")
    public void onAdventureOther(CommandSender sender, OnlinePlayer targetWrapper) {
        Player target = targetWrapper.getPlayer();
        target.setGameMode(GameMode.ADVENTURE);
        sender.sendMessage(Utils.format("msg_gamemode_changed_other", target.getDisplayName(), "Creative"));
        target.sendMessage(Utils.format("msg_gamemode_changed", "Adventure"));
    }

    @CatchUnknown
    public void onUnknown(CommandSender sender) {
        sender.sendMessage(Utils.format("msg_gamemode_unknown"));
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
