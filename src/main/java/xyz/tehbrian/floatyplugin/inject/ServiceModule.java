package xyz.tehbrian.floatyplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.floatyplugin.LuckPermsService;
import xyz.tehbrian.floatyplugin.tag.TagService;
import xyz.tehbrian.floatyplugin.transportation.FlightService;
import xyz.tehbrian.floatyplugin.user.UserService;
import xyz.tehbrian.floatyplugin.world.WorldService;

public final class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(FlightService.class).asEagerSingleton();
        this.bind(LuckPermsService.class).asEagerSingleton();
        this.bind(TagService.class).asEagerSingleton();
        this.bind(UserService.class).asEagerSingleton();
    }

}
