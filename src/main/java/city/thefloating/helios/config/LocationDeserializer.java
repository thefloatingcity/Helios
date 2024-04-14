package city.thefloating.helios.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Objects;

public final class LocationDeserializer {

  private LocationDeserializer() {
  }

  public static Location deserializeLocation(final ConfigurationNode section) {
    return new Location(
        Bukkit.getWorld(Objects.requireNonNull(section.node("world").getString("world"))),
        section.node("x").getDouble(0),
        section.node("y").getDouble(0),
        section.node("z").getDouble(0),
        section.node("yaw").getFloat(0),
        section.node("pitch").getFloat(0)
    );
  }

}
