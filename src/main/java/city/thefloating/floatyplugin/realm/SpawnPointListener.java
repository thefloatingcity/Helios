package city.thefloating.floatyplugin.realm;

import com.destroystokyo.paper.MaterialTags;
import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.destroystokyo.paper.event.player.PlayerSetSpawnEvent;
import com.google.inject.Inject;
import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class SpawnPointListener implements Listener {

  private final List<Player> bedDestroyed = new ArrayList<>();
  private final List<Player> bedObstructed = new ArrayList<>();

  private final WorldService worldService;
  private final PdcLocStore pdcLocStore;

  @Inject
  public SpawnPointListener(
      final WorldService worldService,
      final PdcLocStore pdcLocStore
  ) {
    this.worldService = worldService;
    this.pdcLocStore = pdcLocStore;
  }

  /**
   * Teleports players to their realm-specific spawn point on respawn.
   */
  @EventHandler
  public void onRespawn(final PlayerRespawnEvent event) {
    final Player player = event.getPlayer();
    final Realm realm = Realm.from(player.getWorld());
    final @Nullable Location playerSpawn = this.getSpawn(player, realm);

    if (playerSpawn == null) {
      // player hasn't set spawn yet.
      event.setRespawnLocation(this.worldService.getSpawnPoint(realm));
      return;
    }

    if (!MaterialTags.BEDS.isTagged(playerSpawn.getBlock())) {
      // bed destroyed. reset player's spawn.
      event.setRespawnLocation(this.worldService.getSpawnPoint(realm));
      this.removeSpawn(player, realm);
      this.bedDestroyed.add(player);
      return;
    }

    Location bedLoc = playerSpawn.getBlock().getLocation();
    BoundingBox playerBox = player.getBoundingBox();
    BoundingBox playerOnBedBox = BoundingBox.of(
        bedLoc.clone().add(0.5, 0.56250, 0.5).add(0, playerBox.getHeight() / 2, 0),
        playerBox.getWidthX() / 2,
        playerBox.getHeight() / 2,
        playerBox.getWidthZ() / 2
    );
    if (playerOnBedBox.overlaps(bedLoc.clone().add(0, 1, 0).getBlock().getBoundingBox())
        || playerOnBedBox.overlaps(bedLoc.clone().add(0, 2, 0).getBlock().getBoundingBox())) {
      // bed obstructed. don't reset player's home, but spawn them at world spawn.
      event.setRespawnLocation(this.worldService.getSpawnPoint(realm));
      this.bedObstructed.add(player);
      return;
    }

    // player spawn is good. :)
    event.setRespawnLocation(playerSpawn);
  }

  /**
   * Handles effects that happen post-respawn.
   */
  @EventHandler
  public void onPostRespawn(final PlayerPostRespawnEvent event) {
    final Player player = event.getPlayer();

    if (this.bedDestroyed.contains(player)) {
      this.bedDestroyed.remove(player);
      player.showTitle(Title.title(
          Component.text("your bed was destroyed").color(NamedTextColor.RED),
          Component.text("you're back at world spawn").color(NamedTextColor.GRAY),
          Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(5), Duration.ofSeconds(1))
      ));
      player.playSound(Sound.sound(org.bukkit.Sound.ENTITY_TURTLE_EGG_BREAK, Sound.Source.MASTER, 1F, 0.7F));
      player.playSound(Sound.sound(org.bukkit.Sound.BLOCK_IRON_DOOR_CLOSE, Sound.Source.MASTER, 1F, 0.7F));
    }

    if (this.bedObstructed.contains(player)) {
      this.bedObstructed.remove(player);
      player.showTitle(Title.title(
          Component.text("your bed is obstructed").color(NamedTextColor.GOLD),
          Component.text("you'll respawn at world spawn until it's fixed").color(NamedTextColor.GRAY),
          Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(5), Duration.ofSeconds(1))
      ));
      player.playSound(Sound.sound(org.bukkit.Sound.ENTITY_PLAYER_HURT, Sound.Source.MASTER, 1F, 0.8F));
      player.playSound(Sound.sound(org.bukkit.Sound.BLOCK_IRON_DOOR_CLOSE, Sound.Source.MASTER, 1F, 0.9F));
    }
  }

  /**
   * Sets the player's spawn point on deep sleep.
   */
  @EventHandler
  public void onDeepSleep(final PlayerDeepSleepEvent event) {
    final Player player = event.getPlayer();
    player.showTitle(Title.title(
        Component.text("spawn point set").color(NamedTextColor.LIGHT_PURPLE),
        Component.text()
            .append(Component.text("for the ").color(NamedTextColor.GRAY))
            .append(Component.text(Realm.from(player.getWorld()).toString()).color(NamedTextColor.GOLD))
            .build(),
        Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(5), Duration.ofSeconds(1))
    ));
    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 160, 1, true, false, false));
    player.playSound(Sound.sound(org.bukkit.Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, Sound.Source.MASTER, 1F, 1.3F));
    this.setSpawn(player, player.getLocation());
  }

  /**
   * Prevents sleeping in beds from skipping the night.
   */
  @EventHandler
  public void onTimeSkip(final TimeSkipEvent event) {
    if (event.getSkipReason() == TimeSkipEvent.SkipReason.NIGHT_SKIP) {
      event.setCancelled(true);
    }
  }

  /**
   * Allows beds in the nether and end.
   */
  @EventHandler
  public void onBedEnter(final PlayerBedEnterEvent event) {
    if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_HERE) {
      event.setUseBed(Event.Result.ALLOW);
    }

    if (event.useBed() == Event.Result.ALLOW || event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
      event.getPlayer().showTitle(Title.title(
          Component.empty(),
          Component.text(".. stay in bed to set your spawn point").color(NamedTextColor.DARK_GRAY),
          Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(5), Duration.ofSeconds(1))
      ));
    }
  }

  /**
   * Prevent spawn from being set by means other than commands and plugins.
   */
  @EventHandler
  public void onSetSpawn(final PlayerSetSpawnEvent event) {
    event.setNotifyPlayer(false);
    event.setLocation(null);
    if (event.getCause() == PlayerSetSpawnEvent.Cause.COMMAND
        || event.getCause() == PlayerSetSpawnEvent.Cause.PLUGIN) {
      this.setSpawn(event.getPlayer(), event.getLocation());
    }
  }

  private @Nullable Location getSpawn(final Player player, final Realm realm) {
    final PdcLocStore.WorldlessLocation wLoc = this.pdcLocStore.getLocation(player, this.spawnKey(realm));
    if (wLoc == null) {
      return null;
    }
    return new Location(
        this.worldService.getWorld(realm),
        wLoc.x(), wLoc.y(), wLoc.z(),
        wLoc.yaw(), wLoc.pitch()
    );
  }

  private void setSpawn(final Player player, final Location location) {
    this.pdcLocStore.setLocation(player, this.spawnKey(Realm.from(location)), location);
  }

  private void removeSpawn(final Player player, final Realm realm) {
    this.pdcLocStore.setLocation(player, this.spawnKey(realm), null);
  }

  private NamespacedKey spawnKey(final Realm realm) {
    return this.pdcLocStore.key("spawn-" + realm.toString());
  }

}
