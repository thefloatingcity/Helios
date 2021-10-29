package xyz.tehbrian.floatyplugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.tehbrian.tehlib.core.configurate.Config;
import dev.tehbrian.tehlib.paper.TehPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;
import xyz.tehbrian.floatyplugin.command.ActCommands;
import xyz.tehbrian.floatyplugin.command.BroadcastCommand;
import xyz.tehbrian.floatyplugin.command.CommandService;
import xyz.tehbrian.floatyplugin.command.DiscordCommand;
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
import xyz.tehbrian.floatyplugin.command.TagCommand;
import xyz.tehbrian.floatyplugin.command.WorldCommands;
import xyz.tehbrian.floatyplugin.config.BooksConfig;
import xyz.tehbrian.floatyplugin.config.ConfigConfig;
import xyz.tehbrian.floatyplugin.config.EmotesConfig;
import xyz.tehbrian.floatyplugin.config.InventoriesConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.inject.ConfigModule;
import xyz.tehbrian.floatyplugin.inject.PluginModule;
import xyz.tehbrian.floatyplugin.inject.ServiceModule;
import xyz.tehbrian.floatyplugin.listeners.ChatListener;
import xyz.tehbrian.floatyplugin.listeners.FishingListener;
import xyz.tehbrian.floatyplugin.listeners.JoinQuitListener;
import xyz.tehbrian.floatyplugin.listeners.MilkListener;
import xyz.tehbrian.floatyplugin.listeners.ServerPingListener;
import xyz.tehbrian.floatyplugin.listeners.build.AntiBuildListener;
import xyz.tehbrian.floatyplugin.listeners.build.SpawnProtectionListener;
import xyz.tehbrian.floatyplugin.music.ElevatorMusicTask;
import xyz.tehbrian.floatyplugin.music.RainMusicListener;
import xyz.tehbrian.floatyplugin.piano.PianoListener;
import xyz.tehbrian.floatyplugin.tag.TagListener;
import xyz.tehbrian.floatyplugin.transportation.PortalTask;
import xyz.tehbrian.floatyplugin.transportation.TransportationListener;
import xyz.tehbrian.floatyplugin.transportation.TransportationTask;
import xyz.tehbrian.floatyplugin.transportation.VoidLoopListener;
import xyz.tehbrian.floatyplugin.transportation.VoidLoopTask;
import xyz.tehbrian.floatyplugin.world.WorldService;

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
                    new PluginModule(this),
                    new ServiceModule()
            );
        } catch (final Exception e) {
            this.getLog4JLogger().error("Something went wrong while creating the Guice injector.");
            this.getLog4JLogger().error("Disabling plugin.");
            this.disableSelf();
            this.getLog4JLogger().error("Printing stack trace, please send this to the developers:", e);
            return;
        }

        if (!this.injector.getInstance(LuckPermsService.class).load()) {
            this.getLogger().severe("LuckPerms dependency not found. Disabling plugin.");
            this.disableSelf();
        }

        if (!this.loadConfiguration()) {
            this.disableSelf();
            return;
        }
        this.setupListeners();
        this.setupCommands();
        this.setupTasks();

        this.getServer().getScheduler().runTaskLater(this, () -> this.injector.getInstance(WorldService.class).init(), 10);
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    /**
     * Loads the plugin's configuration. If an exception is caught, logs the
     * error and returns false.
     *
     * @return whether or not the loading was successful
     */
    public boolean loadConfiguration() {
        this.saveResourceSilently("books.conf");
        this.saveResourceSilently("config.conf");
        this.saveResourceSilently("emotes.conf");
        this.saveResourceSilently("inventories.conf");
        this.saveResourceSilently("lang.conf");

        final List<Config> configsToLoad = List.of(
                this.injector.getInstance(BooksConfig.class),
                this.injector.getInstance(ConfigConfig.class),
                this.injector.getInstance(EmotesConfig.class),
                this.injector.getInstance(InventoriesConfig.class),
                this.injector.getInstance(LangConfig.class)
        );

        for (final Config config : configsToLoad) {
            try {
                config.load();
            } catch (final ConfigurateException e) {
                this.getLog4JLogger().error("Exception caught during config load for {}", config.configurateWrapper().filePath());
                this.getLog4JLogger().error("Please check your config.");
                this.getLog4JLogger().error("Printing stack trace:", e);
                return false;
            }
        }

        this.getLog4JLogger().info("Successfully loaded configuration.");
        return true;
    }

    private void setupListeners() {
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
                this.injector.getInstance(TransportationListener.class),
                this.injector.getInstance(VoidLoopListener.class),
                this.injector.getInstance(TagListener.class)
        );
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
        this.injector.getInstance(DiscordCommand.class).register(commandManager);
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
        this.injector.getInstance(TagCommand.class).register(commandManager);
        this.injector.getInstance(WorldCommands.class).register(commandManager);
    }

    private void setupTasks() {
        this.injector.getInstance(ElevatorMusicTask.class).start();
        this.injector.getInstance(TransportationTask.class).start();
        this.injector.getInstance(VoidLoopTask.class).start();
        this.injector.getInstance(PortalTask.class).start();
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(
            @NotNull final String worldName,
            @Nullable final String id
    ) {
        return this.injector.getInstance(WorldService.class).getDefaultWorldGenerator(worldName, id);
    }

}
