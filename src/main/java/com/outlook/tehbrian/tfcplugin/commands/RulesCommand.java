package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class RulesCommand implements CommandExecutor {
    private final Main plugin;

    public RulesCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sender.sendMessage(Misc.formatConfig("msg_rules"));
            Inventory rulesInventory = Bukkit.createInventory(null, 9, plugin.getConfig().getString("rules_inventory_name"));
            rulesInventory.addItem(Misc.createItem(plugin.getConfig().getString("rules.rule1.name"), new ArrayList<>(plugin.getConfig().getStringList("rules.rule1.text")), Material.WRITTEN_BOOK, 1, 0));
            rulesInventory.addItem(Misc.createItem(plugin.getConfig().getString("rules.rule3.name"), new ArrayList<>(plugin.getConfig().getStringList("rules.rule3.text")), Material.WRITTEN_BOOK, 1, 0));
            rulesInventory.addItem(Misc.createItem(plugin.getConfig().getString("rules.rule4.name"), new ArrayList<>(plugin.getConfig().getStringList("rules.rule4.text")), Material.WRITTEN_BOOK, 1, 0));
            rulesInventory.addItem(Misc.createItem(plugin.getConfig().getString("rules.rule5.name"), new ArrayList<>(plugin.getConfig().getStringList("rules.rule5.text")), Material.WRITTEN_BOOK, 1, 0));
            rulesInventory.addItem(Misc.createItem(plugin.getConfig().getString("rules.rule2.name"), new ArrayList<>(plugin.getConfig().getStringList("rules.rule2.text")), Material.WRITTEN_BOOK, 1, 0));
            rulesInventory.addItem(Misc.createItem(plugin.getConfig().getString("rules.rule6.name"), new ArrayList<>(plugin.getConfig().getStringList("rules.rule6.text")), Material.WRITTEN_BOOK, 1, 0));
            rulesInventory.addItem(Misc.createItem(plugin.getConfig().getString("rules.rule7.name"), new ArrayList<>(plugin.getConfig().getStringList("rules.rule7.text")), Material.WRITTEN_BOOK, 1, 0));
            rulesInventory.addItem(Misc.createItem(plugin.getConfig().getString("rules.rule8.name"), new ArrayList<>(plugin.getConfig().getStringList("rules.rule8.text")), Material.WRITTEN_BOOK, 1, 0));
            rulesInventory.addItem(Misc.createItem(plugin.getConfig().getString("rules.rule9.name"), new ArrayList<>(plugin.getConfig().getStringList("rules.rule9.text")), Material.WRITTEN_BOOK, 1, 0));
            player.openInventory(rulesInventory);
        } else {
            sender.sendMessage(Misc.formatConfig("msg_player_only"));
        }
        return true;
    }
}
