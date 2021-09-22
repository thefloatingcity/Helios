package xyz.tehbrian.tfcplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.tfcplugin.config.BooksConfig;
import xyz.tehbrian.tfcplugin.config.ConfigConfig;
import xyz.tehbrian.tfcplugin.config.EmotesConfig;
import xyz.tehbrian.tfcplugin.config.InventoriesConfig;
import xyz.tehbrian.tfcplugin.config.LangConfig;

/**
 * Guice module which provides the various configs.
 */
public final class ConfigModule extends AbstractModule {

    /**
     * Binds the configs as eager singletons.
     */
    @Override
    protected void configure() {
        this.bind(BooksConfig.class).asEagerSingleton();
        this.bind(ConfigConfig.class).asEagerSingleton();
        this.bind(EmotesConfig.class).asEagerSingleton();
        this.bind(InventoriesConfig.class).asEagerSingleton();
        this.bind(LangConfig.class).asEagerSingleton();
    }

}
