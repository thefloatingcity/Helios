package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("item")
@Description("Useful commands for modifying items.")
public class ItemCommand extends BaseCommand {

    @Subcommand("setname")
    @CommandPermission("tfcplugin.item.setname")
    @Description("Set the item's display name.")
    public void onSetName(Player player, String string) {
        // WIP.
    }

    @Subcommand("addlore")
    @CommandPermission("tfcplugin.item.addlore")
    @Description("Add lore to the end of the list.")
    public void onAddLore(Player player, String string) {
    }

    @Subcommand("removelore")
    @CommandPermission("tfcplugin.item.removelore")
    @Description("Remove lore from the specific index.")
    public void onRemoveLore(Player player, Short index, String string) {
    }

    @Subcommand("setlore")
    @CommandPermission("tfcplugin.item.setlore")
    @Description("Set the lore at the specific index.")
    public void onSetLore(Player player, Short index, String string) {
    }

    @Subcommand("setamount")
    @CommandPermission("tfcplugin.item.setamount")
    @Description("Set the item's amount.")
    public void onSetAmount(Player player, Integer amount) {
    }

    @Subcommand("setdurability")
    @CommandPermission("tfcplugin.item.setdurability")
    @Description("Set the item's durability.")
    public void onSetDurability(Player player, Short durability) {
    }
}
