package xyz.tehbrian.tfcplugin.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.tehbrian.tfcplugin.TFCPlugin;

import java.io.File;

public class ConfigManager {

    private final TFCPlugin main;

    public ConfigManager(final TFCPlugin main) {
        this.main = main;

        final File whateverFile = new File(main.getDataFolder() + "/whatever.yml");
        final FileConfiguration whatever = YamlConfiguration.loadConfiguration(whateverFile);
    }

}
