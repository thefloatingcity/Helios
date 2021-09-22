package xyz.tehbrian.tfcplugin.config;

import dev.tehbrian.tehlib.core.configurate.ConfigurateWrapper;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;

public class HoconConfigurateWrapper extends ConfigurateWrapper<HoconConfigurationLoader> {

    /**
     * @param logger   the logger
     * @param filePath the file path for the config
     */
    public HoconConfigurateWrapper(
            @NonNull final Logger logger,
            @NonNull final Path filePath
    ) {
        super(logger, filePath, HoconConfigurationLoader.builder()
                .path(filePath)
                .build());
    }

    /**
     * @param logger   the logger
     * @param filePath the file path for the config
     * @param loader   the loader
     */
    public HoconConfigurateWrapper(
            @NonNull final Logger logger,
            @NonNull final Path filePath,
            @NonNull final HoconConfigurationLoader loader
    ) {
        super(logger, filePath, loader);
    }

}
