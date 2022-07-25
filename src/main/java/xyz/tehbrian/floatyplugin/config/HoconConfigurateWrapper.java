package xyz.tehbrian.floatyplugin.config;

import dev.tehbrian.tehlib.configurate.ConfigurateWrapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;

public class HoconConfigurateWrapper extends ConfigurateWrapper<HoconConfigurationLoader> {

    /**
     * @param filePath the file path for the config
     */
    public HoconConfigurateWrapper(final @NonNull Path filePath) {
        super(filePath, HoconConfigurationLoader.builder()
                .path(filePath)
                .build());
    }

    /**
     * @param filePath the file path for the config
     * @param loader   the loader
     */
    public HoconConfigurateWrapper(final @NonNull Path filePath, final @NonNull HoconConfigurationLoader loader) {
        super(filePath, loader);
    }

}
