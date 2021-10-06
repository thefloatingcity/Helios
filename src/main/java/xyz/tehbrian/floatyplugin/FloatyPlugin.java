package xyz.tehbrian.floatyplugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.tehbrian.tehlib.core.configurate.AbstractConfig;
import dev.tehbrian.tehlib.paper.TehPlugin;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import xyz.tehbrian.floatyplugin.command.ActCommands;
import xyz.tehbrian.floatyplugin.command.BroadcastCommand;
import xyz.tehbrian.floatyplugin.command.CommandService;
import xyz.tehbrian.floatyplugin.command.FloatyPluginCommand;
import xyz.tehbrian.floatyplugin.command.FlyCommand;
import xyz.tehbrian.floatyplugin.command.FunCommands;
import xyz.tehbrian.floatyplugin.command.GamemodeCommands;
import xyz.tehbrian.floatyplugin.command.HatCommand;
import xyz.tehbrian.floatyplugin.command.InfoCommand;
import xyz.tehbrian.floatyplugin.command.MilkCommand;
import xyz.tehbrian.floatyplugin.command.PackCommand;
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
import xyz.tehbrian.floatyplugin.inject.FlightModule;
import xyz.tehbrian.floatyplugin.inject.LuckPermsModule;
import xyz.tehbrian.floatyplugin.inject.PluginModule;
import xyz.tehbrian.floatyplugin.inject.UserModule;
import xyz.tehbrian.floatyplugin.listeners.AntiBuildListener;
import xyz.tehbrian.floatyplugin.listeners.ChatListener;
import xyz.tehbrian.floatyplugin.listeners.FishingListener;
import xyz.tehbrian.floatyplugin.listeners.JoinQuitListener;
import xyz.tehbrian.floatyplugin.listeners.MilkListener;
import xyz.tehbrian.floatyplugin.listeners.ServerPingListener;
import xyz.tehbrian.floatyplugin.listeners.SpawnProtectionListener;
import xyz.tehbrian.floatyplugin.listeners.TransportationListener;
import xyz.tehbrian.floatyplugin.listeners.VoidLoopListener;
import xyz.tehbrian.floatyplugin.music.ElevatorMusicTask;
import xyz.tehbrian.floatyplugin.music.RainMusicListener;
import xyz.tehbrian.floatyplugin.piano.PianoListener;

import java.util.List;

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
                    new FlightModule(),
                    new LuckPermsModule(),
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

        for (final World world : this.getServer().getWorlds()) {
            world.setGameRule(GameRule.MOB_GRIEFING, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            world.setGameRule(GameRule.DISABLE_RAIDS, true);
            world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);

            if (world.getEnvironment() == World.Environment.NETHER) {
                world.setGameRule(GameRule.REDUCED_DEBUG_INFO, true);
                world.setGameRule(GameRule.KEEP_INVENTORY, false);
            } else {
                world.setGameRule(GameRule.KEEP_INVENTORY, true);
            }
        }

        if (!this.loadConfigs()) {
            return;
        }
        this.setupListeners();
        this.setupCommands();

        if (!this.injector.getInstance(LuckPermsService.class).load()) {
            this.getLogger().severe("LuckPerms dependency not found. Disabling plugin.");
            this.disableSelf();
        }
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    /**
     * Loads the plugin's config.
     *
     * @return whether or not it was successful
     */
    public boolean loadConfigs() {
        this.saveResourceSilently("books.conf");
        this.saveResourceSilently("config.conf");
        this.saveResourceSilently("emotes.conf");
        this.saveResourceSilently("inventories.conf");
        this.saveResourceSilently("lang.conf");

        final List<AbstractConfig<?>> configsToLoad = List.of(
                this.injector.getInstance(BooksConfig.class),
                this.injector.getInstance(ConfigConfig.class),
                this.injector.getInstance(EmotesConfig.class),
                this.injector.getInstance(InventoriesConfig.class),
                this.injector.getInstance(LangConfig.class)
        );

        for (final AbstractConfig<?> config : configsToLoad) {
            try {
                config.load();
            } catch (final ConfigurateException e) {
                this.getLog4JLogger().error("Exception caught during configuration load for {}", config.configurateWrapper().filePath());
                this.getLog4JLogger().error("Disabling plugin. Please check your config.");
                this.getLog4JLogger().error("Printing stack trace:", e);
                this.disableSelf();
                return false;
            }
        }

        this.getLog4JLogger().info("Successfully loaded configuration.");

        return true;
    }

    private void setupListeners() {
        final VoidLoopListener voidLoop = this.injector.getInstance(VoidLoopListener.class);
        final TransportationListener transportationListener = this.injector.getInstance(TransportationListener.class);

        registerListeners(
                this.injector.getInstance(AntiBuildListener.class),
                this.injector.getInstance(ChatListener.class),
                this.injector.getInstance(FishingListener.class),
                this.injector.getInstance(JoinQuitListener.class),
                this.injector.getInstance(MilkListener.class),
                this.injector.getInstance(RainMusicListener.class),
                this.injector.getInstance(PianoListener.class),
                this.injector.getInstance(ServerPingListener.class),
                this.injector.getInstance(SpawnProtectionListener.class),
                transportationListener,
                voidLoop
        );

        voidLoop.startTasks();
        transportationListener.startTasks();

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, this.injector.getInstance(ElevatorMusicTask.class), 1, 20);
    }

    private void setupCommands() {
        final @NonNull CommandService commandService = this.injector.getInstance(CommandService.class);
        commandService.init();

        final cloud.commandframework.paper.@Nullable PaperCommandManager<CommandSender> commandManager = commandService.get();
        if (commandManager == null) {
            this.getLog4JLogger().error("The CommandService was null after initialization.");
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
        this.injector.getInstance(InfoCommand.class).register(commandManager);
        this.injector.getInstance(MilkCommand.class).register(commandManager);
        this.injector.getInstance(PackCommand.class).register(commandManager);
        this.injector.getInstance(PianoCommand.class).register(commandManager);
        this.injector.getInstance(PlaytimeCommands.class).register(commandManager);
        this.injector.getInstance(RulesCommand.class).register(commandManager);
        this.injector.getInstance(WorldCommands.class).register(commandManager);
    }

}
