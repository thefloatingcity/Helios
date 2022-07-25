package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.paper.configurate.AbstractLangConfig;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code lang.conf}.
 */
public class LangConfig extends AbstractLangConfig<HoconConfigurateWrapper> {

  /**
   * @param dataFolder the data folder
   */
  @Inject
  public LangConfig(final @NonNull @Named("dataFolder") Path dataFolder) {
    super(new HoconConfigurateWrapper(dataFolder.resolve("lang.conf")));
  }

}
