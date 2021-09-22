package xyz.tehbrian.tfcplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.tfcplugin.user.UserService;

/**
 * Guice module which provides bindings for {@link UserService}.
 */
public final class UserModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(UserService.class).asEagerSingleton();
    }

}
