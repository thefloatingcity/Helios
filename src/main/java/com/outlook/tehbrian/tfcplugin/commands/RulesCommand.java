package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("unused")
@CommandAlias("rules")
@Description("Server and Discord rules.")
public class RulesCommand extends BaseCommand {

    private final Main main;

    public RulesCommand(Main main) {
        this.main = main;
    }

    @Default
    public void onRules(Player player) {
        player.sendMessage(Utils.formatC("rules_prefix", "msg_rules"));
        Inventory rulesInventory = Bukkit.createInventory(null, 9, main.getConfig().getString("rules_inventory_name"));
        for (String key : main.getConfig().getConfigurationSection("rules").getKeys(false)) {
            ConfigurationSection rule = main.getConfig().getConfigurationSection("rules." + key);
            rulesInventory.addItem(Utils.createItem(rule.getString("name"), rule.getStringList("lore"), Material.WRITTEN_BOOK, 1, 0));
        }
        player.openInventory(rulesInventory);
    }

    @Subcommand("accept")
    @CommandAlias("acceptrules")
    @Description("Accept the rules and get building perms!")
    public void onAccept(Player player) {
        if (player.hasPermission("tfcplugin.rulesaccepted")) {
            player.sendMessage(Utils.formatC("rules_prefix", "msg_rules_already_accepted"));
        } else {
            player.sendMessage(Utils.formatC("rules_prefix", "msg_rules_accept"));
            main.getVaultPerms().playerAddGroup(null, player, "passenger");
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Utils.formatC("rules_prefix", "msg_rules_help"));
    }
}
