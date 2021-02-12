package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import xyz.tehbrian.tfcplugin.TFCPlugin;

@SuppressWarnings("unused")
public class FlightListener implements Listener {

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        TFCPlugin.getInstance().getPlayerDataManager().getPlayerData(event.getPlayer()).disableFlight();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TFCPlugin.getInstance().getPlayerDataManager().getPlayerData(event.getPlayer()).disableFlight();
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        TFCPlugin.getInstance().getPlayerDataManager().getPlayerData(event.getPlayer()).disableFlight();
    }
}
