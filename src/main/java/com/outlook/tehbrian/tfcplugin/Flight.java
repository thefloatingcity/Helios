package com.outlook.tehbrian.tfcplugin;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Flight {

    private Flight() {}

    private static Main main = Main.getInstance();

    private static Set<UUID> playerCanFly = new HashSet<>();

    public static boolean getPlayerCanFly(Player player) {
        return playerCanFly.contains(player.getUniqueId());
    }

    public static void setPlayerCanFly(Player player, Boolean bool) {
        if (bool && player.hasPermission("tfcplugin.fly")) {
            playerCanFly.add(player.getUniqueId());
            enableFlight(player);
        } else {
            playerCanFly.remove(player.getUniqueId());
            disableFlight(player);
        }
    }

    public static void enableFlight(Player player) {
        if (getPlayerCanFly(player) && player.hasPermission("tfcplugin.fly")) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    public static void disableFlight(Player player) {
        if (!getPlayerCanFly(player) || !player.hasPermission("tfcplugin.fly")) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }

}
