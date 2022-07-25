package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.configurate.AbstractDataConfig;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code config.conf}.
 */
public final class ConfigConfig extends AbstractDataConfig<HoconConfigurateWrapper, ConfigConfig.Data> {

  @Inject
  public ConfigConfig(final @NonNull @Named("dataFolder") Path dataFolder) {
    super(new HoconConfigurateWrapper(dataFolder.resolve("config.conf"), HoconConfigurationLoader.builder()
        .path(dataFolder.resolve("config.conf"))
        .defaultOptions(opts -> opts.implicitInitialization(false))
        .build()));
  }

  @Override
  protected Class<Data> getDataClass() {
    return Data.class;
  }

  @ConfigSerializable
  public record Data(@NonNull PokeForce pokeForce,
                     @NonNull String resourcePackUrl,
                     @NonNull String resourcePackHash) {

    @ConfigSerializable
    public record PokeForce(double minY, double maxY, double minXZ, double maxXZ) {

    }

  }

}
