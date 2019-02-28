package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandAlias("emote")
public class EmoteCommand extends BaseCommand {

    private final Main plugin;

    public EmoteCommand(Main plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("shrug")
    @CommandPermission("tfcplugin.shrug")
    public void onShrug(CommandSender sender) {
        Bukkit.broadcastMessage(Misc.formatConfig("msg_shrug", sender.getName()));
    }

    @CommandAlias("blame")
    @CommandPermission("tfcplugin.blame")
    public void onBlame(CommandSender sender, String args) {
        Bukkit.broadcastMessage(Misc.formatConfig("msg_blame", sender.getName(), args));
    }

    @CommandAlias("sue")
    @CommandPermission("tfcplugin.sue")
    public void onSue(CommandSender sender, String args) {
        Bukkit.broadcastMessage(Misc.formatConfig("msg_sue", sender.getName(), args));
    }
}
