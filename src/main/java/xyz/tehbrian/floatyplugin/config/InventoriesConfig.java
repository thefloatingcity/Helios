package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code inventories.conf}.
 */
public final class InventoriesConfig extends AbstractRawHoconConfig {

  @Inject
  public InventoriesConfig(final @Named("dataFolder") Path dataFolder) {
    super(dataFolder.resolve("inventories.conf"));
  }

}
