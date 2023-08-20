package city.thefloating.floatyplugin;

import city.thefloating.floatyplugin.piano.PianoPlayListener;
import city.thefloating.floatyplugin.realm.MadlandsMoverListener;
import city.thefloating.floatyplugin.realm.TransposeCommands;
import city.thefloating.floatyplugin.realm.WorldProtectionListener;
import city.thefloating.floatyplugin.realm.WorldService;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.tehbrian.tehlib.configurate.Config;
import dev.tehbrian.tehlib.paper.TehPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurateException;
import city.thefloating.floatyplugin.ascension.AscendCommand;
import city.thefloating.floatyplugin.ascension.PlaytimeCommand;
import city.thefloating.floatyplugin.backrooms.RandomSpooks;
import city.thefloating.floatyplugin.backrooms.SpaceBreakListener;
import city.thefloating.floatyplugin.config.BooksConfig;
import city.thefloating.floatyplugin.config.ConfigConfig;
import city.thefloating.floatyplugin.config.EmotesConfig;
import city.thefloating.floatyplugin.config.LangConfig;
import city.thefloating.floatyplugin.config.PianoNotesConfig;
import city.thefloating.floatyplugin.fun.ActCommands;
import city.thefloating.floatyplugin.fun.ElevatorMusicTask;
import city.thefloating.floatyplugin.fun.FishingListener;
import city.thefloating.floatyplugin.fun.FunCommands;
import city.thefloating.floatyplugin.fun.HatCommand;
import city.thefloating.floatyplugin.fun.PackCommand;
import city.thefloating.floatyplugin.fun.RainMusicListener;
import city.thefloating.floatyplugin.inject.PluginModule;
import city.thefloating.floatyplugin.inject.SingletonModule;
import city.thefloating.floatyplugin.milk.MilkCommand;
import city.thefloating.floatyplugin.milk.MilkListener;
import city.thefloating.floatyplugin.piano.PianoCommand;
import city.thefloating.floatyplugin.realm.FirstJoinListener;
import city.thefloating.floatyplugin.realm.RespawnListener;
import city.thefloating.floatyplugin.realm.SpawnProtectionListener;
import city.thefloating.floatyplugin.realm.VoidGenerator;
import city.thefloating.floatyplugin.server.ChatListener;
import city.thefloating.floatyplugin.server.DiscordCommand;
import city.thefloating.floatyplugin.server.JoinQuitDisplayListener;
import city.thefloating.floatyplugin.server.RulesCommand;
import city.thefloating.floatyplugin.server.ServerPingListener;
import city.thefloating.floatyplugin.server.VoteCommand;
import city.thefloating.floatyplugin.staff.BroadcastCommand;
import city.thefloating.floatyplugin.staff.FloatyPluginCommand;
import city.thefloating.floatyplugin.tag.TagCommand;
import city.thefloating.floatyplugin.tag.TagListener;
import city.thefloating.floatyplugin.transportation.FlightListener;
import city.thefloating.floatyplugin.transportation.FlyCommand;
import city.thefloating.floatyplugin.transportation.PortalUseTask;
import city.thefloating.floatyplugin.transportation.TransportationListener;
import city.thefloating.floatyplugin.transportation.TransportationTask;
import city.thefloating.floatyplugin.void_loop.DamageListener;
import city.thefloating.floatyplugin.void_loop.MobVoidLoopListener;
import city.thefloating.floatyplugin.void_loop.PlayerVoidLoopTask;
import city.thefloating.floatyplugin.void_loop.WarpTask;

import java.util.List;

public final class FloatyPlugin extends TehPlugin {

  private @MonotonicNonNull Injector injector;
  private @MonotonicNonNull Logger logger;

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

    // world creation must occur as a delayed init task.
    this.getServer().getScheduler().runTask(this, () -> this.injector.getInstance(WorldService.class).init());
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
    this.saveResourceSilently("lang.conf");
    this.saveResourceSilently("piano_notes.conf");

    final List<Config> configsToLoad = List.of(
        this.injector.getInstance(BooksConfig.class),
        this.injector.getInstance(ConfigConfig.class),
        this.injector.getInstance(EmotesConfig.class),
        this.injector.getInstance(LangConfig.class),
        this.injector.getInstance(PianoNotesConfig.class)
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
        this.injector.getInstance(WorldProtectionListener.class),
        this.injector.getInstance(ChatListener.class),
        this.injector.getInstance(DamageListener.class),
        this.injector.getInstance(FirstJoinListener.class),
        this.injector.getInstance(FishingListener.class),
        this.injector.getInstance(FlightListener.class),
        this.injector.getInstance(JoinQuitDisplayListener.class),
        this.injector.getInstance(MadlandsMoverListener.class),
        this.injector.getInstance(MilkListener.class),
        this.injector.getInstance(MobVoidLoopListener.class),
        this.injector.getInstance(PianoPlayListener.class),
        this.injector.getInstance(RainMusicListener.class),
        this.injector.getInstance(RespawnListener.class),
        this.injector.getInstance(ServerPingListener.class),
        this.injector.getInstance(SpaceBreakListener.class),
        this.injector.getInstance(SpawnProtectionListener.class),
        this.injector.getInstance(TagListener.class),
        this.injector.getInstance(TransportationListener.class)
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
    this.injector.getInstance(AscendCommand.class).register(commandManager);
    this.injector.getInstance(BroadcastCommand.class).register(commandManager);
    this.injector.getInstance(DiscordCommand.class).register(commandManager);
    this.injector.getInstance(FloatyPluginCommand.class).register(commandManager);
    this.injector.getInstance(FlyCommand.class).register(commandManager);
    this.injector.getInstance(FunCommands.class).register(commandManager);
    this.injector.getInstance(GameModeCommands.class).register(commandManager);
    this.injector.getInstance(HatCommand.class).register(commandManager);
    this.injector.getInstance(MilkCommand.class).register(commandManager);
    this.injector.getInstance(PackCommand.class).register(commandManager);
    this.injector.getInstance(PianoCommand.class).register(commandManager);
    this.injector.getInstance(PlaytimeCommand.class).register(commandManager);
    this.injector.getInstance(RulesCommand.class).register(commandManager);
    this.injector.getInstance(TagCommand.class).register(commandManager);
    this.injector.getInstance(TransposeCommands.class).register(commandManager);
    this.injector.getInstance(VoteCommand.class).register(commandManager);

    return true;
  }

  private void setupTasks() {
    this.injector.getInstance(ElevatorMusicTask.class).start();
    this.injector.getInstance(PlayerVoidLoopTask.class).start();
    this.injector.getInstance(PortalUseTask.class).start();
    this.injector.getInstance(RandomSpooks.class).start();
    this.injector.getInstance(TransportationTask.class).start();
    this.injector.getInstance(WarpTask.class).start();
  }

  @Override
  public ChunkGenerator getDefaultWorldGenerator(
      final @NotNull String worldName,
      final @Nullable String id
  ) {
    return new VoidGenerator();
  }

}
