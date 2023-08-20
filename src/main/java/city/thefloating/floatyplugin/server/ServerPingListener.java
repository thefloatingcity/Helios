package city.thefloating.floatyplugin.server;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spongepowered.configurate.NodePath;
import city.thefloating.floatyplugin.config.LangConfig;

public final class ServerPingListener implements Listener {

  private final LangConfig langConfig;

  @Inject
  public ServerPingListener(
      final LangConfig langConfig
  ) {
    this.langConfig = langConfig;
  }

  @EventHandler
  public void onServerPing(final PaperServerListPingEvent e) {
    e.motd(this.langConfig.c(NodePath.path("server-motd")));
  }

}
