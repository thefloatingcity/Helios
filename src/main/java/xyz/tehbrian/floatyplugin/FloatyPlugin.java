package xyz.tehbrian.floatyplugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.tehbrian.tehlib.paper.TehPlugin;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import xyz.tehbrian.floatyplugin.command.ActCommands;
import xyz.tehbrian.floatyplugin.command.BroadcastCommand;
import xyz.tehbrian.floatyplugin.command.CommandService;
import xyz.tehbrian.floatyplugin.command.FlyCommand;
import xyz.tehbrian.floatyplugin.command.FunCommands;
import xyz.tehbrian.floatyplugin.command.GamemodeCommands;
import xyz.tehbrian.floatyplugin.command.HatCommand;
import xyz.tehbrian.floatyplugin.command.FloatyPluginCommand;
import xyz.tehbrian.floatyplugin.command.PianoCommand;
import xyz.tehbrian.floatyplugin.command.PlaytimeCommands;
import xyz.tehbrian.floatyplugin.command.RulesCommand;
import xyz.tehbrian.floatyplugin.command.WorldCommands;
import xyz.tehbrian.floatyplugin.config.BooksConfig;
import xyz.tehbrian.floatyplugin.config.ConfigConfig;
import xyz.tehbrian.floatyplugin.config.EmotesConfig;
import xyz.tehbrian.floatyplugin.config.InventoriesConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.inject.ConfigModule;
import xyz.tehbrian.floatyplugin.inject.PluginModule;
import xyz.tehbrian.floatyplugin.inject.UserModule;
import xyz.tehbrian.floatyplugin.listeners.AntiBuildListener;
import xyz.tehbrian.floatyplugin.listeners.ChatListener;
import xyz.tehbrian.floatyplugin.listeners.PianoListener;
import xyz.tehbrian.floatyplugin.listeners.JoinQuitListener;
import xyz.tehbrian.floatyplugin.listeners.TransportationListener;
import xyz.tehbrian.floatyplugin.listeners.VoidLoopListener;

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
                this.injector.getInstance(PianoListener.class),
                this.injector.getInstance(JoinQuitListener.class),
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

        this.injector.getInstance(ActCommands.class).register(commandManager);
        this.injector.getInstance(BroadcastCommand.class).register(commandManager);
        this.injector.getInstance(FloatyPluginCommand.class).register(commandManager);
        this.injector.getInstance(FlyCommand.class).register(commandManager);
        this.injector.getInstance(FunCommands.class).register(commandManager);
        this.injector.getInstance(GamemodeCommands.class).register(commandManager);
        this.injector.getInstance(HatCommand.class).register(commandManager);
        this.injector.getInstance(PianoCommand.class).register(commandManager);
        this.injector.getInstance(PlaytimeCommands.class).register(commandManager);
        this.injector.getInstance(RulesCommand.class).register(commandManager);
        this.injector.getInstance(WorldCommands.class).register(commandManager);
    }

}
