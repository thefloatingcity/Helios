package xyz.tehbrian.floatyplugin.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import xyz.tehbrian.floatyplugin.FloatyPlugin;

import java.nio.file.Path;

public final class PluginModule extends AbstractModule {

  private final FloatyPlugin floatyPlugin;

  public PluginModule(final FloatyPlugin floatyPlugin) {
    this.floatyPlugin = floatyPlugin;
  }

  @Override
  protected void configure() {
    this.bind(FloatyPlugin.class).toInstance(this.floatyPlugin);
    this.bind(JavaPlugin.class).toInstance(this.floatyPlugin);
  }

  @Provides
  public Logger provideSLF4JLogger() {
    return this.floatyPlugin.getSLF4JLogger();
  }

  @Provides
  @Named("dataFolder")
  public Path provideDataFolder() {
    return this.floatyPlugin.getDataFolder().toPath();
  }

}
