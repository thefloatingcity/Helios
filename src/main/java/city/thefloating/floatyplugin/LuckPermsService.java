package city.thefloating.floatyplugin;

import com.google.inject.Inject;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.track.Track;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LuckPermsService {

  private final FloatyPlugin plugin;

  private @Nullable LuckPerms luckPerms;

  @Inject
  public LuckPermsService(
      final FloatyPlugin plugin
  ) {
    this.plugin = plugin;
  }

  private static @Nullable String getGroupInTrack(final Player player, final Track track) {
    final List<String> groups = new ArrayList<>(track.getGroups());
    Collections.reverse(groups);
    return getGroup(player, groups);
  }

  /**
   * @param player         the player of whom to search the permissions
   * @param possibleGroups the groups to check in descending priority
   * @return the first encounter of a group in possible groups that the player has
   */
  private static @Nullable String getGroup(final Player player, final List<String> possibleGroups) {
    for (final String group : possibleGroups) {
      if (player.hasPermission("group." + group)) {
        return group;
      }
    }
    return null;
  }

  public boolean load() {
    final RegisteredServiceProvider<LuckPerms> provider = this.plugin.getServer()
        .getServicesManager().getRegistration(LuckPerms.class);
    if (provider == null) {
      return false;
    }
    this.luckPerms = provider.getProvider();
    return true;
  }

  public void promoteInTrack(final Player player, final String trackName) throws IllegalArgumentException {
    assert this.luckPerms != null;
    final Track track = this.luckPerms.getTrackManager().getTrack(trackName);
    if (track == null) {
      throw new IllegalArgumentException("track does not exist");
    }

    this.luckPerms.getUserManager().modifyUser(
        player.getUniqueId(),
        user -> track.promote(user, ImmutableContextSet.empty())
    );
  }

  public @Nullable Group getNextGroupInTrack(final Player player, final String trackName) {
    assert this.luckPerms != null;
    final GroupManager groupManager = this.luckPerms.getGroupManager();

    final Track track = this.luckPerms.getTrackManager().getTrack(trackName);
    if (track == null) {
      throw new IllegalArgumentException("track does not exist");
    }

    final String currentGroupName = getGroupInTrack(player, track);
    if (currentGroupName == null) {
      return null;
    }

    final Group currentGroup = groupManager.getGroup(currentGroupName);
    if (currentGroup == null) {
      throw new NullPointerException("current group does not exist");
    }

    final String nextGroupName = track.getNext(currentGroup);
    if (nextGroupName == null) {
      return null;
    }

    final Group nextGroup = groupManager.getGroup(nextGroupName);
    if (nextGroup == null) {
      throw new NullPointerException("next group does not exist");
    }

    return nextGroup;
  }

}
