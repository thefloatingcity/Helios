package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.utils.MsgBuilder;
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
        Bukkit.broadcastMessage(new MsgBuilder().msg("emote_winkwonk").replace(player.getDisplayName()).build());
    }

    @CommandAlias("shrug")
    @CommandPermission("tfcplugin.shrug")
    @Description("You don't know. They don't know.")
    public void onShrug(Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msg("emote_shrug").replace(player.getDisplayName()).build());
    }

    @CommandAlias("spook")
    @CommandPermission("tfcplugin.spook")
    @Description("OoooOOooOoOOoOOoo")
    public void onSpook(Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msg("emote_spook").replace(player.getDisplayName()).build());
    }

    @CommandAlias("doubt")
    @CommandPermission("tfcplugin.doubt")
    @Description("Press X.")
    public void onDoubt(Player player) {
        Bukkit.broadcastMessage(new MsgBuilder().msg("emote_doubt").replace(player.getDisplayName()).build());
    }

    @CommandAlias("blame")
    @CommandPermission("tfcplugin.blame")
    @Description("It's their fault, not yours.")
    public void onBlame(Player player, String text) {
        Bukkit.broadcastMessage(new MsgBuilder().msg("emote_blame").replace(player.getDisplayName(), text).build());
    }

    @CommandAlias("sue")
    @CommandPermission("tfcplugin.sue")
    @Description("Court fixes everything.. right?")
    public void onSue(Player player, @Optional String text) {
        if (text == null) {
            Bukkit.broadcastMessage(new MsgBuilder().msg("emote_sue").replace(player.getDisplayName()).build());
        } else {
            Bukkit.broadcastMessage(new MsgBuilder().msg("emote_sue_extra").replace(player.getDisplayName(), text).build());
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
