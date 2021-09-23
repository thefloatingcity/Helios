package xyz.tehbrian.floatyplugin;

import com.google.inject.Inject;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

public final class LuckPermsService {

    private final FloatyPlugin floatyPlugin;
    private LuckPerms luckPerms;

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

    public String getPlayerPrefix(final Player player) {
        final User user = Objects.requireNonNull(this.luckPerms.getUserManager().getUser(player.getUniqueId()));

        final QueryOptions queryOptions = this.luckPerms.getContextManager().getQueryOptions(player);

        final CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);
        return metaData.getPrefix() == null ? "" : metaData.getPrefix();
    }

    public String getPlayerSuffix(final Player player) {
        final User user = Objects.requireNonNull(this.luckPerms.getUserManager().getUser(player.getUniqueId()));

        final QueryOptions queryOptions = this.luckPerms.getContextManager().getQueryOptions(player);

        final CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);
        return metaData.getSuffix() == null ? "" : metaData.getSuffix();
    }

    public void addPlayerGroup(final Player player, final String group) {
        Objects.requireNonNull(this.luckPerms);
        final UserManager userManager = this.luckPerms.getUserManager();

        final User user = Objects.requireNonNull(userManager.getUser(player.getUniqueId()));

        user.data().add(InheritanceNode.builder(group).build());
        userManager.saveUser(user);
    }

}
