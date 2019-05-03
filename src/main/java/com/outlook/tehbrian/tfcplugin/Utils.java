package com.outlook.tehbrian.tfcplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final Main main = Main.getInstance();

    private Utils() {
    }

    public static String format(String configKey, Object... replacements) {
        return formatC("tfc_prefix", configKey, replacements);
    }

    public static String formatC(String prefixKey, String configKey, Object... replacements) {
        FileConfiguration config = main.getConfig();
        if (prefixKey.equalsIgnoreCase("none")) {
            return color(String.format(config.getString(configKey), replacements));
        }
        return prefixC(prefixKey, String.format(config.getString(configKey), replacements));
    }

    public static String prefix(String string) {
        return prefixC("tfc_prefix", string);
    }

    public static String prefixC(String prefixKey, String string) {
        FileConfiguration config = main.getConfig();
        return color(config.getString(prefixKey) + " " + string);
    }

    public static String color(String string) {
        return string == null ? null : ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> createPage(Integer pageNumber, String type, String prefixKey, String multiKey) {
        ConfigurationSection page = main.getConfig().getConfigurationSection(type + ".page" + pageNumber);
        List<String> messages = new ArrayList<>();
        messages.add(Utils.formatC(prefixKey, "msg_page", page.getString("topic"), pageNumber));
        for (String line : page.getStringList("content")) {
            messages.add(Utils.prefixC(multiKey, line));
        }
        return messages;
    }

    public static Location getSpawn() {
        FileConfiguration config = main.getConfig();
        return new Location(Bukkit.getWorld(config.getString("spawn.world")), config.getDouble("spawn.x"), config.getDouble("spawn.y"), config.getDouble("spawn.z"));
    }

    public static ItemStack createItem(String name, Material material, int amount, int data) {
        return createItem(name, material, amount, data, new ArrayList<>());
    }

    public static ItemStack createItem(String name, Material material, int amount, int data, List<String> lore) {
        ItemStack i = new ItemStack(material, amount);
        ItemMeta im = i.getItemMeta();
        for (int x = 0; x < lore.size(); x++) {
            lore.set(x, color(lore.get(x)));
        }
        im.setDisplayName(color(name));
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
