package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import org.bukkit.command.CommandSender;

@CommandAlias("help")
@CommandPermission("tfcplugin.help")
public class CustomHelpCommand extends BaseCommand {

    private final Main main;

    public CustomHelpCommand(Main main) {
        this.main = main;
    }

    @Default
    @Description("Some commands and server info!")
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Misc.formatConfig("msg_help"));
    }

    @CommandAlias("vote")
    @Description("It helps out the server!")
    public void onHelpVote(CommandSender sender) {
        sender.sendMessage(Misc.formatConfig("msg_help_vote"));
    }

    @HelpCommand
    public void onHelpUnknown(CommandSender sender) {
        sender.sendMessage(Misc.formatConfig("msg_help_unknown"));
    }
}
