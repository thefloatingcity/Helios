package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code inventories.conf}.
 */
public final class InventoriesConfig extends AbstractRawHoconConfig {

    @Inject
    public InventoriesConfig(
            final @NotNull Logger logger,
            final @NotNull @Named("dataFolder") Path dataFolder
    ) {
        super(logger, dataFolder.resolve("inventories.conf"));
    }

}
