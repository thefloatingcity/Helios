package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import com.outlook.tehbrian.tfcplugin.Piano;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

@CommandAlias("piano")
@CommandPermission("tfcplugin.piano")
public class PianoCommand extends BaseCommand {

    private final Main main;

    public PianoCommand(Main main) {
        this.main = main;
    }

    @Subcommand("menu")
    @Description("Pick your notes here!")
    public void onMenu(Player player) {
        Inventory pianoNotesInventory = Bukkit.createInventory(null, 27, main.getConfig().getString("piano_notes_inventory_name"));
        for (ConfigurationSection rule : (List<ConfigurationSection>) main.getConfig().getList("piano_notes")) {
            String name = rule.getString("name");
            List lore = rule.getStringList("lore");
            int data = rule.getInt("data");
            pianoNotesInventory.addItem(Misc.createItem(name, lore, Material.STAINED_GLASS_PANE, 1, data));
        }
        player.openInventory(pianoNotesInventory);
    }

    @Subcommand("instrument")
    @Description("Pick your instrument! Any sound!")
    @CommandCompletion("@pianosounds")
    public void onInstrument(Player player, Sound sound) {
        Piano.setPlayerPianoInstrument(player, sound);
        player.sendMessage(Misc.formatConfig("msg_piano_instrument_change", sound.toString()));
    }

    @Subcommand("toggle")
    @Description("Toggle your piano on and off.")
    public void onToggle(Player player) {
        if (Piano.getPlayerEnabledPiano(player)) {
            Piano.setPlayerEnabledPiano(player, false);
            player.sendMessage(Misc.formatConfig("msg_piano_disabled"));
        } else {
            Piano.setPlayerEnabledPiano(player, true);
            player.sendMessage(Misc.formatConfig("msg_piano_enabled"));
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Misc.formatConfig("msg_piano_help"));
    }
}
