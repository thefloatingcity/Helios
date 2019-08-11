package com.outlook.tehbrian.tfcplugin.piano;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class PianoManager {

    final private static Set<UUID> playerEnabledPiano = new HashSet<>();
    final private static Map<UUID, Sound> playerPianoInstrument = new HashMap<>();

    private PianoManager() {
    }

    public static boolean getPlayerEnabledPiano(Player player) {
        return playerEnabledPiano.contains(player.getUniqueId());
    }

    public static Sound getPlayerPianoInstrument(Player player) {
        return playerPianoInstrument.getOrDefault(player.getUniqueId(), Sound.BLOCK_NOTE_BLOCK_HARP);
    }

    public static void setPlayerEnabledPiano(Player player, boolean bool) {
        if (player.hasPermission("tfcplugin.piano") && bool) {
            playerEnabledPiano.add(player.getUniqueId());
        } else {
            playerEnabledPiano.remove(player.getUniqueId());
        }
    }

    public static void setPlayerPianoInstrument(Player player, Sound sound) {
        if (player.hasPermission("tfcplugin.piano")) {
            playerPianoInstrument.put(player.getUniqueId(), sound);
        }
    }

    public static void play(Player player, ItemStack item, boolean requireToggle) {
        if (player.hasPermission("tfcplugin.piano") && ((!requireToggle || getPlayerEnabledPiano(player)))) {
            if (item != null && item.getType().name().toLowerCase().contains("pane")) {
                if (item.getItemMeta().hasLore() && Objects.requireNonNull(item.getLore()).get(1).equals(ChatColor.DARK_GRAY + "[Note]")) {
                    player.getWorld().playSound(player.getEyeLocation(), getPlayerPianoInstrument(player), SoundCategory.MASTER, 3, Float.parseFloat(item.getLore().get(2)));
                }
            }
        }
    }
}
