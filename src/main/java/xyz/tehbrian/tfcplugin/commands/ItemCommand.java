package xyz.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("item")
@Description("Useful commands for modifying items.")
public class ItemCommand extends BaseCommand {

    @Subcommand("setname")
    @CommandPermission("tfcplugin.item.setname")
    @Description("Set the item's display name.")
    public void onSetName(Player player, String text) {
    }

    @Subcommand("addlore")
    @CommandPermission("tfcplugin.item.addlore")
    @Description("Add lore to the end of the item's lore.")
    public void onAddLore(Player player, String text) {
    }

    @Subcommand("removelore")
    @CommandPermission("tfcplugin.item.removelore")
    @Description("Remove lore from a specific index.")
    public void onRemoveLore(Player player, Short index, String text) {
    }

    @Subcommand("setlore")
    @CommandPermission("tfcplugin.item.setlore")
    @Description("Set the lore at a specific index.")
    public void onSetLore(Player player, Short index, String text) {
    }

    @Subcommand("setamount")
    @CommandPermission("tfcplugin.item.setamount")
    @Description("Set the amount of items in the itemstack.")
    public void onSetAmount(Player player, Integer amount) {
    }

    @Subcommand("setdurability")
    @CommandPermission("tfcplugin.item.setdurability")
    @Description("Set the item's durability.")
    public void onSetDurability(Player player, Short durability) {
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
