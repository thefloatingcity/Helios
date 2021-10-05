package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.core.configurate.AbstractConfig;
import dev.tehbrian.tehlib.core.configurate.DataConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import xyz.tehbrian.floatyplugin.util.ConfigDeserializers;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Loads and holds values for {@code config.conf}.
 */
public final class ConfigConfig extends AbstractConfig<HoconConfigurateWrapper> implements DataConfig<ConfigConfig.Data> {

    private @Nullable Data data;
    private ConfigConfig.@Nullable Spawn spawn;
    private ConfigConfig.@Nullable PlayerSpawn playerSpawn;

    @Inject
    public ConfigConfig(final @NotNull @Named("dataFolder") Path dataFolder) {
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

        final var spawnNode = rootNode.node("spawn");
        this.spawn = new Spawn(
                ConfigDeserializers.deserializeLocation(spawnNode.node("overworld")),
                ConfigDeserializers.deserializeLocation(spawnNode.node("nether")),
                ConfigDeserializers.deserializeLocation(spawnNode.node("end"))
        );

        final var playerSpawnNode = rootNode.node("player-spawn");
        this.playerSpawn = new PlayerSpawn(
                ConfigDeserializers.deserializeLocation(playerSpawnNode.node("overworld")),
                ConfigDeserializers.deserializeLocation(playerSpawnNode.node("nether")),
                ConfigDeserializers.deserializeLocation(playerSpawnNode.node("end"))
        );
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public @NonNull Data data() {
        return Objects.requireNonNull(this.data, "Data is null");
    }

    public ConfigConfig.@Nullable Spawn spawn() {
        return this.spawn;
    }

    public ConfigConfig.@Nullable PlayerSpawn playerSpawn() {
        return this.playerSpawn;
    }

    public @NonNull Location spawnLocation(final World.Environment environment) {
        Objects.requireNonNull(this.spawn);
        return Objects.requireNonNull(switch (environment) {
            case THE_END -> this.spawn.end();
            case NETHER -> this.spawn.nether();
            default -> this.spawn.overworld();
        });
    }

    public @NonNull Location playerSpawnLocation(final World.Environment environment) {
        Objects.requireNonNull(this.playerSpawn);
        return Objects.requireNonNull(switch (environment) {
            case THE_END -> this.playerSpawn.end();
            case NETHER -> this.playerSpawn.nether();
            default -> this.playerSpawn.overworld();
        });
    }

    @ConfigSerializable
    public static record Data(@NonNull PokeForce pokeForce,
                              @NonNull String resourcePackUrl,
                              @NonNull String resourcePackHash) {

        @ConfigSerializable
        public static record PokeForce(double minY, double maxY, double minXZ, double maxXZ) {

        }

    }

    public static record Spawn(Location overworld,
                               Location nether,
                               Location end) {

    }

    public static record PlayerSpawn(Location overworld,
                                     Location nether,
                                     Location end) {

    }

}
