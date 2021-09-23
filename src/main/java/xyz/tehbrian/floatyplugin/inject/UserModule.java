package xyz.tehbrian.floatyplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.floatyplugin.user.UserService;

/**
 * Guice module which provides bindings for {@link UserService}.
 */
public final class UserModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(UserService.class).asEagerSingleton();
    }

}
