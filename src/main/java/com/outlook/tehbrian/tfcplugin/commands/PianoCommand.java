package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.piano.Piano;
import com.outlook.tehbrian.tfcplugin.piano.PianoSound;
import com.outlook.tehbrian.tfcplugin.utils.ItemBuilder;
import com.outlook.tehbrian.tfcplugin.utils.MiscUtils;
import com.outlook.tehbrian.tfcplugin.utils.MsgBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("unused")
@CommandAlias("piano")
@CommandPermission("tfcplugin.piano")
@Description("A playable piano!")
public class PianoCommand extends BaseCommand {

    private final Main main;

    public PianoCommand(Main main) {
        this.main = main;
    }

    @Subcommand("menu")
    @Description("Pick your notes!")
    public void onMenu(Player player) {
        Inventory pianoNotesInventory = Bukkit.createInventory(null, 27, main.getConfig().getString("piano_notes_inventory_name"));
        for (String key : main.getConfig().getConfigurationSection("piano_notes").getKeys(false)) {
            ConfigurationSection pianoNote = main.getConfig().getConfigurationSection("piano_notes." + key);
            pianoNotesInventory.addItem(new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .name(pianoNote.getString("name"))
                    .amount(1)
                    .durability((short) pianoNote.getInt("data"))
                    .lore(pianoNote.getStringList("lore"))
                    .build());
        }
        player.openInventory(pianoNotesInventory);
    }

    @Subcommand("instrument")
    @Description("Pick your piano instrument!")
    @CommandCompletion("*")
    public void onInstrument(Player player, PianoSound pianoSound) {
        Piano.setPlayerPianoInstrument(player, pianoSound.toSound());
        player.sendMessage(new MsgBuilder().prefix("piano_prefix").msg("msg_piano_instrument_change").replace(pianoSound.toString()).build());
    }

    @Subcommand("toggle")
    @Description("Toggle your piano on and off.")
    public void onToggle(Player player) {
        if (Piano.getPlayerEnabledPiano(player)) {
            Piano.setPlayerEnabledPiano(player, false);
            player.sendMessage(new MsgBuilder().prefix("piano_prefix").msg("msg_piano_disabled").build());
        } else {
            Piano.setPlayerEnabledPiano(player, true);
            player.sendMessage(new MsgBuilder().prefix("piano_prefix").msg("msg_piano_enabled").build());
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, @Default("1") @Conditions("limits:min=1,max=4") Integer page) {
        for (String line : MiscUtils.createPage(page, 4, "piano_help", "piano_prefix", "piano_multi")) {
            sender.sendMessage(line);
        }
    }
}
