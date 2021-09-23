package xyz.tehbrian.floatyplugin.config;

import dev.tehbrian.tehlib.core.configurate.AbstractRawConfig;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;

public abstract class AbstractRawHoconConfig extends AbstractRawConfig<HoconConfigurateWrapper> {

    /**
     * @param logger the logger
     * @param file   the config file
     */
    public AbstractRawHoconConfig(final @NonNull Logger logger, final @NonNull Path file) {
        super(logger, new HoconConfigurateWrapper(logger, file, HoconConfigurationLoader.builder()
                .path(file)
                .defaultOptions(opts -> opts.implicitInitialization(false))
                .build()));
    }

}
