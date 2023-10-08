package city.thefloating.floatyplugin.soul;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Charon, ferryman of the {@link Soul}s.
 */
public final class Charon {

  private final Map<UUID, Soul> soulMap = new HashMap<>();

  public Soul getSoul(final UUID uuid) {
    return this.soulMap.computeIfAbsent(uuid, Soul::new);
  }

  public Soul getSoul(final Player player) {
    return this.getSoul(player.getUniqueId());
  }

}
