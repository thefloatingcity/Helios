package xyz.tehbrian.tfcplugin.util;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import org.bukkit.entity.Player;
import xyz.tehbrian.tfcplugin.TFCPlugin;

public class LuckPermsUtils {

    private static final LuckPermsApi luckPermsApi = TFCPlugin.getInstance().getLuckPermsApi();

    private LuckPermsUtils() {
    }

    public static String getPlayerPrefix(Player player) {
        User user = luckPermsApi.getUserManager().getUser(player.getUniqueId());
        Contexts contexts = luckPermsApi.getContextManager().getApplicableContexts(player);
        MetaData metaData = user.getCachedData().getMetaData(contexts);
        return metaData.getPrefix() == null ? "" : metaData.getPrefix();
    }

    public static String getPlayerSuffix(Player player) {
        User user = luckPermsApi.getUserManager().getUser(player.getUniqueId());
        Contexts contexts = luckPermsApi.getContextManager().getApplicableContexts(player);
        MetaData metaData = user.getCachedData().getMetaData(contexts);
        return metaData.getSuffix() == null ? "" : metaData.getSuffix();
    }

    public static void addPlayerGroup(Player player, String group) {
        User user = luckPermsApi.getUserManager().getUser(player.getUniqueId());
        user.setPermission(luckPermsApi.getNodeFactory().makeGroupNode(group).build());
        luckPermsApi.getUserManager().saveUser(user);
    }
}
