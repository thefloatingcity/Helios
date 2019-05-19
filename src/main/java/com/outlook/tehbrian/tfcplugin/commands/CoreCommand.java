package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.utils.MsgBuilder;
import org.bukkit.command.CommandSender;

@SuppressWarnings("unused")
@CommandAlias("tfc")
@Description("Core commands for TFCPlugin.")
public class CoreCommand extends BaseCommand {

    private final Main main;

    public CoreCommand(Main main) {
        this.main = main;
    }

    @Subcommand("reload")
    @CommandPermission("tfcplugin.reload")
    @Description("Reload TFCPlugin.")
    public void onReload(CommandSender sender) {
        main.reloadConfig();
        sender.sendMessage(new MsgBuilder().def("msg_reloaded").build());
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
