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

    private LuckPermsUtils() {}

    public static String getPlayerPrefix(Player player) {
        User user = Objects.requireNonNull(luckPerms.getUserManager().getUser(player.getUniqueId()));

        QueryOptions queryOptions = luckPerms.getContextManager().getQueryOptions(player);

        CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);
        return metaData.getPrefix() == null ? "" : metaData.getPrefix();
    }

    public static String getPlayerSuffix(Player player) {
        User user = Objects.requireNonNull(luckPerms.getUserManager().getUser(player.getUniqueId()));

        QueryOptions queryOptions = luckPerms.getContextManager().getQueryOptions(player);

        CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);
        return metaData.getSuffix() == null ? "" : metaData.getSuffix();
    }

    public static void addPlayerGroup(Player player, String group) {
        User user = Objects.requireNonNull(luckPerms.getUserManager().getUser(player.getUniqueId()));

        user.data().add(InheritanceNode.builder(group).build());
        luckPerms.getUserManager().saveUser(user);
    }
}
