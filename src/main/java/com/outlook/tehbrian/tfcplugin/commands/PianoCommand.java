package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import com.outlook.tehbrian.tfcplugin.Piano;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;

@CommandAlias("piano")
@CommandPermission("tfcplugin.piano")
public class PianoCommand extends BaseCommand {

    private final Main main;

    public PianoCommand(Main main) {
        this.main = main;
    }

    @HelpCommand
    public void onHelp(Player player) {
        player.sendMessage(Misc.formatConfig("msg_piano_help"));
    }

    @Subcommand("menu")
    public void onMenu(Player player) {
        Inventory pianoNotesInventory = Bukkit.createInventory(null, 27, main.getConfig().getString("piano_menu_inventory_name"));
        pianoNotesInventory.addItem(Misc.createItem("&rF♯/G♭ [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major, F# Minor&f]", "&8[Note]", "0.5")), Material.STAINED_GLASS_PANE, 1, 8));
        pianoNotesInventory.addItem(Misc.createItem("&rG [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[]", "&8[Note]", "0.529732")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rG♯/A♭ [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major, F# Minor&f]", "&8[Note]", "0.561231")), Material.STAINED_GLASS_PANE, 1, 8));
        pianoNotesInventory.addItem(Misc.createItem("&rA [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Minor&f]", "&8[Note]", "0.594604")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rA♯/B♭ [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major&f]", "&8[Note]", "0.629961")), Material.STAINED_GLASS_PANE, 1, 8));
        pianoNotesInventory.addItem(Misc.createItem("&rB [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major, F# Minor&f]", "&8[Note]", "0.667420")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rC [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[]", "&8[Note]", "0.707107")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rC♯/D♭ [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major, F# Minor&f]", "&8[Note]", "0.749154")), Material.STAINED_GLASS_PANE, 1, 8));
        pianoNotesInventory.addItem(Misc.createItem("&rD [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Minor&f]", "&8[Note]", "0.793701")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rD♯/E♭ [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major&f]", "&8[Note]", "0.840896")), Material.STAINED_GLASS_PANE, 1, 8));
        pianoNotesInventory.addItem(Misc.createItem("&rE [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Minor&f]", "&8[Note]", "0.890899")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rF [&bOctave 1&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major&f]", "&8[Note]", "0.943874")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rF♯/G♭ [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major, F# Minor&f]", "&8[Note]", "1")), Material.STAINED_GLASS_PANE, 1, 8));
        pianoNotesInventory.addItem(Misc.createItem("&rG [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[]", "&8[Note]", "1.059463")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rG♯/A♭ [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major, F# Minor&f]", "&8[Note]", "1.122462")), Material.STAINED_GLASS_PANE, 1, 8));
        pianoNotesInventory.addItem(Misc.createItem("&rA [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Minor&f]", "&8[Note]", "1.189207")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rA#/B♭ [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major&f]", "&8[Note]", "1.259921")), Material.STAINED_GLASS_PANE, 1, 8));
        pianoNotesInventory.addItem(Misc.createItem("&rB [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major, F# Minor&f]", "&8[Note]", "1.334840")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rC [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[]", "&8[Note]", "1.414214")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rC♯/D♭ [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major, F# Minor&f]", "&8[Note]", "1.498307")), Material.STAINED_GLASS_PANE, 1, 8));
        pianoNotesInventory.addItem(Misc.createItem("&rD [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Minor&f]", "&8[Note]", "1.587401")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rD♯/E♭ [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major&f]", "&8[Note]", "1.681793")), Material.STAINED_GLASS_PANE, 1, 8));
        pianoNotesInventory.addItem(Misc.createItem("&rE [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Minor&f]", "&8[Note]", "1.781797")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rF [&bOctave 2&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major&f]", "&8[Note]", "1.887749")), Material.STAINED_GLASS_PANE, 1, 0));
        pianoNotesInventory.addItem(Misc.createItem("&rF♯/G♭ [&bOctave 3&f]", new ArrayList<>(Arrays.asList("&7Part of &f[&eF# Major, F# Minor&f]", "&8[Note]", "2")), Material.STAINED_GLASS_PANE, 1, 8));
        player.openInventory(pianoNotesInventory);
    }

    @Subcommand("instrument")
    public void onInstrument(Player player) {

    }

    @Subcommand("toggle")
    public void onToggle(Player player) {
        if (Piano.getPlayerEnabledPiano(player)) {
            Piano.setPlayerEnabledPiano(player, false);
            player.sendMessage(Misc.formatConfig("msg_piano_disabled"));
        } else {
            Piano.setPlayerEnabledPiano(player, true);
            player.sendMessage(Misc.formatConfig("msg_piano_enabled"));
        }
    }
}
