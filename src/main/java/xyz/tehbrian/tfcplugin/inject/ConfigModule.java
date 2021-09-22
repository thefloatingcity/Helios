package xyz.tehbrian.tfcplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.tfcplugin.config.ConfigConfig;
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
        this.bind(ConfigConfig.class).asEagerSingleton();
        this.bind(LangConfig.class).asEagerSingleton();
    }

}
