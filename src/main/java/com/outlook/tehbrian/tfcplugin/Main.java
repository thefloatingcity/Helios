package com.outlook.tehbrian.tfcplugin;

import co.aikar.commands.PaperCommandManager;
import com.outlook.tehbrian.tfcplugin.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance = null;

    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        PaperCommandManager manager = new PaperCommandManager(this);

        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new EventsHandler(this), this);

        manager.registerCommand(new EmoteCommand(this));
        manager.registerCommand(new ActionsCommand(this));
        manager.registerCommand(new UtilCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("I hope to see you again soon!");
    }
}