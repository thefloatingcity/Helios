package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.utils.LuckPermsUtils;
import com.outlook.tehbrian.tfcplugin.utils.MiscUtils;
import com.outlook.tehbrian.tfcplugin.utils.MsgBuilder;
import org.bukkit.command.CommandSender;
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
    public void onRules(CommandSender sender, @Default("1") @Conditions("limits:min=1,max=9") Integer page) {
        for (String line : MiscUtils.createPage(page, 9, "rules", "rules_prefix", "rules_multi")) {
            sender.sendMessage(line);
        }
    }

    @Subcommand("accept")
    @CommandAlias("acceptrules")
    @Description("Accept the rules and get building permissions!")
    public void onAccept(Player player) {
        if (player.hasPermission("tfcplugin.rulesaccepted")) {
            player.sendMessage(new MsgBuilder().prefix("rules_prefix").msg("msg_rules_already_accepted").build());
        } else {
            LuckPermsUtils.setPlayerGroup(player, "passenger");
            player.sendMessage(new MsgBuilder().prefix("rules_prefix").msg("msg_rules_accept").build());
        }
    }
}
