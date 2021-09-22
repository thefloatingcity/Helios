package xyz.tehbrian.tfcplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.tfcplugin.LuckPermsService;
import xyz.tehbrian.tfcplugin.config.ConfigConfig;

public final class LuckPermsModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(LuckPermsService.class).asEagerSingleton();
    }

}
