package com.outlook.tehbrian.tfcplugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static org.bukkit.Sound.*;

public class Piano {

    final private static Set<UUID> playerEnabledPiano = new HashSet<>();
    final private static Map<UUID, Sound> playerPianoInstrument = new HashMap<>();

    private Piano() {
    }

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

    public static void play(Player player, ItemStack item, boolean requireToggle) {
        if ((!requireToggle || getPlayerEnabledPiano(player)) && player.hasPermission("tfcplugin.piano")) {
            if (item != null && item.getType() == Material.STAINED_GLASS_PANE) {
                if (item.getItemMeta().hasLore() && Objects.requireNonNull(item.getLore()).get(1).equals(ChatColor.DARK_GRAY + "[Note]")) {
                    player.getWorld().playSound(player.getLocation(), getPlayerPianoInstrument(player), SoundCategory.MASTER, 3, Float.parseFloat(item.getLore().get(2)));
                }
            }
        }
    }

    public enum PianoSound {
        BASEDRUM(BLOCK_NOTE_BASEDRUM),
        BASS(BLOCK_NOTE_BASS),
        BEL(BLOCK_NOTE_BELL),
        CHIME(BLOCK_NOTE_CHIME),
        FLUTE(BLOCK_NOTE_FLUTE),
        GUITAR(BLOCK_NOTE_GUITAR),
        HARP(BLOCK_NOTE_HARP),
        HAT(BLOCK_NOTE_HAT),
        PLING(BLOCK_NOTE_PLING),
        SNARE(BLOCK_NOTE_SNARE),
        XYLOPHONE(BLOCK_NOTE_XYLOPHONE);

        private final Sound sound;

        PianoSound(Sound sound) {
            this.sound = sound;
        }

        public Sound toSound() {
            return sound;
        }
    }
}
