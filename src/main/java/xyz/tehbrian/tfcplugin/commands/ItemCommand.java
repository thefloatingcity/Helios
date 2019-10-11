package xyz.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import xyz.tehbrian.tfcplugin.util.item.ItemModifier;

@SuppressWarnings("unused")
@CommandAlias("item")
@Description("Useful commands for modifying items.")
public class ItemCommand extends BaseCommand {

    @Subcommand("setmaterial")
    @CommandPermission("tfcplugin.item.setmaterial")
    @Description("Set the item's material.")
    @CommandCompletion("*")
    public void onSetMaterial(Player player, Material material) {
        PlayerInventory inventory = player.getInventory();
        inventory.setItemInMainHand(new ItemModifier(inventory.getItemInMainHand()).setMaterial(material).getItem());
    }

    @Subcommand("setamount")
    @CommandPermission("tfcplugin.item.setamount")
    @Description("Set the item's amount.")
    public void onSetAmount(Player player, @Conditions("limits:min=1,max=127") Integer amount) {
        PlayerInventory inventory = player.getInventory();
        inventory.setItemInMainHand(new ItemModifier(inventory.getItemInMainHand()).setAmount(amount).getItem());
    }

    @Subcommand("enchant")
    public class Enchant extends BaseCommand {

        @Subcommand("add")
        @CommandPermission("tfcplugin.item.enchant.add")
        @Description("blag")
        public void onEnchantAdd(Player player, Enchantment enchantment, Short level) {
            PlayerInventory inventory = player.getInventory();
            inventory.setItemInMainHand(new ItemModifier(inventory.getItemInMainHand()).addEnchantment(enchantment, level).getItem());
        }

        // TODO: You know, this.
    }

    @Subcommand("setname")
    @CommandPermission("tfcplugin.item.setname")
    @Description("Set the item's display name.")
    public void onSetName(Player player, String text) {
        PlayerInventory inventory = player.getInventory();
        inventory.setItemInMainHand(new ItemModifier(inventory.getItemInMainHand()).setName(text).getItem());
    }

    @Subcommand("lore")
    public class Lore extends BaseCommand {

        @Subcommand("add")
        @CommandPermission("tfcplugin.item.lore.add")
        @Description("Add on to the item's lore in a new line.")
        public void onAddLore(Player player, String text) {
            PlayerInventory inventory = player.getInventory();
            inventory.setItemInMainHand(new ItemModifier(inventory.getItemInMainHand()).addLore(text).getItem());
        }

        @Subcommand("change")
        @CommandPermission("tfcplugin.item.lore.change")
        @Description("Change a specific line on an item's lore.")
        public void onChangeLore(Player player, int index, String text) {
            PlayerInventory inventory = player.getInventory();
            inventory.setItemInMainHand(new ItemModifier(inventory.getItemInMainHand()).changeLore(index, text).getItem());
        }

        @Subcommand("remove")
        @CommandPermission("tfcplugin.item.lore.remove")
        @Description("Remove a line of lore.")
        public void onRemoveLore(Player player, int index) {
            PlayerInventory inventory = player.getInventory();
            inventory.setItemInMainHand(new ItemModifier(inventory.getItemInMainHand()).removeLore(index).getItem());
        }
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
