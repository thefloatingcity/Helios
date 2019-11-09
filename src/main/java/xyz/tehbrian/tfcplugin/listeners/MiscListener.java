package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import xyz.tehbrian.tfcplugin.TFCPlugin;
import xyz.tehbrian.tfcplugin.util.ConfigUtils;
import xyz.tehbrian.tfcplugin.util.LuckPermsUtils;
import xyz.tehbrian.tfcplugin.util.MiscUtils;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

import java.util.Calendar;

@SuppressWarnings("unused")
public class MiscListener implements Listener {

    private final TFCPlugin main;

    public MiscListener(TFCPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
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
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(new MsgBuilder().msgKey("msg.leave").formats(event.getPlayer().getDisplayName()).build());
    }

    @EventHandler
    public void onVoidDamageEvent(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.VOID) return;
        Location location = event.getEntity().getLocation();

        if (location.getY() > -50) return;
        event.setCancelled(true);

        if (location.getY() > -450) return;
        Bukkit.getScheduler().runTask(main, () -> {
            location.setY(650);
            event.getEntity().teleport(location);

            if (!(event.getEntity() instanceof Player)) return;
            Player player = (Player) event.getEntity();

            if (player.getFallDistance() >= 3000) {
                player.sendMessage(new MsgBuilder().prefixKey("infixes.warper.prefix").msgKey("msg.warp.max").build());
                player.setFallDistance(0);
                player.teleport(ConfigUtils.getSpawn());
                player.getWorld().strikeLightningEffect(ConfigUtils.getSpawn());
                player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, ConfigUtils.getSpawn(), 1);
                player.getWorld().playSound(ConfigUtils.getSpawn(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 4, 1);
            } else if (player.getFallDistance() >= 2000) {
                player.sendMessage(new MsgBuilder().prefixKey("infixes.warper.prefix").msgKey("msg.warp.second").build());
            } else if (player.getFallDistance() >= 1000) {
                player.sendMessage(new MsgBuilder().prefixKey("infixes.warper.prefix").msgKey("msg.warp.first").build());
            }
        });
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
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

        event.setFormat(MiscUtils.color(main.getConfig().getString("msg.chat_format")
                .replace("{prefix}", LuckPermsUtils.getPlayerPrefix(player))
                .replace("{suffix}", LuckPermsUtils.getPlayerSuffix(player))));
    }
}
