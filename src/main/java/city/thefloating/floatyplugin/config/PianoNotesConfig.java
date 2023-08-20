package city.thefloating.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code piano_notes.conf}.
 */
public final class PianoNotesConfig extends AbstractRawHoconConfig {

  @Inject
  public PianoNotesConfig(final @Named("dataFolder") Path dataFolder) {
    super(dataFolder.resolve("piano_notes.conf"));
  }

}
