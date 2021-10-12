package xyz.tehbrian.floatyplugin.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;

@SuppressWarnings("ClassCanBeRecord")
public final class ServerPingListener implements Listener {

    private final LangConfig langConfig;

    @Inject
    public ServerPingListener(
            final @NonNull LangConfig langConfig
    ) {
        this.langConfig = langConfig;
    }

    @EventHandler
    public void onServerPing(final PaperServerListPingEvent e) {
        e.motd(this.langConfig.c(NodePath.path("server_motd")));
    }

}
