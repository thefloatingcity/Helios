package com.outlook.tehbrian.tfcplugin;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.outlook.tehbrian.tfcplugin.Utils.getSpawn;

public class Misc {

    private Misc() {
    }

    private static Main main = Main.getInstance();

    public static void maximumWarp(Player player) {
        if (player.getFallDistance() >= 1500) {
            player.sendMessage(Utils.format("msg_warp_max"));
            player.teleport(getSpawn());
            player.getWorld().strikeLightningEffect(getSpawn());
            player.getWorld().playSound(getSpawn(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 3, 1);
            player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, getSpawn(), 1);
        } else if (player.getFallDistance() >= 1000) {
            player.sendMessage(Utils.format("msg_warp_second"));
        } else if (player.getFallDistance() >= 500) {
            player.sendMessage(Utils.format("msg_warp_first"));
        }
    }

    public static void oldPlayerJoin(Player player) {
        long millisSinceLastPlayed = Calendar.getInstance().getTimeInMillis() - player.getLastPlayed();
        if (millisSinceLastPlayed >= 86400000) {
            player.sendMessage(Utils.format("msg_motd", TimeUnit.MILLISECONDS.toDays(millisSinceLastPlayed), "days"));
        } else if (millisSinceLastPlayed >= 3600000) {
            player.sendMessage(Utils.format("msg_motd", TimeUnit.MILLISECONDS.toHours(millisSinceLastPlayed), "hours"));
        } else if (millisSinceLastPlayed >= 60000) {
            player.sendMessage(Utils.format("msg_motd", TimeUnit.MILLISECONDS.toMinutes(millisSinceLastPlayed), "minutes"));
        } else if (millisSinceLastPlayed >= 1000) {
            player.sendMessage(Utils.format("msg_motd", TimeUnit.MILLISECONDS.toSeconds(millisSinceLastPlayed), "seconds"));
        } else {
            player.sendMessage(Utils.format("msg_motd", millisSinceLastPlayed, "milliseconds"));
        }
    }

    public static void newPlayerJoin(Player player) {
        player.sendMessage(Utils.format("msg_welcome", player.getName()));
    }
}
