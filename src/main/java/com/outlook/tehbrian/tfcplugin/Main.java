package com.outlook.tehbrian.tfcplugin;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.outlook.tehbrian.tfcplugin.commands.*;
import com.outlook.tehbrian.tfcplugin.events.AntiBuildEvents;
import com.outlook.tehbrian.tfcplugin.events.BuildingEvents;
import com.outlook.tehbrian.tfcplugin.events.MiscEvents;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    private LuckPermsApi luckPermsApi;
    private MongoClient mongoClient;
    private MongoDatabase database;

    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        setupDatabase();

        setupCommandManager();

        if (!setupLuckPermsApi()) {
            getLogger().severe("No LuckPerms dependency found! Disabling plugin..");
            getServer().getPluginManager().disablePlugin(this);
        }

        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new AntiBuildEvents(), this);
        getServer().getPluginManager().registerEvents(new BuildingEvents(this), this);
        getServer().getPluginManager().registerEvents(new MiscEvents(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Closing database..");
        mongoClient.close();
        getLogger().info("Database has been closed!");
    }

    private void setupDatabase() {
        mongoClient = MongoClients.create("mongodb+srv://TFCPlugin:thefloatingdatabase12345@cluster0-hkzse.mongodb.net/test?retryWrites=true");
        database = mongoClient.getDatabase("tfc");
    }

    private void setupCommandManager() {
        PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new ActionCommand(this));
        manager.registerCommand(new CoreCommand(this));
        manager.registerCommand(new EmoteCommand(this));
        manager.registerCommand(new GamemodeCommand(this));
        manager.registerCommand(new PianoCommand(this));
        manager.registerCommand(new RulesCommand(this));
        manager.registerCommand(new UtilCommand(this));

        manager.enableUnstableAPI("help");

        manager.getCommandConditions().addCondition(Integer.class, "limits", (context, executionContext, value) -> {
            if (value == null) {
                return;
            }
            if (context.hasConfig("min") && context.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("Minimum value is " + context.getConfigValue("min", 0) + ".");
            }
            if (context.hasConfig("max") && context.getConfigValue("max", 3) < value) {
                throw new ConditionFailedException("Maximum value is " + context.getConfigValue("max", 3) + ".");
            }
        });
    }

    private boolean setupLuckPermsApi() {
        RegisteredServiceProvider<LuckPermsApi> prsp = getServer().getServicesManager().getRegistration(LuckPermsApi.class);
        if (prsp == null) {
            return false;
        }
        luckPermsApi = prsp.getProvider();
        return luckPermsApi != null;
    }

    public LuckPermsApi getLuckPermsApi() {
        return luckPermsApi;
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}