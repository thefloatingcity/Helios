package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import com.outlook.tehbrian.tfcplugin.Flight;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import com.outlook.tehbrian.tfcplugin.Piano;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

@CommandAlias("util")
public class UtilCommand extends BaseCommand {

    private final Main main;

    public UtilCommand(Main main) {
        this.main = main;
    }

    @CommandAlias("fly")
    @CommandPermission("tfcplugin.fly")
    public void onFly(Player player) {
        if (Flight.getCanBypassFly(player)) {
            Flight.setCanBypassFly(player, false);
            player.sendMessage(Misc.formatConfig("msg_fly_disabled"));
        } else {
            Flight.setCanBypassFly(player, true);
            player.sendMessage(Misc.formatConfig("msg_fly_enabled"));
        }
    }

    @CommandAlias("fly")
    @CommandPermission("tfcplugin.rules")
    public void onRules(Player player) {
        player.sendMessage(Misc.formatConfig("msg_rules"));
        Inventory rulesInventory = Bukkit.createInventory(null, 9, main.getConfig().getString("rules_inventory_name"));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule3.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule3.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule4.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule4.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule5.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule5.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule2.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule2.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule6.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule6.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule7.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule7.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule8.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule8.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule9.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule9.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule1.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule1.text")), Material.WRITTEN_BOOK, 1, 0));
        player.openInventory(rulesInventory);
    }

    @CommandAlias("hat")
    @CommandPermission("tfcplugin.hat")
    public void onHat(Player player) {
        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            if (player.getInventory().getHelmet() == null) {
                player.sendMessage(Misc.formatConfig("msg_hat_none"));
            } else {
                player.sendMessage(Misc.formatConfig("msg_hat_removed"));
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
            }
        } else {
            player.sendMessage(Misc.formatConfig("msg_hat_set"));
            player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
        }
    }




    @CommandAlias("gamemode|g/m")
    @CommandPermission("tfcplugin.gamemode")
    public void onGamemode(Player player) {
        if (label.equalsIgnoreCase("gm")) {
            player.sendMessage(Misc.formatConfig("msg_gamemode"));
        } else if (label.equalsIgnoreCase("gms")) {
            player.sendMessage(Misc.formatConfig("msg_gamemode_change", "Survival"));
            player.setGameMode(GameMode.SURVIVAL);
        } else if (label.equalsIgnoreCase("gmc")) {
            player.sendMessage(Misc.formatConfig("msg_gamemode_change", "Creative"));
            player.setGameMode(GameMode.CREATIVE);
        } else if (label.equalsIgnoreCase("gma")) {
            player.sendMessage(Misc.formatConfig("msg_gamemode_change", "Adventure"));
            player.setGameMode(GameMode.ADVENTURE);
        }
    }

    @Override
    public boolean onPiano(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Misc.formatConfig("msg_piano_help"));
        } else if (args.length >= 1) {
            if (args[0].equals("menu")) {
                Inventory pianoNotesInventory = Bukkit.createInventory(null, 27, plugin.getConfig().getString("piano_menu_inventory_name"));
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
    }
}
