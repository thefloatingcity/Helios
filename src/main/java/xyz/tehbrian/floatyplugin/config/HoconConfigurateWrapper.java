package xyz.tehbrian.floatyplugin.config;

import dev.tehbrian.tehlib.core.configurate.ConfigurateWrapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;

public class HoconConfigurateWrapper extends ConfigurateWrapper<HoconConfigurationLoader> {

    /**
     * @param filePath the file path for the config
     */
    public HoconConfigurateWrapper(@NonNull final Path filePath) {
        super(filePath, HoconConfigurationLoader.builder()
                .path(filePath)
                .build());
    }

    /**
     * @param filePath the file path for the config
     * @param loader   the loader
     */
    public HoconConfigurateWrapper(@NonNull final Path filePath, @NonNull final HoconConfigurationLoader loader) {
        super(filePath, loader);
    }

}
