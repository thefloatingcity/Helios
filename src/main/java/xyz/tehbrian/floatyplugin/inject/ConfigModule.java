package xyz.tehbrian.floatyplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.floatyplugin.config.BooksConfig;
import xyz.tehbrian.floatyplugin.config.ConfigConfig;
import xyz.tehbrian.floatyplugin.config.EmotesConfig;
import xyz.tehbrian.floatyplugin.config.InventoriesConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;

public final class ConfigModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(BooksConfig.class).asEagerSingleton();
        this.bind(ConfigConfig.class).asEagerSingleton();
        this.bind(EmotesConfig.class).asEagerSingleton();
        this.bind(InventoriesConfig.class).asEagerSingleton();
        this.bind(LangConfig.class).asEagerSingleton();
    }

}
