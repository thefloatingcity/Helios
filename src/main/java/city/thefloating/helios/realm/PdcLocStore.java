package city.thefloating.helios.realm;

import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public final class PdcLocStore {

  private final JavaPlugin plugin;

  @Inject
  public PdcLocStore(
      final JavaPlugin plugin
  ) {
    this.plugin = plugin;
  }

  public NamespacedKey key(final String key) {
    return new NamespacedKey(this.plugin, key);
  }

  public @Nullable WorldlessLocation getLocation(final Player owner, final NamespacedKey locKey) {
    return this.getLocation(owner.getPersistentDataContainer(), locKey);
  }

  private @Nullable WorldlessLocation getLocation(final PersistentDataContainer owner, final NamespacedKey locKey) {
    if (!owner.has(locKey)) {
      return null;
    }
    final PersistentDataContainer locPdc = owner.get(locKey, PersistentDataType.TAG_CONTAINER);
    assert locPdc != null; // we just checked that the data has the key.
    return this.getLocation(locPdc);
  }

  private @Nullable WorldlessLocation getLocation(final PersistentDataContainer locPdc) {
    final Double x = locPdc.get(this.key("x"), PersistentDataType.DOUBLE);
    final Double y = locPdc.get(this.key("y"), PersistentDataType.DOUBLE);
    final Double z = locPdc.get(this.key("z"), PersistentDataType.DOUBLE);
    final Float yaw = locPdc.get(this.key("yaw"), PersistentDataType.FLOAT);
    final Float pitch = locPdc.get(this.key("pitch"), PersistentDataType.FLOAT);

    if (x == null || y == null || z == null || yaw == null || pitch == null) {
      return null;
    }
    return new WorldlessLocation(x, y, z, yaw, pitch);
  }

  public void setLocation(final Player owner, final NamespacedKey locKey, final @Nullable Location loc) {
    this.setLocation(owner.getPersistentDataContainer(), locKey, loc);
  }

  private void setLocation(
      final PersistentDataContainer owner,
      final NamespacedKey locKey,
      final @Nullable Location loc
  ) {
    final PersistentDataContainer locData = owner.getAdapterContext().newPersistentDataContainer();
    this.setLocation(locData, loc);
    owner.set(locKey, PersistentDataType.TAG_CONTAINER, locData);
  }

  private void setLocation(final PersistentDataContainer locPdc, final @Nullable Location loc) {
    if (loc != null) {
      locPdc.set(this.key("x"), PersistentDataType.DOUBLE, loc.getX());
      locPdc.set(this.key("y"), PersistentDataType.DOUBLE, loc.getY());
      locPdc.set(this.key("z"), PersistentDataType.DOUBLE, loc.getZ());
      locPdc.set(this.key("yaw"), PersistentDataType.FLOAT, loc.getYaw());
      locPdc.set(this.key("pitch"), PersistentDataType.FLOAT, loc.getPitch());
    } else {
      locPdc.remove(this.key("x"));
      locPdc.remove(this.key("y"));
      locPdc.remove(this.key("z"));
      locPdc.remove(this.key("yaw"));
      locPdc.remove(this.key("pitch"));
    }
  }

  public record WorldlessLocation(
      double x, double y, double z,
      float yaw, float pitch
  ) {

  }

}
