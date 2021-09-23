package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code emotes.conf}.
 */
public final class EmotesConfig extends AbstractRawHoconConfig {

    @Inject
    public EmotesConfig(
            final @NotNull Logger logger,
            final @NotNull @Named("dataFolder") Path dataFolder
    ) {
        super(logger, dataFolder.resolve("emotes.conf"));
    }

}
