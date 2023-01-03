package xyz.tehbrian.floatyplugin.inject;

import com.google.inject.AbstractModule;
import xyz.tehbrian.floatyplugin.LuckPermsService;
import xyz.tehbrian.floatyplugin.config.BooksConfig;
import xyz.tehbrian.floatyplugin.config.ConfigConfig;
import xyz.tehbrian.floatyplugin.config.EmotesConfig;
import xyz.tehbrian.floatyplugin.config.InventoriesConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.realm.Transposer;
import xyz.tehbrian.floatyplugin.realm.WorldService;
import xyz.tehbrian.floatyplugin.tag.TagGame;
import xyz.tehbrian.floatyplugin.transportation.FlightService;
import xyz.tehbrian.floatyplugin.user.UserService;

public final class SingletonModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(FlightService.class).asEagerSingleton();
    this.bind(LuckPermsService.class).asEagerSingleton();
    this.bind(TagGame.class).asEagerSingleton();
    this.bind(UserService.class).asEagerSingleton();
    this.bind(WorldService.class).asEagerSingleton();
    this.bind(Transposer.class).asEagerSingleton();

    // configs.
    this.bind(BooksConfig.class).asEagerSingleton();
    this.bind(ConfigConfig.class).asEagerSingleton();
    this.bind(EmotesConfig.class).asEagerSingleton();
    this.bind(InventoriesConfig.class).asEagerSingleton();
    this.bind(LangConfig.class).asEagerSingleton();
  }

}
