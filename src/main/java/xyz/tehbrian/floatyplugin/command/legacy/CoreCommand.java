package xyz.tehbrian.floatyplugin.command.legacy;

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
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.user.UserService;
import xyz.tehbrian.floatyplugin.util.MsgBuilder;

@SuppressWarnings("unused")
@CommandAlias("tfc")
@Description("Core commands for FloatyPlugin.")
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
    @CommandPermission("floatyplugin.core.reload")
    @Description("Reload FloatyPlugin's config.")
    public void onReload(final CommandSender sender) {
        this.floatyPlugin.loadConfigs();
        sender.sendMessage(new MsgBuilder().def("core.reload").build());
    }

    @CommandAlias("fly")
    @CommandPermission("floatyplugin.core.fly")
    @Description("Toggle your flight ability.")
    public void onFly(final Player player) {
        if (this.userService.getUser(player).toggleFlyBypassEnabled()) {
            player.sendMessage(new MsgBuilder().def("core.fly_enabled").build());
        } else {
            player.sendMessage(new MsgBuilder().def("core.fly_disabled").build());
        }
    }

    @HelpCommand
    public void onHelp(final CommandSender sender, final CommandHelp help) {
        help.showHelp();
    }

}
