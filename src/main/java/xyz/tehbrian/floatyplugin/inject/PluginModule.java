package xyz.tehbrian.floatyplugin.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.FloatyPlugin;

import java.nio.file.Path;

/**
 * Guice module which provides bindings for the plugin's instances.
 */
public final class PluginModule extends AbstractModule {

    private final FloatyPlugin floatyPlugin;

    /**
     * @param floatyPlugin FloatyPlugin reference
     */
    public PluginModule(final @NonNull FloatyPlugin floatyPlugin) {
        this.floatyPlugin = floatyPlugin;
    }

    @Override
    protected void configure() {
        this.bind(FloatyPlugin.class).toInstance(this.floatyPlugin);
        this.bind(JavaPlugin.class).toInstance(this.floatyPlugin);
    }

    /**
     * Provides the plugin's Log4J logger.
     *
     * @return the plugin's Log4J logger
     */
    @Provides
    public @NonNull Logger provideLog4JLogger() {
        return this.floatyPlugin.getLog4JLogger();
    }

    /**
     * Provides the plugin's data folder.
     *
     * @return the data folder
     */
    @Provides
    @Named("dataFolder")
    public @NonNull Path provideDataFolder() {
        return this.floatyPlugin.getDataFolder().toPath();
    }

}
