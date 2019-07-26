package com.outlook.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.outlook.tehbrian.tfcplugin.util.MiscUtils;
import com.outlook.tehbrian.tfcplugin.util.MsgBuilder;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("ontime")
@Description("Check your playtime. ")
public class OntimeCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onOntime(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target == null ? player : target.getPlayer();

        long millisPlayed = (targetPlayer.getStatistic(Statistic.PLAY_ONE_TICK) / 20) * 1000;

        if (target == null) {
            player.sendMessage(new MsgBuilder().def("msg.ontime").replace(MiscUtils.fancifyTime(millisPlayed)).build());
        } else {
            player.sendMessage(new MsgBuilder().def("msg.ontime_other").replace(targetPlayer.getDisplayName(), MiscUtils.fancifyTime(millisPlayed)).build());
        }
    }

    @CommandAlias("rankup")
    @Description("Get fancy new perks the more you play!")
    public void onRankup(Player player) {

    }
}
