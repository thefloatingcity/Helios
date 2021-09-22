package xyz.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import xyz.tehbrian.tfcplugin.util.MiscUtils;
import xyz.tehbrian.tfcplugin.util.MsgBuilder;

@SuppressWarnings("unused")
@CommandAlias("ontime")
@Description("Check your playtime.")
public class OntimeCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onOntime(final Player player, @Optional final OnlinePlayer target) {
        final Player targetPlayer = target == null ? player : target.getPlayer();

        // Liars. Why was it changed from PLAY_ONE_TICK to PLAY_ONE_MINUTE? It's incremented every tick, not minute.
        final long millisPlayed = (targetPlayer.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) * 1000;

        if (target == null) {
            player.sendMessage(new MsgBuilder().def("msg.ontime.check_self").formats(MiscUtils.fancifyTime(millisPlayed)).build());
        } else {
            player.sendMessage(new MsgBuilder()
                    .def("msg.ontime.check_other")
                    .formats(targetPlayer.getDisplayName(), MiscUtils.fancifyTime(millisPlayed))
                    .build());
        }
    }

    @CommandAlias("rankup")
    @Description("Get fancy new perks the more you play!")
    public void onRankup(final Player player) {
        player.sendMessage("Currently not working.. Bug Brian about this till it's done ;p");
    }

}
