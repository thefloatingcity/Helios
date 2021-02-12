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

    public Booster newBooster(String name, Location location, int radius, Axis axis) {
        Booster booster = new Booster(location, radius, axis);
        boosters.put(name, booster);
        return booster;
    }

    public void addGlidingPlayer(Player player) {
        glidingPlayers.add(player);
    }

    public void removeGlidingPlayer(Player player) {
        glidingPlayers.remove(player);
    }

    public boolean isPlayerGliding(Player player) {
        return glidingPlayers.contains(player);
    }

    public void setPlayerGliding(Player player, boolean bool) {
        if (bool) {
            glidingPlayers.add(player);
        } else {
            glidingPlayers.remove(player);
        }
    }

    public Set<Player> getGlidingPlayers() {
        return glidingPlayers;
    }

    public Set<Booster> getBoosters() { return new HashSet<>(boosters.values()); }

    public Booster getBooster(String name) {
        return boosters.get(name);
    }
/*
    public Set<Location> getBoosterLocations() {
        return boosters.keySet();
    }

 */

    public boolean checkIfPlayerIsInBooster(Player player) {
        return false;
    }
}