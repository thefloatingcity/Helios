package com.outlook.tehbrian.tfcplugin.flight;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FlightManager {

    final private static Set<UUID> playerCanBypassFly = new HashSet<>();

    private FlightManager() {
    }

    public static boolean getPlayerCanBypassFly(Player player) {
        return playerCanBypassFly.contains(player.getUniqueId());
    }

    public static void setPlayerCanBypassFly(Player player, boolean bool) {
        if (player.hasPermission("tfcplugin.util.fly") && bool) {
            playerCanBypassFly.add(player.getUniqueId());
            enableFlight(player);
        } else {
            playerCanBypassFly.remove(player.getUniqueId());
            disableFlight(player);
        }
    }

    public static void enableFlight(Player player) {
        if (player.hasPermission("tfcplugin.util.fly") && getPlayerCanBypassFly(player)) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    public static void disableFlight(Player player) {
        if (!player.hasPermission("tfcplugin.util.fly") || !getPlayerCanBypassFly(player)) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
}
