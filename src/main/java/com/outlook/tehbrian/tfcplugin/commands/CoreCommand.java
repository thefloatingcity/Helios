package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import org.bukkit.command.CommandSender;

@CommandAlias("tfcplugin")
public class CoreCommand extends BaseCommand {

    public final Main main;

    public CoreCommand(Main main) {
        this.main = main;
    }

    @Subcommand("reload")
    @CommandPermission("tfcplugin.reload")
    @Description("Reload TFCPlugin.")
    public void onReload(CommandSender sender) {
        sender.sendMessage(Misc.formatConfig("msg_reloaded"));
        main.reloadConfig();
    }

    public void onHelp(CommandSender sender) {
        sender.sendMessage(Misc.formatConfig("msg_core_help"));
    }
}
