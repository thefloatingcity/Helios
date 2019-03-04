package com.outlook.tehbrian.tfcplugin;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Misc {

    private Misc() {
    }

    private static Main main = Main.getInstance();

    public static void maximumWarp(Player player) {
        if (player.getFallDistance() >= 1000) {
            player.sendMessage(formatConfig("msg_warp_max"));
            player.teleport(getSpawn());
            player.getWorld().strikeLightningEffect(getSpawn());
            player.getWorld().playSound(getSpawn(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 3, 1);
            player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, getSpawn(), 1);
        } else if (player.getFallDistance() >= 750) {
            player.sendMessage(formatConfig("msg_warp_second"));
        } else if (player.getFallDistance() >= 500) {
            player.sendMessage(formatConfig("msg_warp_first"));
        }
    }

    public static void oldPlayerJoin(Player player) {
        long millisSinceLastPlayed = Calendar.getInstance().getTimeInMillis() - player.getLastPlayed();
        if (millisSinceLastPlayed >= 86400000) {
            player.sendMessage(formatConfig("msg_motd", TimeUnit.MILLISECONDS.toDays(millisSinceLastPlayed), "days"));
        } else if (millisSinceLastPlayed >= 3600000) {
            player.sendMessage(formatConfig("msg_motd", TimeUnit.MILLISECONDS.toHours(millisSinceLastPlayed), "hours"));
        } else if (millisSinceLastPlayed >= 60000) {
            player.sendMessage(formatConfig("msg_motd", TimeUnit.MILLISECONDS.toMinutes(millisSinceLastPlayed), "minutes"));
        } else if (millisSinceLastPlayed >= 1000) {
            player.sendMessage(formatConfig("msg_motd", TimeUnit.MILLISECONDS.toSeconds(millisSinceLastPlayed), "seconds"));
        } else {
            player.sendMessage(formatConfig("msg_motd", millisSinceLastPlayed, "milliseconds"));
        }
    }

    public static void newPlayerJoin(Player player) {
        player.sendMessage(formatConfig("msg_welcome", player.getName()));
    }

    public static String formatConfig(String configkey, Object... formats) {
        return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("msg_prefix") + String.format(main.getConfig().getString(configkey), formats));
    }

    public static String formatConfig(Boolean useprefix, String configkey, Object... formats) {
        if (useprefix) {
            return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("msg_prefix") + String.format(main.getConfig().getString(configkey), formats));
        }
        return ChatColor.translateAlternateColorCodes('&', String.format(main.getConfig().getString(configkey), formats));
    }

    public static Location getSpawn() {
        return new Location(Bukkit.getWorld(main.getConfig().getString("spawn.world")), main.getConfig().getDouble("spawn.x"), main.getConfig().getDouble("spawn.y"), main.getConfig().getDouble("spawn.z"));
    }

    public static ItemStack createItem(String name, List<String> lore, Material material, int amount, int data) {
        ItemStack i = new ItemStack(material, amount);
        ItemMeta im = i.getItemMeta();
        for (int x = 0; x < lore.size(); x++) {
            lore.set(x, ChatColor.translateAlternateColorCodes('&', lore.get(x)));
        }
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        im.setLore(lore);
        i.setDurability((short) data);
        i.setItemMeta(im);
        return i;
    }
}
