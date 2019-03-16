package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import org.bukkit.command.CommandSender;

@CommandAlias("tfcplugin")
@Description("Core commands for TFCPlugin.")
public class CoreCommand extends BaseCommand {

    public final Main main;

    public CoreCommand(Main main) {
        this.main = main;
    }

    @Subcommand("reload")
    @CommandPermission("tfcplugin.reload")
    @Description("Reload TFCPlugin.")
    public void onReload(CommandSender sender) {
        main.reloadConfig();
        sender.sendMessage(Misc.formatConfig("msg_reloaded"));
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Misc.formatConfig("msg_core_help"));
    }
}
