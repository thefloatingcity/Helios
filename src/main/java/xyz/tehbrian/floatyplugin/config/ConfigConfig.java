package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.core.configurate.AbstractConfig;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.serialize.SerializationException;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.util.ConfigDeserializers;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Loads and holds values for {@code config.conf}.
 */
public final class ConfigConfig extends AbstractConfig<HoconConfigurateWrapper> {

    private final FloatyPlugin floatyPlugin;

    private @Nullable Data data;
    private ConfigConfig.@Nullable Spawn spawn;
    private ConfigConfig.@Nullable PlayerSpawn playerSpawn;

    @Inject
    public ConfigConfig(
            final @NotNull Logger logger,
            final @NotNull @Named("dataFolder") Path dataFolder,
            final @NotNull FloatyPlugin floatyPlugin
    ) {
        super(logger, new HoconConfigurateWrapper(logger, dataFolder.resolve("config.conf"), HoconConfigurationLoader.builder()
                .path(dataFolder.resolve("config.conf"))
                .defaultOptions(opts -> opts.implicitInitialization(false))
                .build()));
        this.floatyPlugin = floatyPlugin;
    }

    @Override
    public void load() {
        this.configurateWrapper.load();
        final CommentedConfigurationNode rootNode = this.configurateWrapper.get();
        final String fileName = this.configurateWrapper.filePath().getFileName().toString();

        try {
            this.data = rootNode.get(Data.class);
        } catch (final SerializationException e) {
            this.logger.warn("Exception caught during configuration deserialization for {}", fileName);
            this.logger.warn("Disabling plugin. Please check your {}", fileName);
            this.floatyPlugin.disableSelf();
            this.logger.warn("Printing stack trace:", e);
            return;
        }

        if (this.data == null) {
            this.logger.warn("The deserialized configuration for {} was null.", fileName);
            this.logger.warn("Disabling plugin. Please check your {}", fileName);
            this.floatyPlugin.disableSelf();
            return;
        }

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

        this.logger.info("Successfully loaded configuration file {}", fileName);
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public @Nullable Data data() {
        return this.data;
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
    public static record Data(@NonNull PokeForce pokeForce) {

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
