package city.thefloating.floatyplugin.realm;

import city.thefloating.floatyplugin.PotEff;
import com.google.inject.Inject;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Responsible for transposing players to the different realms.
 */
public final class Transposer {

  private final WorldService worldService;
  private final PdcLocStore pdcLocStore;

  @Inject
  public Transposer(
      final WorldService worldService,
      final PdcLocStore pdcLocStore
  ) {
    this.worldService = worldService;
    this.pdcLocStore = pdcLocStore;
  }

  public void transpose(final Player player, final Realm destination) {
    final Realm current = Realm.of(player);
    this.setPreviousLocation(player, current);

    player.teleport(this.getNextLocation(player, destination));
    player.setFallDistance(0);
  }

  /**
   * Transpose player to backrooms with special effects.
   */
  public void noclipIntoBackrooms(final Player player) {
    final Location nextLocation = this.getNextLocation(player, Realm.BACKROOMS);
    this.transpose(player, Realm.BACKROOMS);

    player.addPotionEffect(PotEff.hidden(PotionEffectType.BLINDNESS, 60, 10));
    player.getWorld().spawnParticle(Particle.SMOKE_LARGE, nextLocation, 40, 2, 2, 2);
    player.playSound(nextLocation, Sound.BLOCK_PORTAL_TRAVEL, SoundCategory.MASTER, 4, 1);
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
        () -> this.worldService.ornateSpawn(realm)
    );
  }

  private @Nullable Location getPreviousLocation(final Player player, final Realm realm) {
    final PdcLocStore.WorldlessLocation wLoc = this.pdcLocStore.getLocation(player, this.prevLocKey(realm));
    if (wLoc == null) {
      return null;
    }
    return new Location(
        this.worldService.getWorld(realm),
        wLoc.x(), wLoc.y(), wLoc.z(),
        wLoc.yaw(), wLoc.pitch()
    );
  }

  private void setPreviousLocation(final Player player, final Realm realm) {
    this.pdcLocStore.setLocation(player, this.prevLocKey(realm), player.getLocation());
  }

  private NamespacedKey prevLocKey(final Realm realm) {
    return this.pdcLocStore.key("previous-location-" + realm.toString());
  }

}
