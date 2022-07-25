package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code emotes.conf}.
 */
public final class EmotesConfig extends AbstractRawHoconConfig {

  @Inject
  public EmotesConfig(final @NonNull @Named("dataFolder") Path dataFolder) {
    super(dataFolder.resolve("emotes.conf"));
  }

}
