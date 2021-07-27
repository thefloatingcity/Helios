package xyz.tehbrian.tfcplugin.booster;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BoosterManager {

    private final Map<String, Booster> boosters = new HashMap<>();
    private final Set<Player> glidingPlayers = new HashSet<>();

    public Booster newBooster(final String name, final Location location, final int radius, final Axis axis) {
        final Booster booster = new Booster(location, radius, axis);
        this.boosters.put(name, booster);
        return booster;
    }

    public void addGlidingPlayer(final Player player) {
        this.glidingPlayers.add(player);
    }

    public void removeGlidingPlayer(final Player player) {
        this.glidingPlayers.remove(player);
    }

    public boolean isPlayerGliding(final Player player) {
        return this.glidingPlayers.contains(player);
    }

    public void setPlayerGliding(final Player player, final boolean bool) {
        if (bool) {
            this.glidingPlayers.add(player);
        } else {
            this.glidingPlayers.remove(player);
        }
    }

    public Set<Player> getGlidingPlayers() {
        return this.glidingPlayers;
    }

    public Set<Booster> getBoosters() {
        return new HashSet<>(this.boosters.values());
    }

    public Booster getBooster(final String name) {
        return this.boosters.get(name);
    }
/*
    public Set<Location> getBoosterLocations() {
        return boosters.keySet();
    }

 */

    public boolean checkIfPlayerIsInBooster(final Player player) {
        return false;
    }

}
