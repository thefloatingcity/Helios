package com.outlook.tehbrian.tfcplugin;

import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.ImmutableList;
import com.outlook.tehbrian.tfcplugin.commands.*;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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

        // HelpCommand
        // Default
        // CatchUnknown
        // Subcommand
        // CommandAlias
        // CommandPermission
        // Description
        // Syntax
        // CommandCompletion

        manager.getCommandCompletions().registerCompletion("sounds", c -> {
            return Arrays.stream(Sound.values()).map(sound -> sound.toString()).collect(Collectors.toList());
        });

        manager.registerCommand(new EmoteCommand(this));
        manager.registerCommand(new ActionCommand(this));
        manager.registerCommand(new UtilCommand(this));
        manager.registerCommand(new GamemodeCommand(this));
        manager.registerCommand(new PianoCommand(this));
        manager.registerCommand(new RulesCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("I hope to see you again soon!");
    }
}