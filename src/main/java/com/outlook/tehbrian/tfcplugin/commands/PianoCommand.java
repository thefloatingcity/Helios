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
                    pianoNotesInventory.setItem(0, Misc.createItem("F#/Gb", new ArrayList<>(Arrays.asList("[Piano]", "0.5")), Material.STAINED_GLASS_PANE, 1));
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
                }
            }
        } else {
            sender.sendMessage(Misc.formatConfig("msg_player_only"));
        }
        return true;
    }
}
