package xyz.tehbrian.floatyplugin;

import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.user.User;
import xyz.tehbrian.floatyplugin.user.UserService;

@SuppressWarnings("ClassCanBeRecord")
public final class FlightService {

    private final UserService userService;
    private final FloatyPlugin floatyPlugin;

    @Inject
    public FlightService(
            final @NonNull UserService userService,
            final @NonNull FloatyPlugin floatyPlugin
    ) {
        this.userService = userService;
        this.floatyPlugin = floatyPlugin;
    }

    public void checkFlight(final Player player) {
        final User user = this.userService.getUser(player);

        if (player.hasPermission(Constants.Permissions.FLY) && user.flyBypassEnabled()) {
            this.enableFlight(player);
        } else {
            this.disableFlight(player);
        }
    }

    public void enableFlight(final Player player) {
        this.floatyPlugin.getServer().getScheduler().runTask(this.floatyPlugin, () -> {
            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
            }
        });
    }

    public void disableFlight(final Player player) {
        this.floatyPlugin.getServer().getScheduler().runTask(this.floatyPlugin, () -> {
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
            }
            if (player.isFlying()) {
                player.setFlying(false);
            }
        });
    }

}
