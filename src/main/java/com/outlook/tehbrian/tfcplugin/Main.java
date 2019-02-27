package com.outlook.tehbrian.tfcplugin;

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
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new EventsHandler(this), this);

        getCommand("zap").setExecutor(new ZapCommand(this));
        getCommand("launch").setExecutor(new LaunchCommand(this));
        getCommand("blame").setExecutor(new BlameCommand(this));
        getCommand("shrug").setExecutor(new ShrugCommand(this));
        getCommand("sue").setExecutor(new SueCommand(this));
        getCommand("hat").setExecutor(new HatCommand(this));
        getCommand("poke").setExecutor(new PokeCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("gm").setExecutor(new GamemodeCommand(this));
        getCommand("rules").setExecutor(new RulesCommand(this));
        getCommand("help").setExecutor(new HelpCommand(this));
        getCommand("piano").setExecutor(new PianoCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("I hope to see you again soon!");
    }
}