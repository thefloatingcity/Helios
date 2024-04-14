package city.thefloating.helios.soul;

import city.thefloating.helios.config.HoconConfigurateWrapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.configurate.AbstractDataConfig;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Stores persistent data for souls via a YAML config.
 * <p>
 * <a href="https://en.wikipedia.org/wiki/Guf">If you're curious about the name.</a>
 */
public final class Otzar extends AbstractDataConfig<HoconConfigurateWrapper, Otzar.Data> {

  @Inject
  public Otzar(final @Named("dataFolder") Path dataFolder) {
    super(new HoconConfigurateWrapper(dataFolder.resolve("otzar.conf"), HoconConfigurationLoader.builder()
        .path(dataFolder.resolve("otzar.conf"))
        .defaultOptions(opts -> opts.implicitInitialization(true))
        .build()));
  }

  public void save() throws ConfigurateException {
    final CommentedConfigurationNode rootNode = Objects.requireNonNull(this.configurateWrapper.get());
    rootNode.set(this.getDataClass(), this.data);
    this.configurateWrapper.save();
  }

  public Map<UUID, Data.Spirit> spirits() {
    return this.data().spirits();
  }

  @Override
  protected Class<Otzar.Data> getDataClass() {
    return Otzar.Data.class;
  }

  @ConfigSerializable
  public record Data(Map<UUID, Spirit> spirits) {

    @ConfigSerializable
    public record Spirit(int netherInfractions) {

    }

  }

}
