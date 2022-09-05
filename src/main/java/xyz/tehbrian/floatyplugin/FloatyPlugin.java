package xyz.tehbrian.floatyplugin;

import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.tehbrian.tehlib.configurate.Config;
import dev.tehbrian.tehlib.paper.TehPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurateException;
import xyz.tehbrian.floatyplugin.backrooms.BackroomsAmbianceTask;
import xyz.tehbrian.floatyplugin.backrooms.BackroomsBlockListener;
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
import xyz.tehbrian.floatyplugin.command.VoteCommand;
import xyz.tehbrian.floatyplugin.command.WorldCommands;
import xyz.tehbrian.floatyplugin.config.BooksConfig;
import xyz.tehbrian.floatyplugin.config.ConfigConfig;
import xyz.tehbrian.floatyplugin.config.EmotesConfig;
import xyz.tehbrian.floatyplugin.config.InventoriesConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.inject.PluginModule;
import xyz.tehbrian.floatyplugin.inject.SingletonModule;
import xyz.tehbrian.floatyplugin.listener.ChatListener;
import xyz.tehbrian.floatyplugin.listener.FishingListener;
import xyz.tehbrian.floatyplugin.listener.JoinQuitListener;
import xyz.tehbrian.floatyplugin.listener.MilkListener;
import xyz.tehbrian.floatyplugin.listener.ServerPingListener;
import xyz.tehbrian.floatyplugin.listener.build.AntiBuildListener;
import xyz.tehbrian.floatyplugin.listener.build.SpawnProtectionListener;
import xyz.tehbrian.floatyplugin.music.ElevatorMusicTask;
import xyz.tehbrian.floatyplugin.music.RainMusicListener;
import xyz.tehbrian.floatyplugin.piano.PianoListener;
import xyz.tehbrian.floatyplugin.tag.TagListener;
import xyz.tehbrian.floatyplugin.transportation.PortalTask;
import xyz.tehbrian.floatyplugin.transportation.TransportationListener;
import xyz.tehbrian.floatyplugin.transportation.TransportationTask;
import xyz.tehbrian.floatyplugin.voidloop.VoidLoopListener;
import xyz.tehbrian.floatyplugin.voidloop.VoidLoopTask;
import xyz.tehbrian.floatyplugin.world.WorldService;

import java.util.List;

public final class FloatyPlugin extends TehPlugin {

  private @Nullable Injector injector;
  private @Nullable Logger logger;

  @Override
  public void onEnable() {
    try {
      this.injector = Guice.createInjector(
          new PluginModule(this),
          new SingletonModule()
      );
    } catch (final Exception e) {
      this.getSLF4JLogger().error("Something went wrong while creating the Guice injector.");
      this.getSLF4JLogger().error("Printing stack trace, please send this to the developers:", e);
      this.disableSelf();
      return;
    }

    this.logger = this.injector.getInstance(Logger.class);

    if (!this.injector.getInstance(LuckPermsService.class).load()) {
      this.logger.error("LuckPerms dependency not found. Disabling plugin.");
      this.disableSelf();
      return;
    }

    if (!this.loadConfiguration()) {
      this.disableSelf();
      return;
    }

    if (!this.setupCommands()) {
      this.disableSelf();
      return;
    }

    this.setupListeners();
    this.setupTasks();

    this.getServer().getScheduler().runTaskLater(this, () -> this.injector.getInstance(WorldService.class).init(), 10);
  }

  @Override
  public void onDisable() {
    this.getServer().getScheduler().cancelTasks(this);
  }

  /**
   * @return whether it was successful
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
        this.logger.error("Exception caught during config load for {}", config.configurateWrapper().filePath());
        this.logger.error("Please check your config.");
        this.logger.error("Printing stack trace:", e);
        return false;
      }
    }

    this.logger.info("Successfully loaded configuration.");
    return true;
  }

  private void setupListeners() {
    registerListeners(
        this.injector.getInstance(BackroomsBlockListener.class),
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

  /**
   * @return whether it was successful
   */
  private boolean setupCommands() {
    final CommandService commandService = this.injector.getInstance(CommandService.class);
    try {
      commandService.init();
    } catch (final Exception e) {
      this.getSLF4JLogger().error("Failed to create the CommandManager.");
      this.getSLF4JLogger().error("Printing stack trace, please send this to the developers:", e);
      return false;
    }

    final @Nullable PaperCommandManager<CommandSender> commandManager = commandService.get();
    if (commandManager == null) {
      this.getSLF4JLogger().error("The CommandService was null after initialization!");
      return false;
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
    this.injector.getInstance(VoteCommand.class).register(commandManager);
    this.injector.getInstance(WorldCommands.class).register(commandManager);

    return true;
  }

  private void setupTasks() {
    this.injector.getInstance(ElevatorMusicTask.class).start();
    this.injector.getInstance(TransportationTask.class).start();
    this.injector.getInstance(VoidLoopTask.class).start();
    this.injector.getInstance(PortalTask.class).start();
    this.injector.getInstance(BackroomsAmbianceTask.class).start();
  }

  @Override
  public ChunkGenerator getDefaultWorldGenerator(
      @NotNull final String worldName,
      @Nullable final String id
  ) {
    return this.injector.getInstance(WorldService.class).getDefaultWorldGenerator(worldName, id);
  }

}
