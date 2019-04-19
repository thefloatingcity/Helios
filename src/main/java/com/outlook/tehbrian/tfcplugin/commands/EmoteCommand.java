package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandAlias("emote|emotes")
@Description("Various fun chat emotes.")
public class EmoteCommand extends BaseCommand {

    private final Main main;

    public EmoteCommand(Main main) {
        this.main = main;
    }

    @CommandAlias("winkwonk")
    @CommandPermission("tfcplugin.winkwonk")
    @Description("Wink wonk ;)")
    public void onWinkWonk(CommandSender sender) {
        Bukkit.broadcastMessage(Utils.emote(sender, "emote_winkwonk"));
    }

    @CommandAlias("shrug")
    @CommandPermission("tfcplugin.shrug")
    @Description("You don't know. They don't know.")
    public void onShrug(CommandSender sender) {
        Bukkit.broadcastMessage(Utils.emote(sender, "emote_shrug"));
    }

    @CommandAlias("doubt")
    @CommandPermission("tfcplugin.doubt")
    @Description("Press X to doubt.")
    public void onDoubt(CommandSender sender) {
        Bukkit.broadcastMessage(Utils.emote(sender, "emote_doubt"));
    }

    @CommandAlias("blame")
    @CommandPermission("tfcplugin.blame")
    @Description("Pfft, it's their fault, not yours.")
    public void onBlame(CommandSender sender, String text) {
        Bukkit.broadcastMessage(Utils.emote(sender, "emote_blame", text));
    }

    @CommandAlias("sue")
    @CommandPermission("tfcplugin.sue")
    @Description("Court fixes everything.. right?")
    public void onSue(CommandSender sender, String text) {
        Bukkit.broadcastMessage(Utils.emote(sender, "emote_sue", text));
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Utils.format("msg_emote_help"));
    }
}
