package xyz.tehbrian.tfcplugin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.tehbrian.tfcplugin.util.MsgBuilder;

@SuppressWarnings("unused")
@CommandAlias("gm|gamemode")
@CommandPermission("tfcplugin.gamemode")
@Description("Change a player's gamemode.")
public class GamemodeCommand extends BaseCommand {

    @Subcommand("survival|s|0")
    @CommandAlias("gms")
    @Description("Change a player's gamemode to Survival.")
    @CommandCompletion("@players")
    public void onSurvival(final Player player, @Optional @CommandPermission("tfcplugin.gamemodeother") final OnlinePlayer target) {
        final Player targetPlayer = target == null ? player : target.getPlayer();

        targetPlayer.setGameMode(GameMode.SURVIVAL);

        if (target == null) {
            player.sendMessage(new MsgBuilder().def("msg.gamemode.change_self").formats("Survival").build());
        } else {
            player.sendMessage(new MsgBuilder()
                    .def("msg.gamemode.change_other")
                    .formats(targetPlayer.getDisplayName(), "Survival")
                    .build());
            targetPlayer.sendMessage(new MsgBuilder().def("msg.gamemode.change_self").formats("Survival").build());
        }
    }

    @Subcommand("creative|c|1")
    @CommandAlias("gmc")
    @Description("Change a player's gamemode to Creative.")
    @CommandCompletion("@players")
    public void onCreative(final Player player, @Optional @CommandPermission("tfcplugin.gamemodeother") final OnlinePlayer target) {
        final Player targetPlayer = target == null ? player : target.getPlayer();

        targetPlayer.setGameMode(GameMode.CREATIVE);

        if (target == null) {
            player.sendMessage(new MsgBuilder().def("msg.gamemode.change_self").formats("Creative").build());
        } else {
            player.sendMessage(new MsgBuilder()
                    .def("msg.gamemode.change_other")
                    .formats(targetPlayer.getDisplayName(), "Creative")
                    .build());
            targetPlayer.sendMessage(new MsgBuilder().def("msg.gamemode.change_self").formats("Creative").build());
        }
    }

    @Subcommand("adventure|a|2")
    @CommandAlias("gma")
    @Description("Change a player's gamemode to Adventure.")
    @CommandCompletion("@players")
    public void onAdventure(final Player player, @Optional @CommandPermission("tfcplugin.gamemodeother") final OnlinePlayer target) {
        final Player targetPlayer = target == null ? player : target.getPlayer();

        targetPlayer.setGameMode(GameMode.ADVENTURE);

        if (target == null) {
            player.sendMessage(new MsgBuilder().def("msg.gamemode.change_self").formats("Adventure").build());
        } else {
            player.sendMessage(new MsgBuilder()
                    .def("msg.gamemode.change_other")
                    .formats(targetPlayer.getDisplayName(), "Creative")
                    .build());
            targetPlayer.sendMessage(new MsgBuilder().def("msg.gamemode.change_self").formats("Adventure").build());
        }
    }

    @CatchUnknown
    public void onUnknown(final CommandSender sender) {
        sender.sendMessage(new MsgBuilder().def("msg.gamemode.unknown").build());
    }

}
