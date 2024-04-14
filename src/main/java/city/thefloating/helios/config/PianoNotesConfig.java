package city.thefloating.helios.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code piano-notes.conf}.
 */
public final class PianoNotesConfig extends AbstractRawHoconConfig {

  @Inject
  public PianoNotesConfig(final @Named("dataFolder") Path dataFolder) {
    super(dataFolder.resolve("piano-notes.conf"));
  }

}
