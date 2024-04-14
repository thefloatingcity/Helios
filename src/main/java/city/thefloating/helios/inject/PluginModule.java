package city.thefloating.helios.inject;

import city.thefloating.helios.Helios;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import java.nio.file.Path;

public final class PluginModule extends AbstractModule {

  private final Helios plugin;

  public PluginModule(final Helios plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    this.bind(Helios.class).toInstance(this.plugin);
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
