package xyz.tehbrian.tfcplugin;

import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.tehbrian.tehlib.paper.TehPlugin;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.tehbrian.tfcplugin.command.BroadcastCommand;
import xyz.tehbrian.tfcplugin.command.CommandService;
import xyz.tehbrian.tfcplugin.command.HatCommand;
import xyz.tehbrian.tfcplugin.command.RulesCommand;
import xyz.tehbrian.tfcplugin.command.WorldCommands;
import xyz.tehbrian.tfcplugin.command.legacy.ActCommand;
import xyz.tehbrian.tfcplugin.command.legacy.CoreCommand;
import xyz.tehbrian.tfcplugin.command.legacy.EmoteCommand;
import xyz.tehbrian.tfcplugin.command.legacy.GamemodeCommand;
import xyz.tehbrian.tfcplugin.command.legacy.OntimeCommand;
import xyz.tehbrian.tfcplugin.command.legacy.PianoCommand;
import xyz.tehbrian.tfcplugin.config.BooksConfig;
import xyz.tehbrian.tfcplugin.config.ConfigConfig;
import xyz.tehbrian.tfcplugin.config.EmotesConfig;
import xyz.tehbrian.tfcplugin.config.InventoriesConfig;
import xyz.tehbrian.tfcplugin.config.LangConfig;
import xyz.tehbrian.tfcplugin.inject.ConfigModule;
import xyz.tehbrian.tfcplugin.inject.PluginModule;
import xyz.tehbrian.tfcplugin.inject.UserModule;
import xyz.tehbrian.tfcplugin.listeners.AntiBuildListener;
import xyz.tehbrian.tfcplugin.listeners.ChatListener;
import xyz.tehbrian.tfcplugin.listeners.FlightListener;
import xyz.tehbrian.tfcplugin.listeners.PianoListener;
import xyz.tehbrian.tfcplugin.listeners.PlayerListener;
import xyz.tehbrian.tfcplugin.listeners.TransportationListener;
import xyz.tehbrian.tfcplugin.listeners.VoidLoopListener;

public final class FloatyPlugin extends TehPlugin {

    /**
     * The Guice injector.
     */
    private @MonotonicNonNull Injector injector;

    @Override
    public void onEnable() {
        try {
            this.injector = Guice.createInjector(
                    new ConfigModule(),
                    new PluginModule(this),
                    new UserModule()
            );
        } catch (final Exception e) {
            this.getLog4JLogger().error("Something went wrong while creating the Guice injector.");
            this.getLog4JLogger().error("Disabling plugin.");
            this.disableSelf();
            this.getLog4JLogger().error("Printing stack trace, please send this to the developers:", e);
            return;
        }

        this.loadConfigs();
        this.setupListeners();
        this.setupCommands();

        if (!this.injector.getInstance(LuckPermsService.class).load()) {
            this.getLogger().severe("No LuckPerms dependency found. Disabling plugin.");
            this.disableSelf();
        }
    }

    public void loadConfigs() {
        this.saveResourceSilently("books.conf");
        this.saveResourceSilently("config.conf");
        this.saveResourceSilently("emotes.conf");
        this.saveResourceSilently("inventories.conf");
        this.saveResourceSilently("lang.conf");

        this.injector.getInstance(BooksConfig.class).load();
        this.injector.getInstance(ConfigConfig.class).load();
        this.injector.getInstance(EmotesConfig.class).load();
        this.injector.getInstance(InventoriesConfig.class).load();
        this.injector.getInstance(LangConfig.class).load();
    }

    private void setupListeners() {
        registerListeners(
                this.injector.getInstance(AntiBuildListener.class),
                this.injector.getInstance(ChatListener.class),
                this.injector.getInstance(FlightListener.class),
                this.injector.getInstance(PianoListener.class),
                this.injector.getInstance(PlayerListener.class),
                this.injector.getInstance(TransportationListener.class),
                this.injector.getInstance(VoidLoopListener.class)
        );
    }

    private void setupCommands() {
        final @NonNull CommandService commandService = this.injector.getInstance(CommandService.class);
        commandService.init();

        final cloud.commandframework.paper.@Nullable PaperCommandManager<CommandSender> commandManager = commandService.get();
        if (commandManager == null) {
            this.getLog4JLogger().error("The CommandService was null after initialization!");
            this.getLog4JLogger().error("Disabling plugin.");
            this.disableSelf();
            return;
        }

        this.injector.getInstance(BroadcastCommand.class).register(commandManager);
        this.injector.getInstance(HatCommand.class).register(commandManager);
        this.injector.getInstance(RulesCommand.class).register(commandManager);
        this.injector.getInstance(WorldCommands.class).register(commandManager);

        this.setupLegacyCommands();
    }

    private void setupLegacyCommands() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);

        commandManager.registerCommand(this.injector.getInstance(ActCommand.class));
        commandManager.registerCommand(this.injector.getInstance(CoreCommand.class));
        commandManager.registerCommand(this.injector.getInstance(EmoteCommand.class));
        commandManager.registerCommand(this.injector.getInstance(GamemodeCommand.class));
        commandManager.registerCommand(this.injector.getInstance(OntimeCommand.class));
        commandManager.registerCommand(this.injector.getInstance(PianoCommand.class));

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

}
