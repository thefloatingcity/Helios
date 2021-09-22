package xyz.tehbrian.tfcplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.tfcplugin.FlightService;
import xyz.tehbrian.tfcplugin.config.ConfigConfig;
import xyz.tehbrian.tfcplugin.config.LangConfig;

public final class FlightModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(FlightService.class).asEagerSingleton();
    }

}
