package xyz.tehbrian.floatyplugin;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.nio.file.Path;

public final class PluginModule extends AbstractModule {

  private final FloatyPlugin plugin;

  public PluginModule(final FloatyPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    this.bind(FloatyPlugin.class).toInstance(this.plugin);
    this.bind(JavaPlugin.class).toInstance(this.plugin);
  }

  @Provides
  public Logger provideSLF4JLogger() {
    return this.plugin.getSLF4JLogger();
  }

  @Provides
  @Named("dataFolder")
  public Path provideDataFolder() {
    return this.plugin.getDataFolder().toPath();
  }

}
