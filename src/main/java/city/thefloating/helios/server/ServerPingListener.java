package city.thefloating.helios.server;

import city.thefloating.helios.config.LangConfig;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.map.MinecraftFont;
import org.spongepowered.configurate.NodePath;

import java.util.List;
import java.util.Random;

public final class ServerPingListener implements Listener {

  private static final List<String> CATCHES = List.of(
      "Flightless freebuild in the void.",
      "Flightless freebuild in a void world.",
      "Flightless freebuild in an infinite void.",
      "Flightless creative building in the void.",
      "Flightless creative building in a void world.",
      "Flightless creative building in an infinite void.",
      "Flightless creation in the void.",
      "Flightless creation in a void world.",
      "Flightless creation in an infinite void.",
      "Creative building in the void, with no fly.",
      "Creative building in a void world, with no fly.",
      "Creative building in an infinite void, with no fly.",
      "Creative building in the void, without flight.",
      "Creative building in a void world, without flight.",
      "Creative building in an infinite void, without flight.",
      "Grounded creativity in the void.",
      "Grounded creativity in a void world.",
      "Grounded creativity in an infinite void.",
      "Unleash creation unto the void.",
      "Unleash creation unto a void world.",
      "Unleash creation unto an infinite void.",
      "Building, but more annoying.",
      "Deep fried cheese sticks, mmm.",
      "Does anyone even read these?"
  );

  private static final int MOTD_WIDTH = 270; // max server motd width as of 1.20.2.
  private static final Random RANDOM = new Random();

  private final LangConfig langConfig;

  @Inject
  public ServerPingListener(
      final LangConfig langConfig
  ) {
    this.langConfig = langConfig;
  }

  private static String center(final String text) {
    final int totalPaddingLen = (MOTD_WIDTH - MinecraftFont.Font.getWidth(text)) / (MinecraftFont.Font.getWidth(" ") + 1);
    final String padding = " ".repeat(totalPaddingLen / 2);
    return padding + text + padding;
  }

  public static <T> T randomIn(final List<T> list) {
    return list.get(RANDOM.nextInt(list.size()));
  }

  @EventHandler
  public void onServerPing(final PaperServerListPingEvent event) {
    event.motd(this.langConfig.c(
        NodePath.path("server-motd"),
        Placeholder.unparsed("catch", center(randomIn(CATCHES)))
    ));
  }

}
