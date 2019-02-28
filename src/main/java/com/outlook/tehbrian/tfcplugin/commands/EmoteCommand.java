package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandAlias("emote|emotes")
public class EmoteCommand extends BaseCommand {

    private final Main main;

    public EmoteCommand(Main main) {
        this.main = main;
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Misc.formatConfig("msg_emote_help"));
    }

    @CommandAlias("shrug")
    @CommandPermission("tfcplugin.shrug")
    @Description("You don't know. They don't know.")
    public void onShrug(CommandSender sender) {
        Bukkit.broadcastMessage(Misc.formatConfig("msg_shrug", sender.getName()));
    }

    @CommandAlias("blame")
    @CommandPermission("tfcplugin.blame")
    @Description("They did something wrong. Blame them!")
    public void onBlame(CommandSender sender, String args) {
        Bukkit.broadcastMessage(Misc.formatConfig("msg_blame", sender.getName(), args));
    }

    @CommandAlias("sue")
    @CommandPermission("tfcplugin.sue")
    @Description("Sue them! It fixes everything.")
    public void onSue(CommandSender sender, String args) {
        Bukkit.broadcastMessage(Misc.formatConfig("msg_sue", sender.getName(), args));
    }
}
