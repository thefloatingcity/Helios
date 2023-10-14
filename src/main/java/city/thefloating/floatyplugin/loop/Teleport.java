package city.thefloating.floatyplugin.loop;

import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public final class Teleport {

  private Teleport() {
  }

  public static void relative(
      final Entity entity, final Location loc
  ) {
    entity.teleport(
        loc,
        TeleportFlag.Relative.X,
        TeleportFlag.Relative.Y,
        TeleportFlag.Relative.Z,
        TeleportFlag.Relative.YAW,
        TeleportFlag.Relative.PITCH
    );
  }

}
