package xyz.tehbrian.floatyplugin;

import com.google.inject.Inject;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.query.QueryOptions;
import net.luckperms.api.track.Track;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public final class LuckPermsService {

    private final FloatyPlugin floatyPlugin;

    private @MonotonicNonNull LuckPerms luckPerms;

    @Inject
    public LuckPermsService(
            final @NonNull FloatyPlugin floatyPlugin
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

    public String getPrefix(final Player player) {
        final User user = Objects.requireNonNull(this.luckPerms.getUserManager().getUser(player.getUniqueId()));
        final QueryOptions queryOptions = this.luckPerms.getContextManager().getQueryOptions(player);
        final CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);

        return metaData.getPrefix() == null ? "" : metaData.getPrefix();
    }

    public String getSuffix(final Player player) {
        final User user = Objects.requireNonNull(this.luckPerms.getUserManager().getUser(player.getUniqueId()));
        final QueryOptions queryOptions = this.luckPerms.getContextManager().getQueryOptions(player);
        final CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);

        return metaData.getSuffix() == null ? "" : metaData.getSuffix();
    }

    public void promote(final Player player, final String trackName) {
        final User user = Objects.requireNonNull(this.luckPerms.getUserManager().getUser(player.getUniqueId()));
        final Track track = Objects.requireNonNull(this.luckPerms.getTrackManager().getTrack(trackName));

        track.promote(user, ImmutableContextSet.empty());
        this.luckPerms.getUserManager().saveUser(user);
    }

    public void addGroup(final Player player, final String groupName) {
        final User user = Objects.requireNonNull(this.luckPerms.getUserManager().getUser(player.getUniqueId()));

        user.data().add(InheritanceNode.builder(groupName).build());
        this.luckPerms.getUserManager().saveUser(user);
    }

    public @Nullable Group getNextGroup(final Player player, final String trackName) {
        final var playerTrack = Objects.requireNonNull(this.luckPerms).getTrackManager().getTrack(trackName);
        final var groupManager = Objects.requireNonNull(this.luckPerms).getGroupManager();

        final var currentGroupName = this.getPrimaryGroup(player);
        final var currentGroup = Objects.requireNonNull(groupManager.getGroup(currentGroupName));

        final @Nullable String nextGroupName = Objects.requireNonNull(playerTrack).getNext(currentGroup);
        if (nextGroupName == null) {
            return null;
        }

        return groupManager.getGroup(nextGroupName);
    }

    public String getPrimaryGroup(final Player player) {
        final User user = Objects.requireNonNull(this.luckPerms.getUserManager().getUser(player.getUniqueId()));

        return user.getPrimaryGroup();
    }

    public @Nullable LuckPerms getLuckPerms() {
        return this.luckPerms;
    }

}
