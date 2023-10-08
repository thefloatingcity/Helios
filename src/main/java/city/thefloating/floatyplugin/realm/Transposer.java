package city.thefloating.floatyplugin.realm;

import city.thefloating.floatyplugin.FloatyPlugin;
import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Responsible for transposing players to the different realms.
 */
public final class Transposer {

  private final FloatyPlugin plugin;
  private final WorldService worldService;

  @Inject
  public Transposer(
      final FloatyPlugin plugin,
      final WorldService worldService
  ) {
    this.plugin = plugin;
    this.worldService = worldService;
  }

  public void transpose(final Player player, final Realm destination) {
    final Realm current = Realm.from(player.getWorld());
    this.setPreviousLocation(player, current);

    player.teleport(this.getNextLocation(player, destination));
    player.setFallDistance(0);
  }

  /**
   * Get the location that a player would be teleported to if they were transposed
   * via {@link #transpose} to the provided realm.
   *
   * @param player the player
   * @param realm  the realm
   * @return the location that the player would be teleported to on transpose
   */
  public Location getNextLocation(final Player player, final Realm realm) {
    return Objects.requireNonNullElseGet(
        this.getPreviousLocation(player, realm),
        () -> this.worldService.getSpawnPoint(realm)
    );
  }

  private @Nullable Location getPreviousLocation(final Player player, final Realm realm) {
    final WorldlessLocation wLoc = this.getLocation(player.getPersistentDataContainer(), this.prevLocKey(realm));
    if (wLoc == null) {
      return null;
    }
    return new Location(
        this.worldService.getWorld(realm),
        wLoc.x(), wLoc.y(), wLoc.z(),
        wLoc.yaw(), wLoc.pitch()
    );
  }

  private @Nullable WorldlessLocation getLocation(final PersistentDataContainer data, final NamespacedKey locKey) {
    if (!data.has(locKey)) {
      return null;
    }
    final PersistentDataContainer locPdc = data.get(locKey, PersistentDataType.TAG_CONTAINER);
    assert locPdc != null; // we just checked that the data has the key.
    return this.getLocation(locPdc);
  }

  private @Nullable WorldlessLocation getLocation(final PersistentDataContainer pdc) {
    final Double x = pdc.get(this.key("x"), PersistentDataType.DOUBLE);
    final Double y = pdc.get(this.key("y"), PersistentDataType.DOUBLE);
    final Double z = pdc.get(this.key("z"), PersistentDataType.DOUBLE);
    final Float yaw = pdc.get(this.key("yaw"), PersistentDataType.FLOAT);
    final Float pitch = pdc.get(this.key("pitch"), PersistentDataType.FLOAT);

    if (x == null || y == null || z == null || yaw == null || pitch == null) {
      return null;
    }

    return new WorldlessLocation(x, y, z, yaw, pitch);
  }

  private void setPreviousLocation(final Player player, final Realm realm) {
    this.setLocation(player.getPersistentDataContainer(), this.prevLocKey(realm), player.getLocation());
  }

  private void setLocation(final PersistentDataContainer data, final NamespacedKey locKey, final Location loc) {
    final PersistentDataContainer locData = data.getAdapterContext().newPersistentDataContainer();
    this.setLocation(locData, loc);
    data.set(locKey, PersistentDataType.TAG_CONTAINER, locData);
  }

  private void setLocation(final PersistentDataContainer pdc, final Location loc) {
    pdc.set(this.key("x"), PersistentDataType.DOUBLE, loc.getX());
    pdc.set(this.key("y"), PersistentDataType.DOUBLE, loc.getY());
    pdc.set(this.key("z"), PersistentDataType.DOUBLE, loc.getZ());
    pdc.set(this.key("yaw"), PersistentDataType.FLOAT, loc.getYaw());
    pdc.set(this.key("pitch"), PersistentDataType.FLOAT, loc.getPitch());
  }

  private NamespacedKey prevLocKey(final Realm realm) {
    return this.key("previous-location-" + realm.toString());
  }

  private NamespacedKey key(final String key) {
    return new NamespacedKey(this.plugin, key);
  }

  private record WorldlessLocation(
      double x, double y, double z,
      float yaw, float pitch
  ) {

  }

}
