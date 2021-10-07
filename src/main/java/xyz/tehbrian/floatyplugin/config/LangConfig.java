package xyz.tehbrian.floatyplugin.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.tehbrian.tehlib.paper.configurate.AbstractLangConfig;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;

public class LangConfig extends AbstractLangConfig<HoconConfigurateWrapper> {

    /**
     * @param dataFolder the data folder
     * @param logger     the logger
     */
    @Inject
    public LangConfig(final @NonNull @Named("dataFolder") Path dataFolder, final @NonNull Logger logger) {
        super(new HoconConfigurateWrapper(dataFolder.resolve("lang.conf")), logger);
    }

}
