package city.thefloating.helios;

import city.thefloating.helios.ascension.AscendCommand;
import city.thefloating.helios.ascension.PlaytimeCommand;
import city.thefloating.helios.backrooms.RandomSpooks;
import city.thefloating.helios.backrooms.SpaceBreakListener;
import city.thefloating.helios.config.BooksConfig;
import city.thefloating.helios.config.ConfigConfig;
import city.thefloating.helios.config.EmotesConfig;
import city.thefloating.helios.config.LangConfig;
import city.thefloating.helios.config.PianoNotesConfig;
import city.thefloating.helios.fun.ActCommands;
import city.thefloating.helios.fun.ElevatorMusicJockey;
import city.thefloating.helios.fun.FishingListener;
import city.thefloating.helios.fun.FlingerListener;
import city.thefloating.helios.fun.FunCommands;
import city.thefloating.helios.fun.HatCommand;
import city.thefloating.helios.fun.PackCommand;
import city.thefloating.helios.fun.RainMusicListener;
import city.thefloating.helios.inject.PluginModule;
import city.thefloating.helios.inject.SingletonModule;
import city.thefloating.helios.loop.PlayerVoidLoopTask;
import city.thefloating.helios.loop.VoidDamageListener;
import city.thefloating.helios.loop.WarpTask;
import city.thefloating.helios.milk.MilkCommand;
import city.thefloating.helios.milk.MilkListener;
import city.thefloating.helios.nextbot.Nate;
import city.thefloating.helios.nextbot.NextbotCommand;
import city.thefloating.helios.piano.PianoCommand;
import city.thefloating.helios.piano.PianoPlayListener;
import city.thefloating.helios.realm.MadlandsMoverListener;
import city.thefloating.helios.realm.PlayerSpawnListener;
import city.thefloating.helios.realm.TransposeCommands;
import city.thefloating.helios.realm.VoidGenerator;
import city.thefloating.helios.realm.WorldProtectionListener;
import city.thefloating.helios.realm.WorldService;
import city.thefloating.helios.realm.WorldSpawnProtectionListener;
import city.thefloating.helios.server.BroadcastCommand;
import city.thefloating.helios.server.ChatListener;
import city.thefloating.helios.server.DiscordCommand;
import city.thefloating.helios.server.GameModeCommands;
import city.thefloating.helios.server.HeliosCommand;
import city.thefloating.helios.server.JoinQuitListener;
import city.thefloating.helios.server.MarkdownCommand;
import city.thefloating.helios.server.RulesCommand;
import city.thefloating.helios.server.ServerPingListener;
import city.thefloating.helios.server.VoteCommand;
import city.thefloating.helios.soul.Charon;
import city.thefloating.helios.soul.Otzar;
import city.thefloating.helios.tag.TagCommand;
import city.thefloating.helios.tag.TagListener;
import city.thefloating.helios.transportation.FlightListener;
import city.thefloating.helios.transportation.FlyCommand;
import city.thefloating.helios.transportation.PortalListener;
import city.thefloating.helios.transportation.TransportationListener;
import city.thefloating.helios.transportation.TransportationTask;
import cloud.commandframework.execution.CommandExecutionCoordinator;
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
import org.spongepowered.configurate.ConfigurateException;

import java.util.List;
import java.util.function.Function;

/**
 * The main class for the Helios plugin.
 */
public final class Helios extends TehPlugin {

  private @MonotonicNonNull PaperCommandManager<CommandSender> commandManager;
  private @MonotonicNonNull Injector injector;

  @Override
  public void onEnable() {
    try {
      this.injector = Guice.createInjector(
          new PluginModule(this),
          new SingletonModule()
      );
    } catch (final Exception e) {
      this.getSLF4JLogger().error("An error occurred while creating the Guice injector.");
      this.getSLF4JLogger().error("Disabling plugin.");
      this.disableSelf();
      this.getSLF4JLogger().error("Printing stack trace. Please send this to the developers:", e);
      return;
    }

    if (!this.injector.getInstance(LuckPermsService.class).load()) {
      this.getSLF4JLogger().error("LuckPerms dependency not found. Disabling plugin.");
      this.disableSelf();
      return;
    }

    if (!this.loadConfiguration()) {
      this.disableSelf();
      return;
    }
    if (!this.initCommands()) {
      this.disableSelf();
      return;
    }
    this.initListeners();
    this.initTasks();

    // world creation must occur as a delayed init task.
    this.getServer().getScheduler().runTask(this, () -> this.injector.getInstance(WorldService.class).init());
  }

  @Override
  public void onDisable() {
    this.injector.getInstance(Nate.class).killNextbots();

    try {
      this.injector.getInstance(Charon.class).save();
    } catch (final ConfigurateException e) {
      this.getSLF4JLogger().error(
          "An error occurred while saving config file {}. Please ensure that the file is valid.",
          this.injector.getInstance(Otzar.class).configurateWrapper().filePath()
      );
      this.getSLF4JLogger().error("Printing stack trace:", e);
    }

    this.getServer().getScheduler().cancelTasks(this);
  }

  /**
   * Loads the plugin's configuration.
   * <p>
   * If there is an error while loading a config file, the exception is logged
   * and the file is skipped.
   *
   * @return whether all config files were successfully loaded
   */
  public boolean loadConfiguration() {
    this.saveResourceSilently("books.conf");
    this.saveResourceSilently("config.conf");
    this.saveResourceSilently("emotes.conf");
    this.saveResourceSilently("lang.conf");
    this.saveResourceSilently("piano-notes.conf");

    final List<Config> configsToLoad = List.of(
        this.injector.getInstance(Otzar.class),
        this.injector.getInstance(BooksConfig.class),
        this.injector.getInstance(ConfigConfig.class),
        this.injector.getInstance(EmotesConfig.class),
        this.injector.getInstance(LangConfig.class),
        this.injector.getInstance(PianoNotesConfig.class)
    );

    boolean wasSuccessful = true;
    for (final Config config : configsToLoad) {
      try {
        config.load();
      } catch (final ConfigurateException e) {
        this.getSLF4JLogger().error(
            "An error occurred while loading config file {}. Please ensure that the file is valid.",
            config.configurateWrapper().filePath()
        );
        this.getSLF4JLogger().error("Printing stack trace:", e);
        wasSuccessful = false;
      }
    }

    if (wasSuccessful) {
      this.getSLF4JLogger().info("Successfully loaded configuration.");
    }
    return wasSuccessful;
  }

  /**
   * @return whether it was successful
   */
  private boolean initCommands() {
    if (this.commandManager != null) {
      throw new IllegalStateException("The CommandManager is already instantiated.");
    }

    try {
      this.commandManager = new PaperCommandManager<>(
          this,
          CommandExecutionCoordinator.simpleCoordinator(),
          Function.identity(),
          Function.identity()
      );
    } catch (final Exception e) {
      this.getSLF4JLogger().error("Failed to create the CommandManager.");
      this.getSLF4JLogger().error("Printing stack trace. Please send this to the developers:", e);
      return false;
    }

    this.injector.getInstance(ActCommands.class).register(this.commandManager);
    this.injector.getInstance(AscendCommand.class).register(this.commandManager);
    this.injector.getInstance(BroadcastCommand.class).register(this.commandManager);
    this.injector.getInstance(DiscordCommand.class).register(this.commandManager);
    this.injector.getInstance(HeliosCommand.class).register(this.commandManager);
    this.injector.getInstance(FlyCommand.class).register(this.commandManager);
    this.injector.getInstance(FunCommands.class).register(this.commandManager);
    this.injector.getInstance(GameModeCommands.class).register(this.commandManager);
    this.injector.getInstance(HatCommand.class).register(this.commandManager);
    this.injector.getInstance(MilkCommand.class).register(this.commandManager);
    this.injector.getInstance(MarkdownCommand.class).register(this.commandManager);
    this.injector.getInstance(NextbotCommand.class).register(this.commandManager);
    this.injector.getInstance(PackCommand.class).register(this.commandManager);
    this.injector.getInstance(PianoCommand.class).register(this.commandManager);
    this.injector.getInstance(PlaytimeCommand.class).register(this.commandManager);
    this.injector.getInstance(RulesCommand.class).register(this.commandManager);
    this.injector.getInstance(TagCommand.class).register(this.commandManager);
    this.injector.getInstance(TransposeCommands.class).register(this.commandManager);
    this.injector.getInstance(VoteCommand.class).register(this.commandManager);

    return true;
  }

  private void initListeners() {
    registerListeners(
        this.injector.getInstance(ChatListener.class),
        this.injector.getInstance(FishingListener.class),
        this.injector.getInstance(FlightListener.class),
        this.injector.getInstance(FlingerListener.class),
        this.injector.getInstance(JoinQuitListener.class),
        this.injector.getInstance(MadlandsMoverListener.class),
        this.injector.getInstance(MilkListener.class),
        this.injector.getInstance(VoidDamageListener.class),
        this.injector.getInstance(Nate.class),
        this.injector.getInstance(PianoPlayListener.class),
        this.injector.getInstance(RainMusicListener.class),
        this.injector.getInstance(PlayerSpawnListener.class),
        this.injector.getInstance(ServerPingListener.class),
        this.injector.getInstance(SpaceBreakListener.class),
        this.injector.getInstance(WorldSpawnProtectionListener.class),
        this.injector.getInstance(TagListener.class),
        this.injector.getInstance(PortalListener.class),
        this.injector.getInstance(TransportationListener.class),
        this.injector.getInstance(WorldProtectionListener.class)
    );
  }

  private void initTasks() {
    this.injector.getInstance(ElevatorMusicJockey.class).start();
    this.injector.getInstance(PlayerVoidLoopTask.class).start();
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
