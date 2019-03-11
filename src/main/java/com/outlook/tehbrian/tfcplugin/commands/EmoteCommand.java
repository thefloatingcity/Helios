package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("emote|emotes")
public class EmoteCommand extends BaseCommand {

    private final Main main;

    public EmoteCommand(Main main) {
        this.main = main;
    }

    @CommandAlias("shrug")
    @CommandPermission("tfcplugin.shrug")
    @Description("You don't know. They don't know.")
    public void onShrug(CommandSender sender) {
        Bukkit.broadcastMessage(Misc.formatConfig("msg_shrug", sender.getName()));
    }

    @CommandAlias("doubt")
    @CommandPermission("tfcplugin.doubt")
    @Description("Press X to doubt.")
    public void onDoubt(CommandSender sender) {
        Bukkit.broadcastMessage(Misc.formatConfig("msg_doubt", sender.getName()));
    }

    @CommandAlias("blame")
    @CommandPermission("tfcplugin.blame")
    @Description("They did something wrong. Blame them!")
    public void onBlame(CommandSender sender, String thing) {
        Bukkit.broadcastMessage(Misc.formatConfig("msg_blame", sender.getName(), thing));
    }

    @CommandAlias("sue")
    @CommandPermission("tfcplugin.sue")
    @Description("Sue them! It fixes everything.")
    public void onSue(CommandSender sender, String thing) {
        Player player = (Player) sender;
        Bukkit.broadcastMessage(Misc.formatConfig("msg_sue", main.getVaultChat().getPlayerSuffix(player), sender.getName(), thing));
    }

    @CommandAlias("me")
    @CommandPermission("tfcplugin.me")
    @Description ("A replacement for regular /me.")
    public void onMe(Player player, String text) {
        if (player.hasPermission("tfcplugin.chatcolor")) {
            text = Misc.colorString(text);
        }
        Bukkit.broadcastMessage(Misc.formatConfig("msg_me", main.getVaultChat().getPlayerSuffix(player), player.getDisplayName(), text));
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Misc.formatConfig("msg_emote_help"));
    }
}
