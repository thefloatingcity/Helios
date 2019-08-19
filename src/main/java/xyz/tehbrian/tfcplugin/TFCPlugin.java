package xyz.tehbrian.tfcplugin;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.tehbrian.tfcplugin.commands.ActionCommand;
import xyz.tehbrian.tfcplugin.commands.CoreCommand;
import xyz.tehbrian.tfcplugin.commands.EmoteCommand;
import xyz.tehbrian.tfcplugin.commands.GamemodeCommand;
import xyz.tehbrian.tfcplugin.commands.ItemCommand;
import xyz.tehbrian.tfcplugin.commands.OntimeCommand;
import xyz.tehbrian.tfcplugin.commands.PianoCommand;
import xyz.tehbrian.tfcplugin.commands.RulesCommand;
import xyz.tehbrian.tfcplugin.commands.UtilCommand;
import xyz.tehbrian.tfcplugin.events.AntiBuildEvents;
import xyz.tehbrian.tfcplugin.events.BuildingEvents;
import xyz.tehbrian.tfcplugin.events.MiscEvents;
import xyz.tehbrian.tfcplugin.flight.FlightEvents;
import xyz.tehbrian.tfcplugin.piano.PianoEvents;

public final class TFCPlugin extends JavaPlugin {

    private static TFCPlugin instance;
    private PaperCommandManager commandManager;
    private LuckPermsApi luckPermsApi;

    public TFCPlugin() {
        instance = this;
    }

    public static TFCPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        setupCommandManager();
        setupEvents();
        setupConfig();

        if (!setupLuckPermsApi()) {
            getLogger().severe("No LuckPerms dependency found! Disabling plugin..");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("See you later!");
    }

    private void setupCommandManager() {
        commandManager = new PaperCommandManager(this);

        commandManager.registerCommand(new ActionCommand(this));
        commandManager.registerCommand(new CoreCommand(this));
        commandManager.registerCommand(new EmoteCommand());
        commandManager.registerCommand(new GamemodeCommand());
        commandManager.registerCommand(new ItemCommand());
        commandManager.registerCommand(new OntimeCommand());
        commandManager.registerCommand(new PianoCommand(this));
        commandManager.registerCommand(new RulesCommand());
        commandManager.registerCommand(new UtilCommand(this));

        commandManager.enableUnstableAPI("help");

        commandManager.getCommandConditions().addCondition(Integer.class, "limits", (context, executionContext, value) -> {
            if (value == null) {
                return;
            }
            if (context.hasConfig("min") && context.getConfigValue("min", 0) > value) {
                throw new ConditionFailedException("Minimum value is " + context.getConfigValue("min", 0) + ".");
            }
            if (context.hasConfig("max") && context.getConfigValue("max", 10) < value) {
                throw new ConditionFailedException("Maximum value is " + context.getConfigValue("max", 10) + ".");
            }
        });
    }

    private void setupEvents() {
        getServer().getPluginManager().registerEvents(new AntiBuildEvents(), this);
        getServer().getPluginManager().registerEvents(new BuildingEvents(this), this);
        getServer().getPluginManager().registerEvents(new MiscEvents(this), this);
        getServer().getPluginManager().registerEvents(new FlightEvents(), this);
        getServer().getPluginManager().registerEvents(new PianoEvents(this), this);
    }

    private void setupConfig() {
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();
    }

    private boolean setupLuckPermsApi() {
        RegisteredServiceProvider<LuckPermsApi> prsp = getServer().getServicesManager().getRegistration(LuckPermsApi.class);
        if (prsp == null) {
            return false;
        }
        luckPermsApi = prsp.getProvider();
        return true;
    }

    public PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public LuckPermsApi getLuckPermsApi() {
        return luckPermsApi;
    }
}