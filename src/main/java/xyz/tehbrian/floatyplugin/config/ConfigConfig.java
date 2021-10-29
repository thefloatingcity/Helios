package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.core.configurate.AbstractConfig;
import dev.tehbrian.tehlib.core.configurate.DataConfig;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Loads and holds values for {@code config.conf}.
 */
public final class ConfigConfig extends AbstractConfig<HoconConfigurateWrapper> implements DataConfig<ConfigConfig.Data> {

    private @Nullable Data data;

    @Inject
    public ConfigConfig(final @NonNull @Named("dataFolder") Path dataFolder) {
        super(new HoconConfigurateWrapper(dataFolder.resolve("config.conf"), HoconConfigurationLoader.builder()
                .path(dataFolder.resolve("config.conf"))
                .defaultOptions(opts -> opts.implicitInitialization(false))
                .build()));
    }

    @Override
    public void load() throws ConfigurateException {
        this.configurateWrapper.load();
        final @NonNull CommentedConfigurationNode rootNode = Objects.requireNonNull(this.configurateWrapper.get());
        this.data = Objects.requireNonNull(rootNode.get(Data.class), "Deserialized data is null");
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public @NonNull Data data() {
        return Objects.requireNonNull(this.data, "Data is null");
    }

    @ConfigSerializable
    public static record Data(@NonNull PokeForce pokeForce,
                              @NonNull String resourcePackUrl,
                              @NonNull String resourcePackHash) {

        @ConfigSerializable
        public static record PokeForce(double minY, double maxY, double minXZ, double maxXZ) {

        }

    }

}
