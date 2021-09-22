package xyz.tehbrian.tfcplugin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.LuckPermsService;
import xyz.tehbrian.tfcplugin.util.MsgBuilder;

@SuppressWarnings("unused")
@CommandAlias("rules")
@Description("Server and Discord rules.")
public class RulesCommand extends BaseCommand {

    private final LuckPermsService luckPermsService;

    public RulesCommand(
            final @NonNull LuckPermsService luckPermsService
    ) {
        this.luckPermsService = luckPermsService;
    }

    @Default
    public void onRules(final CommandSender sender, @Default("1") @Conditions("limits:min=1,max=9") final Integer page) {
//        for (final String line : ConfigDeserializers.deserializePage("books.rules", page)) {
//            sender.sendMessage(line);
//        }
    }

    @Subcommand("accept")
    @CommandAlias("acceptrules")
    @Description("Accept the rules and get building permissions!")
    public void onAccept(final Player player) {
        if (player.hasPermission("tfcplugin.build")) {
            player.sendMessage(new MsgBuilder().prefixKey("prefixes.rules.prefix").msgKey("msg.rules.already_accepted").build());
        } else {
            this.luckPermsService.addPlayerGroup(player, "passenger");
            player.sendMessage(new MsgBuilder().prefixKey("prefixes.rules.prefix").msgKey("msg.rules.accept").build());
        }
    }

}
