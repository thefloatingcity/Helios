package com.outlook.tehbrian.tfcplugin;

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

    private final double FS_GB1 = 0.5;
    private final double G1 = 0.529732;
    private final double GS_AB1 = 0.561231;
    private final double A1 = 0.594604;
    private final double AS_BB1 = 0.629961;
    private final double B1 = 0.667420;
    private final double C1 = 0.707107;
    private final double CS_DB1 = 0.749154;
    private final double D1 = 0.793701;
    private final double DS_EB1 = 0.840896;
    private final double E1 = 0.890899;
    private final double F1 = 0.943874;
    private final double FS_GB2 = 1;
    private final double G2 = 1.059463;
    private final double GS_AB2 = 1.122462;
    private final double A2 = 1.189207;
    private final double AS_BB2 = 1.259921;
    private final double B2 = 1.334840;
    private final double C2 = 1.414214;
    private final double CS_DB2 = 1.498307;
    private final double D2 = 1.587401;
    private final double DS_EB2 = 1.681793;
    private final double E2 = 1.781797;
    private final double F2 = 1.887749;
    private final double FS_GB3 = 2;

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
            if (item.getType() == Material.STAINED_GLASS) {
                player.sendMessage(item.getLore().get(0));
                player.sendMessage(item.getLore().get(1));
                player.sendMessage(item.getLore().get(2));
            }
            //&& item.getLore().get(0).equals("[Piano]"))
            // { player.getWorld().playSound(player.getLocation(), getPlayerPianoInstrument(player), SoundCategory.MASTER, 3, Float.parseFloat(item.getLore().get(1)));
        }
    }
}
