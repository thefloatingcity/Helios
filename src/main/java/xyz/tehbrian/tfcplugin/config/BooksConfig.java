package xyz.tehbrian.tfcplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.core.configurate.AbstractConfig;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.nio.file.Path;

/**
 * Loads and holds values for {@code books.yml}.
 */
public final class BooksConfig extends AbstractConfig<HoconConfigurateWrapper> {

    private @Nullable CommentedConfigurationNode rootNode;

    @Inject
    public BooksConfig(
            final @NotNull Logger logger,
            final @NotNull @Named("dataFolder") Path dataFolder
    ) {
        super(logger, new HoconConfigurateWrapper(logger, dataFolder.resolve("config.yml"), HoconConfigurationLoader.builder()
                .path(dataFolder.resolve("config.yml"))
                .defaultOptions(opts -> opts.implicitInitialization(false))
                .build()));
    }

    @Override
    public void load() {
        this.configurateWrapper.load();
        this.rootNode = this.configurateWrapper.get();
        final String fileName = this.configurateWrapper.filePath().getFileName().toString();

        this.logger.info("Successfully loaded configuration file {}", fileName);
    }

    /**
     * Gets the root node.
     *
     * @return the root node
     */
    public @Nullable CommentedConfigurationNode rootNode() {
        return this.rootNode;
    }

}
