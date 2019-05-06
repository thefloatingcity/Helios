package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.utils.TextUtils;
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
        sender.sendMessage(TextUtils.format("msg_reloaded"));
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
