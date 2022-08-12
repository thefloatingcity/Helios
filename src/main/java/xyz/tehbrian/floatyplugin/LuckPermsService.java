package xyz.tehbrian.floatyplugin;

import com.google.inject.Inject;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.track.Track;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public final class LuckPermsService {

  private final FloatyPlugin floatyPlugin;

  private @Nullable LuckPerms luckPerms;

  @Inject
  public LuckPermsService(
      final FloatyPlugin floatyPlugin
  ) {
    this.floatyPlugin = floatyPlugin;
  }

  public boolean load() {
    final RegisteredServiceProvider<LuckPerms> provider = this.floatyPlugin
        .getServer()
        .getServicesManager()
        .getRegistration(LuckPerms.class);
    if (provider == null) {
      return false;
    }
    this.luckPerms = provider.getProvider();
    return true;
  }

  /**
   * @param player    the player to promote
   * @param trackName the track to promote the player on
   * @throws IllegalArgumentException if the track does not exist
   */
  public void promoteInTrack(final Player player, final String trackName)
      throws IllegalArgumentException {
    final User user = this.luckPerms.getPlayerAdapter(Player.class).getUser(player);
    final @Nullable Track track = this.luckPerms.getTrackManager().getTrack(trackName);
    if (track == null) {
      throw new IllegalArgumentException("Track does not exist");
    }

    track.promote(user, ImmutableContextSet.empty());
    this.luckPerms.getUserManager().saveUser(user);
  }

  public @Nullable Group getNextGroupInTrack(final Player player, final String trackName) {
    final @Nullable Track track = this.luckPerms.getTrackManager().getTrack(trackName);
    if (track == null) {
      throw new NullPointerException("Track does not exist");
    }

    final var groupManager = this.luckPerms.getGroupManager();
    final var currentGroupName = this.luckPerms.getPlayerAdapter(Player.class).getUser(player).getPrimaryGroup();

    final @Nullable Group currentGroup = groupManager.getGroup(currentGroupName);
    if (currentGroup == null) {
      throw new NullPointerException("Current group does not exist");
    }

    final @Nullable String nextGroupName = Objects.requireNonNull(track).getNext(currentGroup);
    if (nextGroupName == null) {
      return null;
    }

    return groupManager.getGroup(nextGroupName);
  }

}
