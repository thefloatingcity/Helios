package xyz.tehbrian.tfcplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.core.configurate.AbstractConfig;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.serialize.SerializationException;
import xyz.tehbrian.tfcplugin.FloatyPlugin;
import xyz.tehbrian.tfcplugin.util.ConfigDeserializers;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code config.conf}.
 */
public final class ConfigConfig extends AbstractConfig<HoconConfigurateWrapper> {

    private final FloatyPlugin floatyPlugin;

    private @Nullable Data data;
    private @Nullable Spawn spawn;

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

    public @Nullable Spawn spawn() {
        return this.spawn;
    }

    @ConfigSerializable
    public static record Data(@NonNull PokeForce pokeForce) {

        @ConfigSerializable
        public static record PokeForce(int minY, int maxY, int minXZ, int maxXZ) {

        }

    }

    public static record Spawn(Location overworld,
                               Location nether,
                               Location end) {

    }

}
