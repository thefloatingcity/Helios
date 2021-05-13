package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import xyz.tehbrian.tfcplugin.TFCPlugin;

public class BoosterListener implements Listener {

    private final TFCPlugin main;

    public BoosterListener(TFCPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerToggleGlide(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            this.main.getBoosterManager().setPlayerGliding(player, player.isGliding());
        }
    }

    public void startThingy() {
        for (Player player : this.main.getBoosterManager().getGlidingPlayers()) {
            if (this.main.getBoosterManager().checkIfPlayerIsInBooster(player)) {
                player.setVelocity(player.getLocation().getDirection().multiply(3));
            }
        }
    }
}
