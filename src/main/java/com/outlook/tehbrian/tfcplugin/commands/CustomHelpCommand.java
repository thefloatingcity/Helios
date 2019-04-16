package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Utils;
import org.bukkit.command.CommandSender;

@CommandAlias("help")
@Description("Custom /help for the server.")
public class CustomHelpCommand extends BaseCommand {

    private final Main main;

    public CustomHelpCommand(Main main) {
        this.main = main;
    }

    @Default
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Utils.format("msg_help"));
    }

    @HelpCommand
    public void onHelpUnknown(CommandSender sender) {
        sender.sendMessage(Utils.format("msg_help_unknown"));
    }
}
