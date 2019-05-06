package com.outlook.tehbrian.tfcplugin.utils;

import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MiscUtils {

    private static final Main main = Main.getInstance();

    private MiscUtils() {
    }

    public static Location getSpawn() {
        FileConfiguration config = main.getConfig();
        return new Location(Bukkit.getWorld(config.getString("spawn.world")), config.getDouble("spawn.x"), config.getDouble("spawn.y"), config.getDouble("spawn.z"));
    }

    public static boolean isTop(Player player, Block block) {
        Location start = player.getEyeLocation().clone();
        while (!start.getBlock().equals(block) && start.distance(player.getEyeLocation()) < 6.0D) {
            start.add(player.getLocation().getDirection().multiply(0.05D));
        }
        return start.getY() % 1.0D > 0.5D;
    }
}
