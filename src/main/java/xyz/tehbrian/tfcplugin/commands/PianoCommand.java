package xyz.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.tehbrian.tfcplugin.managers.PianoManager;
import xyz.tehbrian.tfcplugin.managers.PianoSound;
import xyz.tehbrian.tfcplugin.util.ConfigUtils;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

@SuppressWarnings("unused")
@CommandAlias("piano")
@CommandPermission("tfcplugin.piano")
@Description("A playable piano!")
public class PianoCommand extends BaseCommand {

    @Subcommand("menu")
    @Description("Pick your notes!")
    public void onMenu(Player player) {
        player.openInventory(ConfigUtils.getInventory("inventories.piano_notes"));
    }

    @Subcommand("instrument")
    @Description("Pick your instrument!")
    @CommandCompletion("*")
    public void onInstrument(Player player, PianoSound pianoSound) {
        PianoManager.setPlayerPianoInstrument(player, pianoSound.toSound());
        player.sendMessage(new MsgBuilder().prefixKey("infixes.piano.prefix").msgKey("msg.piano.instrument_change").formats(pianoSound.toString()).build());
    }

    @Subcommand("toggle")
    @Description("Toggle your piano on and off.")
    public void onToggle(Player player) {
        PianoManager.setPlayerEnabledPiano(player, !PianoManager.getPlayerEnabledPiano(player));

        if (PianoManager.getPlayerEnabledPiano(player)) {
            player.sendMessage(new MsgBuilder().prefixKey("infixes.piano.prefix").msgKey("msg.piano.enabled").build());
        } else {
            player.sendMessage(new MsgBuilder().prefixKey("infixes.piano.prefix").msgKey("msg.piano.disabled").build());
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, @Default("1") @Conditions("limits:min=1,max=4") Integer page) {
        for (String line : ConfigUtils.getPage("books.piano_manual", page)) {
            sender.sendMessage(line);
        }
    }
}
