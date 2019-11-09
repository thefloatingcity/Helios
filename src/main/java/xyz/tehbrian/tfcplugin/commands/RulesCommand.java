package xyz.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.tehbrian.tfcplugin.util.ConfigUtils;
import xyz.tehbrian.tfcplugin.util.LuckPermsUtils;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

@SuppressWarnings("unused")
@CommandAlias("rules")
@Description("Server and Discord rules.")
public class RulesCommand extends BaseCommand {

    @Default
    public void onRules(CommandSender sender, @Default("1") @Conditions("limits:min=1,max=9") Integer page) {
        for (String line : ConfigUtils.getPage("books.rules", page)) {
            sender.sendMessage(line);
        }
    }

    @Subcommand("accept")
    @CommandAlias("acceptrules")
    @Description("Accept the rules and get building permissions!")
    public void onAccept(Player player) {
        if (player.hasPermission("tfcplugin.build")) {
            player.sendMessage(new MsgBuilder().prefixKey("infixes.rules.prefix").msgKey("msg.rules.already_accepted").build());
        } else {
            LuckPermsUtils.addPlayerGroup(player, "passenger");
            player.sendMessage(new MsgBuilder().prefixKey("infixes.rules.prefix").msgKey("msg.rules.accept").build());
        }
    }
}
