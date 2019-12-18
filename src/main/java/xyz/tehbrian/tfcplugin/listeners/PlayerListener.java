package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import xyz.tehbrian.tfcplugin.TFCPlugin;
import xyz.tehbrian.tfcplugin.util.LuckPermsUtils;
import xyz.tehbrian.tfcplugin.util.MiscUtils;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

import java.util.Calendar;
import java.util.Objects;

@SuppressWarnings("unused")
public class PlayerListener implements Listener {

    private final TFCPlugin main;

    public PlayerListener(TFCPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(new MsgBuilder().msgKey("msg.banner").build());

        Firework firework = player.getWorld().spawn(player.getEyeLocation(), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
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

            long millisSinceLastPlayed = Calendar.getInstance().getTimeInMillis() - player.getLastPlayed();
            player.sendMessage(new MsgBuilder().def("msg.motd").formats(MiscUtils.fancifyTime(millisSinceLastPlayed)).build());
        } else {
            event.setJoinMessage(new MsgBuilder().msgKey("msg.join_new").formats(player.getDisplayName()).build());

            player.sendMessage(new MsgBuilder().def("msg.motd_new").formats(player.getName()).build());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(new MsgBuilder().msgKey("msg.leave").formats(event.getPlayer().getDisplayName()).build());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("tfcplugin.chatcolor")) {
            event.setMessage(MiscUtils.color(event.getMessage()));
        }

        for (Player pingedPlayer : Bukkit.getOnlinePlayers()) {
            if (!player.equals(pingedPlayer)) {
                String lastColors = ChatColor.getLastColors(event.getMessage());
                String playerName = pingedPlayer.getName();

                if (event.getMessage().toLowerCase().contains(playerName.toLowerCase())) {
                    event.setMessage(event.getMessage().replaceAll("(?i)(" + playerName + ")", ChatColor.GOLD + "$1" + (lastColors.isEmpty() ? ChatColor.RESET : lastColors)));
                    pingedPlayer.playSound(pingedPlayer.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2);
                }
            }
        }

        event.setFormat(MiscUtils.color(Objects.requireNonNull(main.getConfig().getString("msg.chat_format"))
                .replace("{prefix}", LuckPermsUtils.getPlayerPrefix(player))
                .replace("{suffix}", LuckPermsUtils.getPlayerSuffix(player))));
    }
}
