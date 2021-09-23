package xyz.tehbrian.floatyplugin.config;

import dev.tehbrian.tehlib.core.configurate.AbstractConfig;
import dev.tehbrian.tehlib.core.configurate.ConfigurateWrapper;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;

public abstract class AbstractRawConfig<W extends ConfigurateWrapper<?>> extends AbstractConfig<W> {

    private @Nullable CommentedConfigurationNode rootNode;

    /**
     * @param logger             the logger
     * @param configurateWrapper the wrapper
     */
    public AbstractRawConfig(@NonNull final Logger logger, @NonNull final W configurateWrapper) {
        super(logger, configurateWrapper);
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
