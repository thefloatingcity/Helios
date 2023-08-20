package city.thefloating.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code emotes.conf}.
 */
public final class EmotesConfig extends AbstractRawHoconConfig {

  @Inject
  public EmotesConfig(final @Named("dataFolder") Path dataFolder) {
    super(dataFolder.resolve("emotes.conf"));
  }

}
