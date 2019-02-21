package com.outlook.tehbrian.tfcplugin;

import com.outlook.tehbrian.tfcplugin.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();
        saveConfig();

        getServer().getPluginManager().registerEvents(new EventsHandler(this), this);

        getCommand("zap").setExecutor(new ZapCommand(this));
        getCommand("launch").setExecutor(new LaunchCommand(this));
        getCommand("blame").setExecutor(new BlameCommand(this));
        getCommand("shrug").setExecutor(new ShrugCommand(this));
        getCommand("sue").setExecutor(new SueCommand(this));
        getCommand("hat").setExecutor(new HatCommand(this));
        getCommand("poke").setExecutor(new PokeCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("I hope to see you again soon!");
    }

    public String formatChat(Boolean prefix, String configkey, Object... formats) {
        if (prefix) {
            return ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix") + String.format(getConfig().getString(configkey), formats));
        }
        return ChatColor.translateAlternateColorCodes('&', String.format(getConfig().getString(configkey), formats));
    }

    public String formatChat(String configkey, Object... formats) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix") + String.format(getConfig().getString(configkey), formats));
    }

    public Location getSpawn() {
        return new Location(Bukkit.getWorlds().get(0), getConfig().getDouble("spawn.X"), getConfig().getDouble("spawn.Y"), getConfig().getDouble("spawn.Z"));
    }
}