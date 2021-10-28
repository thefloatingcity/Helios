package xyz.tehbrian.floatyplugin.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.FloatyPlugin;

import java.nio.file.Path;

public final class PluginModule extends AbstractModule {

    private final FloatyPlugin floatyPlugin;

    public PluginModule(final @NonNull FloatyPlugin floatyPlugin) {
        this.floatyPlugin = floatyPlugin;
    }

    @Override
    protected void configure() {
        this.bind(FloatyPlugin.class).toInstance(this.floatyPlugin);
        this.bind(JavaPlugin.class).toInstance(this.floatyPlugin);
    }

    @Provides
    public @NonNull Logger provideLog4JLogger() {
        return this.floatyPlugin.getLog4JLogger();
    }

    @Provides
    @Named("dataFolder")
    public @NonNull Path provideDataFolder() {
        return this.floatyPlugin.getDataFolder().toPath();
    }

}
