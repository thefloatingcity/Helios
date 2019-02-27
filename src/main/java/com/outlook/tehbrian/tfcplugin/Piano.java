package com.outlook.tehbrian.tfcplugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Piano {

    private Piano() {}

    private static Main main = Main.getInstance();

    private static Set<UUID> playerEnabledPiano = new HashSet<>();
    private static Map<UUID, Sound> playerPianoInstrument = new HashMap<>();

    public static boolean getPlayerEnabledPiano(Player player) {
        return playerEnabledPiano.contains(player.getUniqueId());
    }

    public static void setPlayerEnabledPiano(Player player, Boolean bool) {
        if (bool && player.hasPermission("tfcplugin.piano")) {
            playerEnabledPiano.add(player.getUniqueId());
        } else {
            playerEnabledPiano.remove(player.getUniqueId());
        }
    }

    public static Sound getPlayerPianoInstrument(Player player) {
        playerPianoInstrument.putIfAbsent(player.getUniqueId(), Sound.BLOCK_NOTE_HARP);
        return playerPianoInstrument.get(player.getUniqueId());
    }

    public static void setPlayerPianoInstrument(Player player, Sound sound) {
        if (player.hasPermission("tfcplugin.piano")) {
            playerPianoInstrument.put(player.getUniqueId(), sound);
        }
    }

    public static void play(Player player, ItemStack item) {
        if (getPlayerEnabledPiano(player) && player.hasPermission("tfcplugin.piano")) {
            if (item != null) {
                if (item.getType() == Material.STAINED_GLASS_PANE && item.getItemMeta().hasLore()) {
                    if (item.getLore().get(1).equals(ChatColor.DARK_GRAY + "[Note]")) {
                        player.getWorld().playSound(player.getLocation(), getPlayerPianoInstrument(player), SoundCategory.MASTER, 3, Float.parseFloat(item.getLore().get(2)));
                    }
                }
            }
        }
    }
}
