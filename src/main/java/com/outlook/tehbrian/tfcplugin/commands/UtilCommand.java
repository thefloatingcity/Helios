package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import com.outlook.tehbrian.tfcplugin.Flight;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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
}
