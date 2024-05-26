package city.thefloating.helios.soul;

import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.ConfigurateException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Charon, ferryman of the {@link Soul}s.
 */
public final class Charon {

  private final Otzar otzar;

  private final Map<UUID, Soul> souls = new HashMap<>();

  @Inject
  public Charon(final Otzar otzar) {
    this.otzar = otzar;
  }

  public Soul grab(final UUID uuid) {
    return this.souls.computeIfAbsent(uuid, u -> {
      final var soul = new Soul(uuid);
      if (this.otzar.spirits().containsKey(uuid)) {
        final var spirit = this.otzar.spirits().get(uuid);
        if (spirit.netherInfractions() != null) {
          soul.netherInfractions(spirit.netherInfractions());
        }
        if (spirit.markdown() != null) {
          soul.markdown(spirit.markdown());
        }
      }
      return soul;
    });
  }

  public Soul grab(final Player player) {
    return this.grab(player.getUniqueId());
  }

  public void save() throws ConfigurateException {
    for (final var soul : this.souls.values()) {
      final var spirit = new Otzar.Data.Spirit(soul.netherInfractions(), soul.markdown());
      this.otzar.spirits().put(soul.getUuid(), spirit);
    }
    this.otzar.save();
  }

}
