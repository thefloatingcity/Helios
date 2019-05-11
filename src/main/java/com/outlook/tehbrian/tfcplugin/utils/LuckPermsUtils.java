package com.outlook.tehbrian.tfcplugin.utils;

import com.outlook.tehbrian.tfcplugin.Main;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import org.bukkit.entity.Player;

public class LuckPermsUtils {

    private static final Main main = Main.getInstance();
    private static final LuckPermsApi luckPermsApi = main.getLuckPermsApi();

    private LuckPermsUtils() {
    }

    public static String getPlayerPrefix(Player player) {
        User user = luckPermsApi.getUserManager().getUser(player.getUniqueId());
        Contexts contexts = luckPermsApi.getContextManager().getApplicableContexts(user);
        return user.getCachedData().getMetaData(contexts).getPrefix();
    }

    public static String getPlayerSuffix(Player player) {
        User user = luckPermsApi.getUserManager().getUser(player.getUniqueId());
        Contexts contexts = luckPermsApi.getContextManager().getApplicableContexts(user);
        return user.getCachedData().getMetaData(contexts).getSuffix();
    }

    public static void setPlayerPrimaryGroup(Player player, String group) {
        User user = luckPermsApi.getUserManager().getUser(player.getUniqueId());
        user.setPrimaryGroup(group);
        main.getLuckPermsApi().getUserManager().saveUser(user);
    }
}
