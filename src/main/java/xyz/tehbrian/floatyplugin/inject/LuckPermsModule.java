package xyz.tehbrian.floatyplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.floatyplugin.LuckPermsService;

public final class LuckPermsModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(LuckPermsService.class).asEagerSingleton();
    }

}
