package xyz.tehbrian.tfcplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.paper.configurate.AbstractLangConfig;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class LangConfig extends AbstractLangConfig<HoconConfigurateWrapper> {

    /**
     * @param logger     the logger
     * @param dataFolder the data folder
     */
    @Inject
    public LangConfig(
            final @NotNull Logger logger,
            final @NotNull @Named("dataFolder") Path dataFolder
    ) {
        super(logger, new HoconConfigurateWrapper(logger, dataFolder.resolve("lang.conf")));
    }

}
