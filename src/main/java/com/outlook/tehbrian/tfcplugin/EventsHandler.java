package com.outlook.tehbrian.tfcplugin;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EventsHandler implements Listener {
    private final Main plugin;

    public EventsHandler(Main plugin) {
        this.plugin = plugin;
    }

    public void disableFlight(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(plugin.formatChat("msg_join", event.getPlayer().getName()));
        event.getPlayer().sendMessage(plugin.formatChat(false, "prefix-long"));
        disableFlight(event.getPlayer());

        Date date = new Date();
        long secondsSinceLastPlayed = TimeUnit.MILLISECONDS.toSeconds(date.getTime() - event.getPlayer().getLastPlayed());
        if (secondsSinceLastPlayed >= 86400) {
            long daysSinceLastPlayed = TimeUnit.SECONDS.toDays(secondsSinceLastPlayed);
            event.getPlayer().sendMessage(plugin.formatChat("motd", daysSinceLastPlayed, "days"));
        } else if (secondsSinceLastPlayed >= 3600) {
            long hoursSinceLastPlayed = TimeUnit.SECONDS.toHours(secondsSinceLastPlayed);
            event.getPlayer().sendMessage(plugin.formatChat("motd", hoursSinceLastPlayed, "hours"));
        } else if (secondsSinceLastPlayed >= 60) {
            long minutesSinceLastPlayed = TimeUnit.SECONDS.toMinutes(secondsSinceLastPlayed);
            event.getPlayer().sendMessage(plugin.formatChat("motd", minutesSinceLastPlayed, "minutes"));
        } else {
            event.getPlayer().sendMessage(plugin.formatChat("motd", secondsSinceLastPlayed, "seconds"));
        }

        Firework f = event.getPlayer().getWorld().spawn(event.getPlayer().getLocation(), Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder().flicker(true).trail(false).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.WHITE, Color.BLUE, Color.GREEN).withFade(Color.GREEN, Color.BLUE, Color.WHITE).build());
        fm.setPower(2);
        f.setFireworkMeta(fm);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(plugin.formatChat("msg_leave", event.getPlayer().getName()));
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        disableFlight(event.getPlayer());
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        disableFlight(event.getPlayer());
    }

    public void maximumWarp(Player player) {
        if (player.getFallDistance() >= 1000) {
            player.sendMessage(plugin.formatChat("maximum_warp_speed"));
            player.teleport(plugin.getSpawn());
            player.getWorld().strikeLightningEffect(plugin.getSpawn());
            player.playSound(plugin.getSpawn(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 3, 1);
            player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, plugin.getSpawn(), 1);

        } else if (player.getFallDistance() >= 750) {
            player.sendMessage(plugin.formatChat("second_warp_speed"));
        } else if (player.getFallDistance() >= 500) {
            player.sendMessage(plugin.formatChat("first_warp_speed"));
        }
    }

    @EventHandler
    public void voidLoop(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setDamage(0);
            Location location = event.getEntity().getLocation();
            location.setY(360);
            event.getEntity().teleport(location);
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                maximumWarp(player);
                player.playSound(location, Sound.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 100000, 1);
            }
        }
    }

    @EventHandler
    public void chatColor(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("tfcplugin.chatcolor")) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
    }

    @EventHandler
    public void signColor(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("tfcplugin.signcolor")) {
            String[] lines = event.getLines();
            for (int l = 0; l <= 3; l++) {
                event.setLine(l, ChatColor.translateAlternateColorCodes('&', lines[l]));
            }
        }
    }
}
