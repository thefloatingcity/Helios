package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import com.outlook.tehbrian.tfcplugin.TFCPlugin;
import com.outlook.tehbrian.tfcplugin.piano.PianoManager;
import com.outlook.tehbrian.tfcplugin.piano.PianoSound;
import com.outlook.tehbrian.tfcplugin.util.ConfigParsers;
import com.outlook.tehbrian.tfcplugin.util.MsgBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("piano")
@CommandPermission("tfcplugin.piano")
@Description("A playable piano!")
public class PianoCommand extends BaseCommand {

    private final TFCPlugin main;

    public PianoCommand(TFCPlugin main) {
        this.main = main;
    }

    @Subcommand("menu")
    @Description("Pick your notes!")
    public void onMenu(Player player) {
        player.openInventory(ConfigParsers.getInventory("inventories.piano_notes"));
    }

    @Subcommand("instrument")
    @Description("Pick your piano instrument!")
    @CommandCompletion("*")
    public void onInstrument(Player player, PianoSound pianoSound) {
        PianoManager.setPlayerPianoInstrument(player, pianoSound.toSound());
        player.sendMessage(new MsgBuilder().prefixKey("infixes.piano.prefix").msgKey("msg.piano_instrument_change").replace(pianoSound.toString()).build());
    }

    @Subcommand("toggle")
    @Description("Toggle your piano on and off.")
    public void onToggle(Player player) {
        if (PianoManager.getPlayerEnabledPiano(player)) {
            PianoManager.setPlayerEnabledPiano(player, false);
            player.sendMessage(new MsgBuilder().prefixKey("infixes.piano.prefix").msgKey("msg.piano_disabled").build());
        } else {
            PianoManager.setPlayerEnabledPiano(player, true);
            player.sendMessage(new MsgBuilder().prefixKey("infixes.piano.prefix").msgKey("msg.piano_enabled").build());
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, @Default("1") @Conditions("limits:min=1,max=4") Integer page) {
        for (String line : ConfigParsers.getPage("books.piano_manual", page)) {
            sender.sendMessage(line);
        }
    }
}
