package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

@CommandAlias("rules")
@CommandPermission("tfcplugin.rules")
public class RulesCommand extends BaseCommand {

    private final Main main;

    public RulesCommand(Main main) {
        this.main = main;
    }

    @Default
    @Description("Please read them!")
    public void onRules(Player player) {
        player.sendMessage(Misc.formatConfig("msg_rules"));
        Inventory rulesInventory = Bukkit.createInventory(null, 9, main.getConfig().getString("rules_inventory_name"));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule1.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule1.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule2.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule2.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule3.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule3.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule4.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule4.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule5.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule5.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule6.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule6.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule7.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule7.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule8.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule8.text")), Material.WRITTEN_BOOK, 1, 0));
        rulesInventory.addItem(Misc.createItem(main.getConfig().getString("rules.rule9.name"), new ArrayList<>(main.getConfig().getStringList("rules.rule9.text")), Material.WRITTEN_BOOK, 1, 0));
        player.openInventory(rulesInventory);
    }

    @Subcommand("accept")
    @Description("Once you're done!")
    public void onAccept(Player player) {
        player.sendMessage(Misc.formatConfig("msg_rules_accept"));
        // LuckPerms Promote
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Misc.formatConfig("msg_rules_help"));
    }
}
