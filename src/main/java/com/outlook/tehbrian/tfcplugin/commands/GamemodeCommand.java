package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Utils;
import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("gm|gamemode")
@CommandPermission("tfcplugin.gamemode")
@Description("Change your gamemode.")
public class GamemodeCommand extends BaseCommand {

    private final Main main;

    public GamemodeCommand(Main main) {
        this.main = main;
    }

    @Subcommand("survival|s|0")
    @CommandAlias("gms")
    @Description("Change your gamemode to Survival.")
    public void onSurvival(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage(Utils.format("msg_gamemode_change", "Survival"));
    }

    @Subcommand("creative|c|1")
    @CommandAlias("gmc")
    @Description("Change your gamemode to Creative.")
    public void onCreative(Player player) {
        player.setGameMode(GameMode.CREATIVE);
        player.sendMessage(Utils.format("msg_gamemode_change", "Creative"));
    }

    @Subcommand("adventure|a|2")
    @CommandAlias("gma")
    @Description("Change your gamemode to Adventure.")
    public void onAdventure(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(Utils.format("msg_gamemode_change", "Adventure"));
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Utils.format("msg_gamemode_help"));
    }
}
