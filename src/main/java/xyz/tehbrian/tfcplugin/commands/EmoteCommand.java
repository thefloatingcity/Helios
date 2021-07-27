package xyz.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

@SuppressWarnings("unused")
@CommandAlias("emote|emotes")
@Description("Various fun chat emotes.")
public class EmoteCommand extends BaseCommand {

    @CommandAlias("unreadable")
    @CommandPermission("tfcplugin.emote.unreadable")
    @Description("Untransparent. Is that a word? Opaque?")
    public void onUnreadable(final Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.unreadable").formats(player.getDisplayName()).build());
    }

    @CommandAlias("cry")
    @CommandPermission("tfcplugin.emote.cry")
    @Description("So sad.")
    public void onCry(final Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.cry").formats(player.getDisplayName()).build());
    }

    @CommandAlias("shrug")
    @CommandPermission("tfcplugin.emote.shrug")
    @Description("You don't know. They don't know.")
    public void onShrug(final Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.shrug").formats(player.getDisplayName()).build());
    }

    @CommandAlias("hug")
    @CommandPermission("tfcplugin.emote.hug")
    @Description("D'aww that's so cute!")
    public void onHug(final Player player, final String text) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.hug").formats(player.getDisplayName(), text).build());
    }

    @CommandAlias("blame")
    @CommandPermission("tfcplugin.emote.blame")
    @Description("It's their fault, not yours.")
    public void onBlame(final Player player, final String text) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.blame").formats(player.getDisplayName(), text).build());
    }

    @CommandAlias("winkwonk")
    @CommandPermission("tfcplugin.emote.winkwonk")
    @Description("Wink wonk ;)")
    public void onWinkWonk(final Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.winkwonk").formats(player.getDisplayName()).build());
    }

    @CommandAlias("sue")
    @CommandPermission("tfcplugin.emote.sue")
    @Description("Court fixes everything.. right?")
    public void onSue(final Player player, @Optional final String text) {
        if (text == null) {
            Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.sue").formats(player.getDisplayName()).build());
        } else {
            Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.sue_extra").formats(player.getDisplayName(), text).build());
        }
    }

    @CommandAlias("highfive")
    @CommandPermission("tfcplugin.emote.highfive")
    @Description("Up high! Down low! Too slow!")
    public void onHighfive(final Player player, final String text) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.highfive").formats(player.getDisplayName(), text).build());
    }

    @CommandAlias("spook")
    @CommandPermission("tfcplugin.emote.spook")
    @Description("OoooOOooOoOOoOOoo")
    public void onSpook(final Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.spook").formats(player.getDisplayName()).build());
    }

    @CommandAlias("doubt")
    @CommandPermission("tfcplugin.emote.doubt")
    @Description("Press X.")
    public void onDoubt(final Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.doubt").formats(player.getDisplayName()).build());
    }

    @CommandAlias("kith")
    @CommandPermission("tfcplugin.emote.kith")
    @Description("It's kiss, with a lisp.")
    public void onKith(final Player player, final String text) {
        Bukkit.broadcastMessage(new MsgBuilder().msgKey("msg.emote.kith").formats(player.getDisplayName(), text).build());
    }

    @HelpCommand
    public void onHelp(final CommandSender sender, final CommandHelp help) {
        help.showHelp();
    }
}
