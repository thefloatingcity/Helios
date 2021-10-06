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

//    no-permission: §cI'm sorry, but you don't have permission for that command.
//    connection-throttle: §c»§6»§e»§f Connection throttled. Please wait a moment before reconnecting. §e«§6«§c«
//    authentication-servers-down: §4»§c»§6»§f Mojang's authentication servers seem to be down. Try joining again in a bit! §6«§c«§4«
//    flying-player: §f»§7»§8»§f You can't fly in The Floating City! Perhaps it was a glitch, or maybe lag? §8«§7«§f«
//    flying-vehicle: §f»§7»§8»§f You can't fly in The Floating City! Perhaps it was a glitch, or maybe lag? §8«§7«§f«
//    whitelist: §f»§7»§8»§f You aren't whitelisted on this server! §8«§7«§f«
//    unknown-command: §fUnrecognized command. Try §e/help§f for a list of commands.
//    server-full: §4»§c»§6»§f The Floating City is currently full! Try joining again in a bit! §6«§c«§4«
//    outdated-client: §1»§9»§3»§f The Floating City is running a newer version! Please use {0} §3«§9«§1«
//    outdated-server: §1»§9»§3»§f The Floating City is running an older version! Please use {0} §3«§9«§1«
//    restart: §9»§3»§b»§f The Floating City is restarting. Come back in a moment! §b«§3«§9«
//    shutdown-message: §f»§7»§8»§f The Floating City has been closed. See ya soon! §8«§7«§f«

    @EventHandler
    public void onServerPing(final PaperServerListPingEvent e) {
        e.motd(this.langConfig.c(NodePath.path("server_motd")));
    }

}
