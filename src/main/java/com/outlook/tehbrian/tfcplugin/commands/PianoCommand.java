package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import com.outlook.tehbrian.tfcplugin.Piano;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;

public class PianoCommand implements CommandExecutor {
    private final Main plugin;

    public PianoCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(Misc.formatConfig("msg_piano_help"));
            } else if (args.length >= 1) {
                if (args[0].equals("menu")) {
                    Inventory pianoNotesInventory = Bukkit.createInventory(null, 27, plugin.getConfig().getString("piano_menu_inventory_name"));
                    pianoNotesInventory.addItem(Misc.createItem("&rF♯/G♭ [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.5")), Material.STAINED_GLASS_PANE, 1, 15));
                    pianoNotesInventory.addItem(Misc.createItem("&rG [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.529732")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rG♯/A♭ [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.561231")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rA [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.594604")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rA♯/B♭ [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.629961")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rB [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.667420")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rC [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.707107")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rC♯/D♭ [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.749154")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rD [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.793701")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rD♯/E♭ [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.840896")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rE [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.890899")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rF [&bOctave &e1&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "0.943874")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rF♯/G♭ [&bOctave &e1&f/&e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rG [&bOctave &e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1.059463")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rG♯/A♭ [&bOctave &e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1.122462")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rA [&bOctave &e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1.189207")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rA#/B♭ [&bOctave &e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1.259921")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rB [&bOctave &e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1.334840")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rC [&bOctave &e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1.414214")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rC♯/D♭ [&bOctave &e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1.498307")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rD [&bOctave &e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1.587401")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rD♯/E♭ [&bOctave &e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1.681793")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rE [&bOctave &e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1.781797")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rF [&bOctave &e2&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "1.887749")), Material.STAINED_GLASS_PANE, 1, 0));
                    pianoNotesInventory.addItem(Misc.createItem("&rF♯/G♭ [&bOctave &e2&f/&e3&f]", new ArrayList<>(Arrays.asList("&7[Piano]", "2")), Material.STAINED_GLASS_PANE, 1, 0));
                    player.openInventory(pianoNotesInventory);
                } else if (args[0].equals("instrument")) {

                } else if (args[0].equals("toggle")) {
                    if (Piano.getPlayerEnabledPiano(player)) {
                        Piano.setPlayerEnabledPiano(player, false);
                        player.sendMessage(Misc.formatConfig("msg_piano_disabled"));
                    } else {
                        Piano.setPlayerEnabledPiano(player, true);
                        player.sendMessage(Misc.formatConfig("msg_piano_enabled"));
                    }
                } else {
                    player.sendMessage(Misc.formatConfig("msg_piano_help"));
                }
            }
        } else {
            sender.sendMessage(Misc.formatConfig("msg_player_only"));
        }
        return true;
    }
}
