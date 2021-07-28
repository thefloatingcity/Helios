package xyz.tehbrian.tfcplugin;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.tehbrian.tfcplugin.commands.ActionCommand;
import xyz.tehbrian.tfcplugin.commands.BoosterCommand;
import xyz.tehbrian.tfcplugin.commands.CoreCommand;
import xyz.tehbrian.tfcplugin.commands.EmoteCommand;
import xyz.tehbrian.tfcplugin.commands.GamemodeCommand;
import xyz.tehbrian.tfcplugin.commands.MenuCommand;
import xyz.tehbrian.tfcplugin.commands.OntimeCommand;
import xyz.tehbrian.tfcplugin.commands.PianoCommand;
import xyz.tehbrian.tfcplugin.commands.RulesCommand;
import xyz.tehbrian.tfcplugin.commands.UtilCommand;
import xyz.tehbrian.tfcplugin.config.ConfigManager;
import xyz.tehbrian.tfcplugin.listeners.AntiBuildListener;
import xyz.tehbrian.tfcplugin.listeners.ChatListener;
import xyz.tehbrian.tfcplugin.listeners.FlightListener;
import xyz.tehbrian.tfcplugin.listeners.PianoListener;
import xyz.tehbrian.tfcplugin.listeners.PlayerListener;
import xyz.tehbrian.tfcplugin.listeners.VoidLoopListener;
import xyz.tehbrian.tfcplugin.user.UserManager;

public final class TFCPlugin extends JavaPlugin {

    private static TFCPlugin instance;

    private LuckPerms luckPermsApi;
    private PaperCommandManager commandManager;
    private UserManager userManager;

    private ConfigManager configManager;

    public TFCPlugin() {
        instance = this;
    }

    public static TFCPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        this.setupConfig();
        this.setupListeners();
        this.setupCommands();

        if (!this.setupLuckPermsApi()) {
            this.getLogger().severe("No LuckPerms dependency found! Disabling plugin..");
            this.getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        this.getLogger().info("See you later!");
    }

    private void setupConfig() {
        this.saveDefaultConfig();
    }

    private void setupListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();

        pluginManager.registerEvents(new AntiBuildListener(), this);
        pluginManager.registerEvents(new ChatListener(this), this);
        pluginManager.registerEvents(new FlightListener(), this);
        pluginManager.registerEvents(new PianoListener(this), this);
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new VoidLoopListener(this), this);
    }

    private void setupCommands() {
        this.commandManager = new PaperCommandManager(this);

        this.commandManager.registerCommand(new ActionCommand(this));
        this.commandManager.registerCommand(new BoosterCommand(this));
        this.commandManager.registerCommand(new CoreCommand(this));
        this.commandManager.registerCommand(new EmoteCommand());
        this.commandManager.registerCommand(new GamemodeCommand());
        this.commandManager.registerCommand(new MenuCommand());
        this.commandManager.registerCommand(new OntimeCommand());
        this.commandManager.registerCommand(new PianoCommand());
        this.commandManager.registerCommand(new RulesCommand());
        this.commandManager.registerCommand(new UtilCommand());

        this.commandManager.enableUnstableAPI("help");

        this.commandManager.getCommandConditions().addCondition(Integer.class, "limits", (context, executionContext, value) -> {
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

    private boolean setupLuckPermsApi() {
        final RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            return false;
        }
        this.luckPermsApi = provider.getProvider();
        return true;
    }

    public PaperCommandManager getCommandManager() {
        return this.commandManager;
    }

    public LuckPerms getLuckPermsApi() {
        return this.luckPermsApi;
    }

    public UserManager getUserManager() {
        if (this.userManager == null) {
            this.userManager = new UserManager();
        }
        return this.userManager;
    }

    public ConfigManager getConfigManager() {
        if (this.configManager == null) {
            this.configManager = new ConfigManager(this);
        }
        return this.configManager;
    }

}
