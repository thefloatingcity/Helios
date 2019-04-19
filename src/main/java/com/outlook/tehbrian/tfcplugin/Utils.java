package com.outlook.tehbrian.tfcplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Utils {

    private static final Main main = Main.getInstance();

    private Utils() {
    }

    public static String emote(CommandSender sender, String configKey) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            return Utils.formatC("none", configKey, player.getDisplayName());
        } else {
            return Utils.formatC("none", configKey, sender.getName());
        }
    }

    public static String emote(CommandSender sender, String configKey, String text) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            return Utils.formatC("none", configKey, player.getDisplayName(), text);
        } else {
            return Utils.formatC("none", configKey, sender.getName(), text);
        }
    }

    public static String format(String configKey, Object... replacements) {
        return formatC("tfc_prefix", configKey, replacements);
    }

    public static String formatC(String prefixKey, String configKey, Object... replacements) {
        FileConfiguration config = main.getConfig();
        if (prefixKey.equalsIgnoreCase("none")) {
            return colorString(String.format(config.getString(configKey), replacements));
        }
        return colorString(config.getString(prefixKey) + " " + String.format(config.getString(configKey), replacements));
    }

    public static String colorString(String string) {
        return string == null ? null : ChatColor.translateAlternateColorCodes('&', string);
    }

    public static Location getSpawn() {
        FileConfiguration config = main.getConfig();
        return new Location(Bukkit.getWorld(config.getString("spawn.world")), config.getDouble("spawn.x"), config.getDouble("spawn.y"), config.getDouble("spawn.z"));
    }

    public static ItemStack createItem(String name, Material material, int amount, int data) {
        ItemStack i = new ItemStack(material, amount);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(colorString(name));
        i.setDurability((short) data);
        i.setItemMeta(im);
        return i;
    }

    public static ItemStack createItem(String name, List<String> lore, Material material, int amount, int data) {
        ItemStack i = new ItemStack(material, amount);
        ItemMeta im = i.getItemMeta();
        for (int x = 0; x < lore.size(); x++) {
            lore.set(x, colorString(lore.get(x)));
        }
        im.setDisplayName(colorString(name));
        im.setLore(lore);
        i.setDurability((short) data);
        i.setItemMeta(im);
        return i;
    }

    public static boolean isTop(Player player, Block block) {
        Location start = player.getEyeLocation().clone();
        while (!start.getBlock().equals(block) && start.distance(player.getEyeLocation()) < 6.0D) {
            start.add(player.getLocation().getDirection().multiply(0.05D));
        }
        return start.getY() % 1.0D > 0.5D;
    }
}
