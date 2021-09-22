package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import xyz.tehbrian.tfcplugin.FloatyPlugin;
import xyz.tehbrian.tfcplugin.util.MiscUtils;
import xyz.tehbrian.tfcplugin.util.MsgBuilder;

import java.util.Calendar;

@SuppressWarnings("unused")
public class PlayerListener implements Listener {

    private final FloatyPlugin main;

    public PlayerListener(final FloatyPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        player.sendMessage(new MsgBuilder().msgKey("msg.banner").build());

        final Firework firework = player.getWorld().spawn(player.getEyeLocation(), Firework.class);
        final FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(false)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.WHITE, Color.BLUE, Color.GREEN)
                .withFade(Color.GREEN, Color.BLUE, Color.WHITE)
                .build());
        fireworkMeta.setPower(2);
        firework.setFireworkMeta(fireworkMeta);

        if (player.hasPlayedBefore()) {
            event.setJoinMessage(new MsgBuilder().msgKey("msg.join").formats(player.getDisplayName()).build());

            final long millisSinceLastPlayed = Calendar.getInstance().getTimeInMillis() - player.getLastPlayed();
            player.sendMessage(new MsgBuilder().def("msg.motd").formats(MiscUtils.fancifyTime(millisSinceLastPlayed)).build());
        } else {
            event.setJoinMessage(new MsgBuilder().msgKey("msg.join_new").formats(player.getDisplayName()).build());

            player.sendMessage(new MsgBuilder().def("msg.motd_new").formats(player.getName()).build());
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        event.setQuitMessage(new MsgBuilder().msgKey("msg.leave").formats(event.getPlayer().getDisplayName()).build());
    }

}
