package com.outlook.tehbrian.tfcplugin.events;

import com.outlook.tehbrian.tfcplugin.TFCPlugin;
import com.outlook.tehbrian.tfcplugin.util.LuckPermsUtils;
import com.outlook.tehbrian.tfcplugin.util.MiscUtils;
import com.outlook.tehbrian.tfcplugin.util.MsgBuilder;
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

import java.util.Calendar;

@SuppressWarnings("unused")
public class MiscEvents implements Listener {

    private final TFCPlugin main;

    public MiscEvents(TFCPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(new MsgBuilder().msgKey("tfc_banner").build());

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
            event.setJoinMessage(new MsgBuilder().msgKey("msg.join").replace(player.getDisplayName()).build());

            long millisSinceLastPlayed = Calendar.getInstance().getTimeInMillis() - player.getLastPlayed();
            player.sendMessage(new MsgBuilder().def("msg.motd").replace(MiscUtils.fancifyTime(millisSinceLastPlayed)).build());
        } else {
            event.setJoinMessage(new MsgBuilder().msgKey("msg.join_new").replace(player.getDisplayName()).build());

            player.sendMessage(new MsgBuilder().def("msg.motd_new").replace(player.getName()).build());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(new MsgBuilder().msgKey("msg.leave").replace(event.getPlayer().getDisplayName()).build());
    }

    @EventHandler
    public void onVoidDamageEvent(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            Location location = event.getEntity().getLocation();
            if (location.getY() <= -50) {
                event.setCancelled(true);
                event.setDamage(0);
                if (location.getY() <= -300) {
                    event.setCancelled(false);
                    location.setY(600);
                    event.getEntity().teleport(location);
                    if (event.getEntity() instanceof Player) {
                        Player player = (Player) event.getEntity();
                        if (player.getFallDistance() >= 3000) {
                            player.sendMessage(new MsgBuilder().prefixKey("infixes.warper.prefix").msgKey("msg.warp_max").build());
                            player.teleport(MiscUtils.getSpawn());
                            player.getWorld().strikeLightningEffect(MiscUtils.getSpawn());
                            player.getWorld().playSound(MiscUtils.getSpawn(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 3, 1);
                            player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, MiscUtils.getSpawn(), 1);
                        } else if (player.getFallDistance() >= 2000) {
                            player.sendMessage(new MsgBuilder().prefixKey("infixes.warper.prefix").msgKey("msg.warp_second").build());
                        } else if (player.getFallDistance() >= 1000) {
                            player.sendMessage(new MsgBuilder().prefixKey("infixes.warper.prefix").msgKey("msg.warp_first").build());
                        }
                        player.getWorld().playSound(location, Sound.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 4, 1);
                    }
                }
            }
        }
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
                    pingedPlayer.playSound(pingedPlayer.getEyeLocation(), Sound.BLOCK_NOTE_PLING, 1000, 2);
                }
            }
        }

        event.setFormat(MiscUtils.color(main.getConfig().getString("chat_format")
                .replace("{prefix}", LuckPermsUtils.getPlayerPrefix(player))
                .replace("{suffix}", LuckPermsUtils.getPlayerSuffix(player))));
    }
}
