package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Utils;
import com.outlook.tehbrian.tfcplugin.Main;
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
        sender.sendMessage(Utils.format("msg_reloaded"));
    }

    @HelpCommand
    public void onHelp(CommandSender sender) {
        sender.sendMessage(Utils.format("msg_core_help"));
    }
}
