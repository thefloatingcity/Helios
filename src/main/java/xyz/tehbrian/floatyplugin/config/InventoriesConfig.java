package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code inventories.conf}.
 */
public final class InventoriesConfig extends AbstractRawHoconConfig {

    @Inject
    public InventoriesConfig(final @NotNull @Named("dataFolder") Path dataFolder) {
        super(dataFolder.resolve("inventories.conf"));
    }

}
