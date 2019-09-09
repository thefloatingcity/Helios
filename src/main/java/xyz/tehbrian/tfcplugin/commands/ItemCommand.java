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
import org.bukkit.inventory.PlayerInventory;
import xyz.tehbrian.tfcplugin.util.item.ItemModifier;

@SuppressWarnings("unused")
@CommandAlias("item")
@Description("Useful commands for modifying items.")
public class ItemCommand extends BaseCommand {

    @Subcommand("setname")
    @CommandPermission("tfcplugin.item.setname")
    @Description("Set the item's display name.")
    public void onSetName(Player player, String text) {
        PlayerInventory inventory = player.getInventory();
        inventory.setItemInMainHand(new ItemModifier(inventory.getItemInMainHand()).setName(text).getItem());
    }

    @Subcommand("setamount")
    @CommandPermission("tfcplugin.item.setamount")
    @Description("Set the item's amount.")
    public void onSetAmount(Player player, Integer amount) {
        PlayerInventory inventory = player.getInventory();
        inventory.setItemInMainHand(new ItemModifier(inventory.getItemInMainHand()).setAmount(amount).getItem());
    }

    @Subcommand("addlore")
    @CommandPermission("tfcplugin.item.addlore")
    @Description("Add on to the item's lore in a new line.")
    public void onAddLore(Player player, String text) {
        PlayerInventory inventory = player.getInventory();
        inventory.setItemInMainHand(new ItemModifier(inventory.getItemInMainHand()).addLore(text).getItem());
    }

    @Subcommand("changelore")
    @CommandPermission("tfcplugin.item.changelore")
    @Description("Change a specific line on an item's lore.")
    public void onEditLore(Player player, String text) {
        PlayerInventory inventory = player.getInventory();
        inventory.setItemInMainHand(new ItemModifier(inventory.getItemInMainHand()).addLore(text).getItem());
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
