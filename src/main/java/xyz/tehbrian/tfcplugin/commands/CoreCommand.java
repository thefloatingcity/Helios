package xyz.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.FloatyPlugin;
import xyz.tehbrian.tfcplugin.user.UserService;
import xyz.tehbrian.tfcplugin.util.MsgBuilder;

@SuppressWarnings("unused")
@CommandAlias("tfc")
@Description("Core commands for TFCPlugin.")
public class CoreCommand extends BaseCommand {

    private final FloatyPlugin floatyPlugin;
    private final UserService userService;

    @Inject
    public CoreCommand(
            final @NonNull UserService userService,
            final @NonNull FloatyPlugin floatyPlugin
    ) {
        this.floatyPlugin = floatyPlugin;
        this.userService = userService;
    }

    @Subcommand("reload")
    @CommandPermission("tfcplugin.core.reload")
    @Description("Reload TFCPlugin's config.")
    public void onReload(final CommandSender sender) {
        this.floatyPlugin.loadConfigs();
        sender.sendMessage(new MsgBuilder().def("msg.core.reload").build());
    }

    @CommandAlias("fly")
    @CommandPermission("tfcplugin.core.fly")
    @Description("Toggle your flight ability.")
    public void onFly(final Player player) {
        if (this.userService.getUser(player).toggleFlyBypassEnabled()) {
            player.sendMessage(new MsgBuilder().def("msg.core.fly_enabled").build());
        } else {
            player.sendMessage(new MsgBuilder().def("msg.core.fly_disabled").build());
        }
    }

    @HelpCommand
    public void onHelp(final CommandSender sender, final CommandHelp help) {
        help.showHelp();
    }

}
