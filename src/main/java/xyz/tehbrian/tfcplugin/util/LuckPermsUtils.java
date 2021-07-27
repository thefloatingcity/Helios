package xyz.tehbrian.tfcplugin.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.entity.Player;
import xyz.tehbrian.tfcplugin.TFCPlugin;

import java.util.Objects;

public class LuckPermsUtils {

    private static final LuckPerms luckPerms = TFCPlugin.getInstance().getLuckPermsApi();

    private LuckPermsUtils() {
    }

    public static String getPlayerPrefix(final Player player) {
        final User user = Objects.requireNonNull(luckPerms.getUserManager().getUser(player.getUniqueId()));

        final QueryOptions queryOptions = luckPerms.getContextManager().getQueryOptions(player);

        final CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);
        return metaData.getPrefix() == null ? "" : metaData.getPrefix();
    }

    public static String getPlayerSuffix(final Player player) {
        final User user = Objects.requireNonNull(luckPerms.getUserManager().getUser(player.getUniqueId()));

        final QueryOptions queryOptions = luckPerms.getContextManager().getQueryOptions(player);

        final CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);
        return metaData.getSuffix() == null ? "" : metaData.getSuffix();
    }

    public static void addPlayerGroup(final Player player, final String group) {
        final User user = Objects.requireNonNull(luckPerms.getUserManager().getUser(player.getUniqueId()));

        user.data().add(InheritanceNode.builder(group).build());
        luckPerms.getUserManager().saveUser(user);
    }

}
