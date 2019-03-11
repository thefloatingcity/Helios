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
        for (String key : main.getConfig().getConfigurationSection("rules").getKeys(false)) {
            ConfigurationSection rule = main.getConfig().getConfigurationSection("rules." + key);
            rulesInventory.addItem(Misc.createItem(rule.getString("name"), rule.getStringList("lore"), Material.WRITTEN_BOOK, 1, 0));
        }
        player.openInventory(rulesInventory);
    }

    @Subcommand("accept")
    @CommandAlias("acceptrules")
    @Description("Thanks for reading them!")
    public void onAccept(Player player) {
        if (!player.hasPermission("tfcplugin.rulesaccepted")) {
            player.sendMessage(Misc.formatConfig("msg_rules_already_accepted"));
        } else {
            player.sendMessage(Misc.formatConfig("msg_rules_accept"));
            main.getVaultPerms().playerAddGroup(null, player, "passenger");
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Misc.formatConfig("msg_rules_help"));
    }
}
