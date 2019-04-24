package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("rules")
@Description("Server and Discord rules.")
public class RulesCommand extends BaseCommand {

    private final Main main;

    public RulesCommand(Main main) {
        this.main = main;
    }

    @Default
    public void onRules(Player player, @Default("1") @Conditions("min=1,max=9") int page) {
        ConfigurationSection rule = main.getConfig().getConfigurationSection("rules.rule" + page);
        player.sendMessage(Utils.formatC("rules_prefix", "msg_rules", rule.getString("topic"), page));
        for (String line : rule.getStringList("content")) {
            player.sendMessage(Utils.colorString("rules_multi"));
        }
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
