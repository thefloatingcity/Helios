package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.outlook.tehbrian.tfcplugin.util.ConfigParsers;
import com.outlook.tehbrian.tfcplugin.util.LuckPermsUtils;
import com.outlook.tehbrian.tfcplugin.util.MsgBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("rules")
@Description("Server and Discord rules.")
public class RulesCommand extends BaseCommand {

    @Default
    public void onRules(CommandSender sender, @Default("1") @Conditions("limits:min=1,max=9") Integer page) {
        for (String line : ConfigParsers.getPage("books.rules", page)) {
            sender.sendMessage(line);
        }
    }

    @Subcommand("accept")
    @CommandAlias("acceptrules")
    @Description("Accept the rules and get building permissions!")
    public void onAccept(Player player) {
        if (player.hasPermission("tfcplugin.rulesaccepted")) {
            player.sendMessage(new MsgBuilder().prefixKey("infixes.rules.prefix").msgKey("msg.rules.already_accepted").build());
        } else {
            LuckPermsUtils.addPlayerGroup(player, "passenger");
            player.sendMessage(new MsgBuilder().prefixKey("infixes.rules.prefix").msgKey("msg.rules.accept").build());
        }
    }
}
