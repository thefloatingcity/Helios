package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code books.conf}.
 */
public final class BooksConfig extends AbstractRawHoconConfig {

  @Inject
  public BooksConfig(final @NonNull @Named("dataFolder") Path dataFolder) {
    super(dataFolder.resolve("books.conf"));
  }

}
