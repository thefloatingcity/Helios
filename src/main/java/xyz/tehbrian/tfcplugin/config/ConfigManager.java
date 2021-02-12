package xyz.tehbrian.tfcplugin.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.tehbrian.tfcplugin.TFCPlugin;

import java.io.File;

public class ConfigManager {

    private final TFCPlugin main;

    public ConfigManager(TFCPlugin main) {
        this.main = main;

        File whateverFile = new File(main.getDataFolder() + "/whatever.yml");
        FileConfiguration whatever = YamlConfiguration.loadConfiguration(whateverFile);
    }
}
