package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
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
    public void onWinkWonk(Player player) {
        Bukkit.broadcastMessage(Utils.formatC("none", "emote_winkwonk", player.getDisplayName()));
    }

    @CommandAlias("shrug")
    @CommandPermission("tfcplugin.shrug")
    @Description("You don't know. They don't know.")
    public void onShrug(Player player) {
        Bukkit.broadcastMessage(Utils.formatC("none", "emote_shrug", player.getDisplayName()));
    }

    @CommandAlias("spook")
    @CommandPermission("tfcplugin.spook")
    @Description("OoooOOooOoOOoOOoo")
    public void onSpook(Player player) {
        Bukkit.broadcastMessage(Utils.formatC("none", "emote_spook", player.getDisplayName()));
    }

    @CommandAlias("doubt")
    @CommandPermission("tfcplugin.doubt")
    @Description("Press X to doubt.")
    public void onDoubt(Player player) {
        Bukkit.broadcastMessage(Utils.formatC("none", "emote_doubt", player.getDisplayName()));
    }

    @CommandAlias("blame")
    @CommandPermission("tfcplugin.blame")
    @Description("Pfft, it's their fault, not yours.")
    public void onBlame(Player player, String text) {
        Bukkit.broadcastMessage(Utils.formatC("none", "emote_blame", player.getDisplayName(), text));
    }

    @CommandAlias("sue")
    @CommandPermission("tfcplugin.sue")
    @Description("Court fixes everything.. right?")
    public void onSue(Player player, String text) {
        Bukkit.broadcastMessage(Utils.formatC("none", "emote_sue", player.getDisplayName(), text));
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
