package city.thefloating.helios.inject;

import city.thefloating.helios.LuckPermsService;
import city.thefloating.helios.config.BooksConfig;
import city.thefloating.helios.config.ConfigConfig;
import city.thefloating.helios.config.EmotesConfig;
import city.thefloating.helios.config.LangConfig;
import city.thefloating.helios.config.PianoNotesConfig;
import city.thefloating.helios.nextbot.Nate;
import city.thefloating.helios.realm.Transposer;
import city.thefloating.helios.realm.WorldService;
import city.thefloating.helios.soul.Charon;
import city.thefloating.helios.soul.Otzar;
import city.thefloating.helios.tag.TagGame;
import city.thefloating.helios.transportation.FlightService;
import city.thefloating.helios.transportation.PortalListener;
import com.google.inject.AbstractModule;

public final class SingletonModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(FlightService.class).asEagerSingleton();
    this.bind(LuckPermsService.class).asEagerSingleton();
    this.bind(TagGame.class).asEagerSingleton();
    this.bind(Charon.class).asEagerSingleton();
    this.bind(WorldService.class).asEagerSingleton();
    this.bind(Transposer.class).asEagerSingleton();
    this.bind(Nate.class).asEagerSingleton();
    this.bind(PortalListener.class).asEagerSingleton();
    this.bind(BooksConfig.class).asEagerSingleton();
    this.bind(ConfigConfig.class).asEagerSingleton();
    this.bind(EmotesConfig.class).asEagerSingleton();
    this.bind(PianoNotesConfig.class).asEagerSingleton();
    this.bind(LangConfig.class).asEagerSingleton();
    this.bind(Otzar.class).asEagerSingleton();
  }

}
