package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import com.outlook.tehbrian.tfcplugin.util.MsgBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("emote|emotes")
@Description("Various fun chat emotes.")
public class EmoteCommand extends BaseCommand {

    @CommandAlias("winkwonk")
    @CommandPermission("tfcplugin.winkwonk")
    @Description("Wink wonk ;)")
    public void onWinkWonk(Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.winkwonk").replace(player.getDisplayName()).build());
    }

    @CommandAlias("shrug")
    @CommandPermission("tfcplugin.shrug")
    @Description("You don't know. They don't know.")
    public void onShrug(Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.shrug").replace(player.getDisplayName()).build());
    }

    @CommandAlias("spook")
    @CommandPermission("tfcplugin.spook")
    @Description("OoooOOooOoOOoOOoo")
    public void onSpook(Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.spook").replace(player.getDisplayName()).build());
    }

    @CommandAlias("doubt")
    @CommandPermission("tfcplugin.doubt")
    @Description("Press X.")
    public void onDoubt(Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.doubt").replace(player.getDisplayName()).build());
    }

    @CommandAlias("unreadable")
    @CommandPermission("tfcplugin.unreadable")
    @Description("Untransparent. Is that a word? Opaque?.")
    public void onUnreadable(Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.unreadable").replace(player.getDisplayName()).build());
    }

    @CommandAlias("blame")
    @CommandPermission("tfcplugin.blame")
    @Description("It's their fault, not yours.")
    public void onBlame(Player player, String text) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.blame").replace(player.getDisplayName(), text).build());
    }

    @CommandAlias("hug")
    @CommandPermission("tfcplugin.hug")
    @Description("D'aww that's so cute!")
    public void onHug(Player player, String text) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.hug").replace(player.getDisplayName(), text).build());
    }

    @CommandAlias("sue")
    @CommandPermission("tfcplugin.sue")
    @Description("Court fixes everything.. right?")
    public void onSue(Player player, @Optional String text) {
        if (text == null) {
            Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.sue").replace(player.getDisplayName()).build());
        } else {
            Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.sue_extra").replace(player.getDisplayName(), text).build());
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
