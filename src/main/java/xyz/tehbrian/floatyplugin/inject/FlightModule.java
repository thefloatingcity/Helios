package xyz.tehbrian.floatyplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.floatyplugin.FlightService;

public final class FlightModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(FlightService.class).asEagerSingleton();
    }

}
