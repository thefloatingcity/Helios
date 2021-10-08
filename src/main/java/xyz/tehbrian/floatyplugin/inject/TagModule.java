package xyz.tehbrian.floatyplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.floatyplugin.tag.TagService;

public final class TagModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(TagService.class).asEagerSingleton();
    }

}
