package com.outlook.tehbrian.tfcplugin.commands;

import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;

public class RulesCommand implements CommandExecutor {
    private final Main plugin;

    public RulesCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            sender.sendMessage(plugin.formatChat("msg_rules"));
            Inventory rulesInventory = Bukkit.createInventory(null, 9, "Rules | The Floating City");
            rulesInventory.addItem(plugin.createItem("Rule 1 | Griefing", new ArrayList<>(Arrays.asList("No touching other people's stuff without their permission.", "This includes placing or breaking blocks,", "taking or placing items in their chests, taking or placing items in their itemframes,", "trampling their crops, killing their animals.. You get the idea.", "Please use common sense. If it isn't yours, leave it be.")), Material.BOOK));
            rulesInventory.addItem(plugin.createItem("Rule 2 | Swearing", new ArrayList<>(Arrays.asList("Swearing is allowed but please try to be courteous.", "Remember, as much as you may think otherwise, Minecraft is", "a kids game and there are kids who play it.", "So please just don't fill up the chat with a bunch of swearing.")), Material.BOOK));
            rulesInventory.addItem(plugin.createItem("Rule 3 | Spamming", new ArrayList<>(Arrays.asList("Having long messages and lots of them is fine, but please", "don't fill up chat with meaningless gibberish,", "or repeat the same messages over and over. Again, please", "be courteous and mindful of other people.")), Material.BOOK));
            rulesInventory.addItem(plugin.createItem("Rule 4 | Advertising", new ArrayList<>(Arrays.asList("We're a pretty unconvential server! Talking about servers,", "saying their names, even putting their IPs is fine.", "Just no advertising any servers trying to get random people on it,", "There are websites for that, you know. If you're asking a friend if they wanna go on", "this server and they forgot the IP, it's fine.")), Material.BOOK));
            rulesInventory.addItem(plugin.createItem("Rule 5 | Hurtful Speech", new ArrayList<>(Arrays.asList("No hate speech or discrimination or insults against anyone.", "We're not a politically correct server but don't be a jerk.", "If something you say might hurt someone, don't say it.")), Material.BOOK));
            rulesInventory.addItem(plugin.createItem("Rule 6 | Flamewars", new ArrayList<>(Arrays.asList("Having discussions about things are fine! Religion, politics, so be it.", "However, keep it cool and a debate, not a flamewar.", "Also, be mindful of Rule 5. If you feel like talking about something", "that might hurt someone, don't talk about it with them.", "But if you both want to talk about it and engage in discussion, it's fine!")), Material.BOOK));
            rulesInventory.addItem(plugin.createItem("Rule 7 | PG-13", new ArrayList<>(Arrays.asList("We're a pretty relaxed server. But keep it PG-13.", "No builds or chat that has a lot of gore or sexual content.", "If you wouldn't see it in Avengers, don't make it please.")), Material.BOOK));
            rulesInventory.addItem(plugin.createItem("Rule 8 | Lag, Bugs, and Glitches", new ArrayList<>(Arrays.asList("Please don't try to break a server. It's not funny.", "You're not a hacker, it just ruins the game we're all trying to", "have fun and play on. This means no lag machines or", "giant super big repeating complex redstone, and if you find", "any bugs or glitches, please report them to staff! Thanks!")), Material.BOOK));
            rulesInventory.addItem(plugin.createItem("Rule 9 | Golden Rule", new ArrayList<>(Arrays.asList("If you forget even everything, remember this golden rule.", "Don't be a jerk, and use common sense. This one golden rule", "will happily keep you playing on the server.", "As long as you're not a jerk towards anyone, and you use common sense,", "like not griefing builds, not spamming chat, etc, you'll be golden!")), Material.BOOK));
            player.openInventory(rulesInventory);
        } else {
            sender.sendMessage(plugin.formatChat("msg_player_only"));
        }

        return true;
    }
}
