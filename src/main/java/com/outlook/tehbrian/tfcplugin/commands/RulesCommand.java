package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

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
        for (ConfigurationSection rule : (List<ConfigurationSection>) main.getConfig().getList("rules")) {
            String name = rule.getString("name");
            List lore = rule.getStringList("lore");
            rulesInventory.addItem(Misc.createItem(name, lore, Material.WRITTEN_BOOK, 1, 0));
        }
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
