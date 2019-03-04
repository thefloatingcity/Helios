package com.outlook.tehbrian.tfcplugin;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Flight {

    private Flight() {
    }

    private static Set<UUID> canBypassFly = new HashSet<>();

    public static boolean getCanBypassFly(Player player) {
        return canBypassFly.contains(player.getUniqueId());
    }

    public static void setCanBypassFly(Player player, Boolean bool) {
        if (bool && player.hasPermission("tfcplugin.fly")) {
            canBypassFly.add(player.getUniqueId());
            enableFlight(player);
        } else {
            canBypassFly.remove(player.getUniqueId());
            disableFlight(player);
        }
    }

    public static void enableFlight(Player player) {
        if (getCanBypassFly(player) && player.hasPermission("tfcplugin.fly")) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    public static void disableFlight(Player player) {
        if (!getCanBypassFly(player) || !player.hasPermission("tfcplugin.fly")) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }
}
